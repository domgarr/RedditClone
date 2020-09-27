import { Injectable } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: "root"
})
export class SignUpService {

  signUpFormGroup : FormGroup;

  private continueSource = new Subject<void>();
  private signUpSource = new Subject<void>();
  private backSource = new Subject<void>();
  private signUpSuccessfulSource = new Subject<void>();
  
  continueAnnounced$ = this.continueSource.asObservable();
  signUpAnnounced$ = this.signUpSource.asObservable();
  backAnnounced$ = this.backSource.asObservable();
  signUpSuccessfulAnnounced$ = this.signUpSuccessfulSource.asObservable();

  constructor(){
    this.initSignUpFormGroup();
  }

  announceContinue(){
    this.continueSource.next();
  }

  announceBack(){
    this.backSource.next();
  }

  announceSignUp(){
    this.signUpSource.next();
  }

  announceSignUpSuccessfull(){
    this.signUpSuccessfulSource.next();
  }

  initSignUpFormGroup(){
    let signUpFormGroup = new FormGroup({
      email: new FormControl('', [Validators.email, Validators.required]),
      username: new FormControl('', [Validators.minLength(8), Validators.maxLength(20), Validators.required]),
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

}
