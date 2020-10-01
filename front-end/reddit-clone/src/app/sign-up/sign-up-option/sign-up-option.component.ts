import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { SignUpService } from '../service/sign-up.service';

@Component({
  selector: 'app-sign-up-option',
  templateUrl: './sign-up-option.component.html',
  styleUrls: ['./sign-up-option.component.css']
})
export class SignUpOptionComponent implements OnInit {

  signUpFormGroup : FormGroup;

  constructor(private signUpService : SignUpService, private authService : AuthService) { 
    this.signUpFormGroup = signUpService.SignUpFormGroup;
  }

  ngOnInit() {
  }

  onContinue(){
    this.signUpService.announceContinue();
  }

}
