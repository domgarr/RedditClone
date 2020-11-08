import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { Observable, Subject } from 'rxjs';
import { LoginRequest } from '../../model/LoginRequest';
import { AuthService } from '../../service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  loginFormGroup: FormGroup;
  private incorrectPassword;

  private loginSuccessfulSource = new Subject<void>();
  loginSuccessfulAnnounced$ = this.loginSuccessfulSource.asObservable();

  private closeSource = new Subject<void>();
  closeAnnounced$ = this.closeSource.asObservable();

  constructor(private authService : AuthService) { 
    this.initLoginFormGroup();
  }

  initLoginFormGroup(){
      let loginFormGroup = new FormGroup({
        username: new FormControl('', [Validators.minLength(8), Validators.maxLength(20), Validators.required, Validators.pattern('^[a-zA-Z1-9]+(_?)([a-zA-Z1-9]+)$')]),
        password: new FormControl('', [Validators.minLength(8), Validators.maxLength(20), Validators.required, this.incorrectPasswordValidator()])
      });
  
      //Required to add after creating signUpFormGroup because retypePassword uses password FormControl in validation password equality.
      this.loginFormGroup = loginFormGroup;
      
  }

  /* The setter should be private, we don't want to form group to be re-initialized */
  get LoginFormGroup() : FormGroup {
    
    if(!this.loginFormGroup){
      throw new Error("SignUpFormGroup has not been initialized yet.");
    }
    return this.loginFormGroup
  }

  incorrectPasswordValidator() : ValidatorFn {
    return (control : AbstractControl): {[key:string]: any} | null =>{
      //If the function returns null, the Validation has passed.
      return this.incorrectPassword ? {incorrectPasswordError: {message: "INCORRECT PASSWORD"}} : null;
    };
  }

  loginObservable = new Observable((observer) => {
    let loginRequest : LoginRequest = new LoginRequest();
    loginRequest.Username = this.loginFormGroup.get("username").value;
    loginRequest.Password = this.loginFormGroup.get("password").value;

    this.authService.login(loginRequest).subscribe(()=>{
      observer.next();
      observer.complete();

     
    },
    (error : HttpErrorResponse) =>{
      /* This is a confusing approach, but the above sets the incorrectPassword to true
      and forces the password FormControl to update and renders the INCORRECT PASSWORD error message,
      we then reset the incorrectPassword back to false just after, such that when the user changes the password
      to something new, the form is updated now to show the error, since technically the password is new because
      there was a change.
      */

      this.incorrectPassword = true;
      this.loginFormGroup.get("password").updateValueAndValidity();
      this.incorrectPassword = false;
      observer.next(error);
      observer.complete();
    });
    
  });

  announceLoginSuccessful(){
    this.loginSuccessfulSource.next();
  }

  announceClose(){
    this.loginFormGroup.reset();
    this.closeSource.next();
  }

 
}

