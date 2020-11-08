import { Component,OnInit, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { LoginComponent } from '../login/login.component';
import { LoginService } from '../login/service/login.service';
import { AuthService } from '../service/auth.service';
import { AccountCreationComponent } from '../sign-up/account-creation/account-creation.component';
import { SignUpService } from '../sign-up/service/sign-up.service';
import { SignUpComponent } from '../sign-up/sign-up.component';
import { timer } from 'rxjs';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})

//TODO: Test entire component.
export class HeaderComponent implements OnInit {

  private signUpSuccessfulSubscription: Subscription;
  private loginSuccessSubscription: Subscription;

  private signUpDialogRef : MatDialogRef<SignUpComponent, any>;
  private loginDialogRef : MatDialogRef<LoginComponent, any>;

  private accountCreationDialogRef : MatDialogRef<AccountCreationComponent, any>;
  isTokenExpired : boolean;


  constructor(public dialog : MatDialog, private signUpService : SignUpService, private loginService: LoginService, private authService : AuthService, private snackbar : MatSnackBar, private router : Router) { 
    this.subscribeToSignUpServices(signUpService);
    this.subscribeToLoginServices(loginService);
  
    this.isTokenExpired = authService.isTokenExpired();
  }

  subscribeToSignUpServices(signUpService : SignUpService){
    this.signUpSuccessfulSubscription = signUpService.signUpSuccessfulAnnounced$.subscribe(()=>{
      this.closeSignUp();
    });

  }

  subscribeToLoginServices(loginService : LoginService){
    this.loginSuccessSubscription = loginService.loginSuccessfulAnnounced$.subscribe(()=>{
      this.closeSignUp();
      this.isTokenExpired = this.authService.isTokenExpired();
    });
  }

  ngOnInit() {
  }

  openSignUp(){
    this.signUpDialogRef = this.dialog.open(SignUpComponent);
    this.signUpDialogRef.updateSize("800","800");

    this.dialog.afterAllClosed.subscribe(()=>{
      this.signUpService.announceClose();
    });
  }

  closeSignUp(){
    this.dialog.closeAll();
    this.signUpService.announceClose();
  }

  openLogin(){
    this.loginDialogRef = this.dialog.open(LoginComponent);
    this.loginDialogRef.updateSize("800","800");

    this.dialog.afterAllClosed.subscribe(()=>{
      this.loginService.announceClose();
    });
  }

  closeLogin(){
    this.dialog.closeAll();
  }

  onLogout(){
    this.authService.logout();
    
    //Need to create a service for handling snackbars.
    this.snackbar.open("Logging off...", null, {duration: 1000});

    //I believe delaying the log out by a second and adding a snackbar, adds a feel to logging out.
    const timerSource = timer(1000)
    timerSource.subscribe(()=>{
      //Redirect after 1 seconds
      this.router.navigateByUrl("/r/all");
      this.isTokenExpired = true;
      this.snackbar.open("Logged out.", null, {duration: 1000});

    });
  }

  ngOnDestroy(){
    this.signUpSuccessfulSubscription.unsubscribe();
    this.loginSuccessSubscription.unsubscribe;
  }


}
