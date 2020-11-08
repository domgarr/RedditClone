import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from './service/login.service';
import { timer } from 'rxjs';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public loginFormGroup : FormGroup;

  constructor(private loginService : LoginService, private snackbar : MatSnackBar, private router : Router) {
    this.loginFormGroup = loginService.LoginFormGroup;
   }

  onLogin(){
    this.loginService.loginObservable.subscribe( (isError : HttpErrorResponse) =>{
      if(isError){
          switch(isError.status){
            case 504 :
              this.displaySnackBar("There seems to be a problem on our end. Please try again.", 2000);
              break;
            default:
              console.log("Error code not handled."); //Log.
          }
      }else{
        this.loginService.announceLoginSuccessful();
        this.redirectToSubredditAll();
      }
    });
  }

  redirectToSubredditAll(){
    this.displaySnackBar("Login successfull. Redirecting...", 2000);
    const timerSource = timer(2000)
    timerSource.subscribe(()=>{
      //Redirect after 2 seconds.
      this.router.navigateByUrl("/r/all");
    });
  }

  displaySnackBar(message : string, lengthInMillis : number){
    this.snackbar.open(message, null, {duration: lengthInMillis});
  }
}
