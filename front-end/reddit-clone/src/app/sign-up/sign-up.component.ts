import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { LoginRequest } from '../model/LoginRequest';
import { SignUpRequest } from '../model/SignUpRequest';
import { AuthService } from '../service/auth.service';
import { SignUpService } from './service/sign-up.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  public isContinuing : boolean = false;

  continuingSubscription : Subscription;
  backingSubscription : Subscription;
  signUpSubscription : Subscription;
  closeSubscription : Subscription;

  signUpFormGroup: FormGroup;

  constructor(private signUpService : SignUpService,  private authService : AuthService, private snackBar: MatSnackBar) { 
      this.subscribeToSignUpService(signUpService);    
      this.signUpFormGroup = signUpService.SignUpFormGroup;
    }

  ngOnInit() {
  }

  subscribeToSignUpService(signUpService : SignUpService){
    this.continuingSubscription = signUpService.continueAnnounced$.subscribe(
      () =>{
        this.renderAccountCreation();
      });
       
      this.backingSubscription = signUpService.backAnnounced$.subscribe(
        ()=>{
          this.renderSignUpOptions();
      });

      this.signUpSubscription = signUpService.signUpAnnounced$.subscribe(
        ()=>{
          this.onSignUp(); 
        }
      );

      this.closeSubscription = signUpService.closeAnnounced$.subscribe(
        ()=>{
        this.signUpFormGroup.reset();
      });
  }

renderAccountCreation(){
    this.isContinuing = true;
  }

  renderSignUpOptions(){
    this.isContinuing = false;
  }

  ngOnDestroy() {
    this.continuingSubscription.unsubscribe();
    this.backingSubscription.unsubscribe();
    this.signUpSubscription.unsubscribe();
  }

  // TODO: Write unit test for this method.
  onSignUp(){
    let signUpRequest : SignUpRequest;
    try {
      signUpRequest = this.instantiateSignUpRequestFromFormGroup();
    }catch(error){
      console.log(error);
    }

    this.authService.signUp(signUpRequest).subscribe(data=>{
      this.displaySnackBar("Welcome to Reddit! Please check your email for the activation URL :)");
      this.signUpService.announceSignUpSuccessfull();
    }, error =>{
      this.displaySnackBar("There seemed to be a problem on our end. Please try again.");
    });
  }

  /**
   * 
   * @param signUpRequestTemp - A SignUpRequest that has defined username and password fields.
   */

  instantiateSignUpRequestFromFormGroup() : SignUpRequest{
    
    if(!this.signUpFormGroup.valid){
      throw new Error("SignUpFormGroup elements are invalid. Please correct and try again.");
    }

    let signUpRequest = new SignUpRequest();
    signUpRequest.Email = this.signUpFormGroup.get("email").value;
    signUpRequest.Username = this.signUpFormGroup.get("username").value;
    signUpRequest.Password = this.signUpFormGroup.get("password").value;

    return signUpRequest;
  }

  displaySnackBar(message : string){
    this.snackBar.open(message, "Dismiss", {
      duration : 10000
    });
  }
}
