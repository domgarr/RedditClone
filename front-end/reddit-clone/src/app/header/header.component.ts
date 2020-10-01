import { Component,OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { AccountCreationComponent } from '../sign-up/account-creation/account-creation.component';
import { SignUpService } from '../sign-up/service/sign-up.service';
import { SignUpComponent } from '../sign-up/sign-up.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})

//TODO: Test entire component.
export class HeaderComponent implements OnInit {

  private signUpSuccessfulSubscription: Subscription;

  private signUpDialogRef : MatDialogRef<SignUpComponent, any>;
  private accountCreationDialogRef : MatDialogRef<AccountCreationComponent, any>;


  constructor(public signUpDialog : MatDialog, private signUpService : SignUpService) { 
    this.subscribeToSignUpService(signUpService);
  }

  subscribeToSignUpService(signUpService : SignUpService){
    this.signUpSuccessfulSubscription = signUpService.signUpSuccessfulAnnounced$.subscribe(()=>{
      this.closeSignUp();
    });
  }

  ngOnInit() {
  }

  openSignUp(){
    this.signUpDialogRef = this.signUpDialog.open(SignUpComponent);
    this.signUpDialogRef.updateSize("800","800")

    this.signUpDialog.afterAllClosed.subscribe(()=>{
      this.signUpService.announceClose();
    });
  }

  closeSignUp(){
    this.signUpDialog.closeAll();
    this.signUpService.announceClose();
  }
}
