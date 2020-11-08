import { Injectable } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { Observable, Subject, Subscription } from 'rxjs';
import { ExistCheckResponse } from '../../model/ExistCheckResponse';
import { AuthService } from '../../service/auth.service';
import { timer } from 'rxjs';
import { timeout} from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpResponse } from '@angular/common/http';


@Injectable({
  providedIn: "root"
})
export class SignUpService {
  private continueSource = new Subject<void>();
  private signUpSource = new Subject<void>();
  private backSource = new Subject<void>();
  private signUpSuccessfulSource = new Subject<void>();
  private closeSource = new Subject<void>();

  signUpFormGroup : FormGroup;
  private timerForEmailExistsSubscription : Subscription;
  private timerForUsernameExistsSubscription : Subscription;
  
  continueAnnounced$ = this.continueSource.asObservable();
  signUpAnnounced$ = this.signUpSource.asObservable();
  backAnnounced$ = this.backSource.asObservable();
  signUpSuccessfulAnnounced$ = this.signUpSuccessfulSource.asObservable();
  closeAnnounced$ = this.closeSource.asObservable();

  constructor(private authService : AuthService, private snackbar : MatSnackBar){
    this.initSignUpFormGroup();
  }

  announceContinue(){
    this.continueSource.next();
  }

  announceBack(){
    this.backSource.next();
  }

  announceClose(){
    this.signUpFormGroup.reset();
    this.closeSource.next();
  }

  announceSignUp(){
    this.signUpSource.next();
  }

  announceSignUpSuccessfull(){
    this.signUpSuccessfulSource.next();
  }

  initSignUpFormGroup(){
    let signUpFormGroup = new FormGroup({
      email: new FormControl('', [Validators.email, Validators.required], this.emailExistsValidator()),
      username: new FormControl('', [Validators.minLength(8), Validators.maxLength(20), Validators.required, Validators.pattern('^[a-zA-Z1-9]+(_?)([a-zA-Z1-9]+)$')], this.usernameExistsValidator()),
      password: new FormControl('', [Validators.minLength(8), Validators.maxLength(20), Validators.required])
    });

    //Required to add after creating signUpFormGroup because retypePassword uses password FormControl in validation password equality.
    this.SignUpFormGroup = signUpFormGroup;
    this.SignUpFormGroup.addControl('retypePassword', new FormControl('', [Validators.minLength(8), Validators.maxLength(20), Validators.required, this.passwordEqualityValidator()]));
    }

  get SignUpFormGroup() : FormGroup{
    
    if(!this.signUpFormGroup){
      throw new Error("SignUpFormGroup has not been initialized yet.");
    }
    return this.signUpFormGroup
  }

  set SignUpFormGroup(signUpFormGroup : FormGroup) {
    this.signUpFormGroup = signUpFormGroup;        
  }

  passwordEqualityValidator() : ValidatorFn {
    return (control : AbstractControl): {[key:string]: any} | null =>{
      let password : string = this.SignUpFormGroup.get("password").value;
      let retypedPassword : string = control.value;

      const equality : boolean = password === retypedPassword;
      //If the function returns null, the Validation has passed.
      return equality ? null : {passwordEqualityError: {message: "PASSWORDS DO NOT MATCH"}};
    };
  }

  emailExistsValidator() : AsyncValidatorFn {
    return (control : AbstractControl): Observable<{[key:string]: any} | null> =>{
        return this.emailExistsObservable;
    };
  }

  
  usernameExistsValidator() : AsyncValidatorFn {
    return (control : AbstractControl): Observable<{[key:string]: any} | null> =>{
        return this.usernameExistsObservable;
    };
  }
  



  //TODO: Is it possible for me to pass AbstractControl to an observable?? Angular.io has a tutorial on async validators. 
  emailExistsObservable = new Observable((observer) => {
    let email : AbstractControl = this.SignUpFormGroup.get("email");
    
    //IF the email isn't the correct syntax, skip checking if email exists.
    if(email.errors){
      if(email.errors.email){
        observer.next(null);
        observer.complete();
      }
    }

    //the API call will not be made until the user stops typing.
    if(this.timerForEmailExistsSubscription && !this.timerForEmailExistsSubscription.closed){
      //Everytime a the value changes we unsubscribe from the previous subscription.
      this.timerForEmailExistsSubscription.unsubscribe();
    }

    //Init timer to 1.5 seconds.
    const timerSource = timer(1500);

    //Then we subscribe to a new one. After 1.5s the API call will be made, as long as the source isn't re-instantiated / reset by the user typing again.
    this.timerForEmailExistsSubscription = timerSource
      .pipe(timeout(2500))
      .subscribe(
        () => {
        this.authService.existsByEmail(email.value).subscribe((response : HttpResponse<ExistCheckResponse>) =>{  
          !response.body.exists ? observer.next(null) : observer.next({emailExistsError: true});
          this.timerForEmailExistsSubscription.unsubscribe();
          observer.complete();
          });
        },
        e => { 
          // TODO: Figure out how to catch errors while using timeout. 
          /* Don't know why this isn't catching a 504 error */ 
          this.throwTimeoutError(observer);
        }   
      );
  })


  //TODO: Is it possible for me to pass AbstractControl to an observable?? Angular.io has a tutorial on async validators. 
  usernameExistsObservable = new Observable((observer) => {
    let username : AbstractControl = this.SignUpFormGroup.get("username");
    
    //IF the email isn't the correct syntax, skip checking if email exists.
    if(username.errors){
      if(username.errors){
        observer.next(null);
        observer.complete();
      }
    }

    //the API call will not be made until the user stops typing.
    if(this.timerForUsernameExistsSubscription && !this.timerForUsernameExistsSubscription.closed){
      //Everytime a the value changes we unsubscribe from the previous subscription.
      this.timerForUsernameExistsSubscription.unsubscribe();
    }
    //Init timer to 1.5 seconds.
    const timerSource = timer(1500);

    //Then we subscribe to a new one. After 1.5s the API call will be made, as long as the source isn't re-instantiated / reset by the user typing again.
    this.timerForUsernameExistsSubscription = timerSource
      .pipe(timeout(2500))
      .subscribe(
        () => {
        this.authService.existsByUsername(username.value).subscribe((response : HttpResponse<ExistCheckResponse>) =>{  
          !response.body.exists ? observer.next(null) : observer.next({usernameExistsError: true});
          this.timerForUsernameExistsSubscription.unsubscribe();
          observer.complete();
          });
        },
        e => { 
          // TODO: Figure out how to catch errors while using timeout. 
          /* Don't know why this isn't catching a 504 error */ 
          this.throwTimeoutError(observer);
        }   
      );
  })

  throwTimeoutError(observer){
    this.displayErrorSnackBar("Sorry, it seems as the server timed out. Please try again.");   
    this.timerForEmailExistsSubscription.unsubscribe();
    //Notify user of timeout.
    observer.next({timeoutError: true});
    observer.complete();
  }

  displayErrorSnackBar(errorMessage : string){
    this.snackbar.open(errorMessage, "Dismiss", {
      duration : 10000
    });
  }

}