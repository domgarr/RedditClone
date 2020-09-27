import { Component, OnInit } from '@angular/core';
import { FormGroup} from '@angular/forms';
import { SignUpService } from '../service/sign-up.service';

@Component({
  selector: 'app-account-creation',
  templateUrl: './account-creation.component.html',
  styleUrls: ['./account-creation.component.css']
})
export class AccountCreationComponent implements OnInit {

  public signUpFormGroup : FormGroup;

  constructor(private signUpService : SignUpService) { 
    this.signUpFormGroup = signUpService.SignUpFormGroup;
  }

  ngOnInit() {
  }

  onBack(){
    this.signUpService.announceBack();
  }

  onChangePassword(){
    //Whenever the password field is altered, we must re-validate the retypedPassword FormControl.
    this.signUpFormGroup.controls["retypePassword"].updateValueAndValidity();
  }

  onSignUp(){
    this.signUpService.announceSignUp();
  }
}
