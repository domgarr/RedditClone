import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { SignUpService } from '../service/sign-up.service';

@Component({
  selector: 'app-sign-up-option',
  templateUrl: './sign-up-option.component.html',
  styleUrls: ['./sign-up-option.component.css']
})
export class SignUpOptionComponent implements OnInit {

  signUpFormGroup : FormGroup;

  constructor(private signUpService : SignUpService) { 
    this.signUpFormGroup = signUpService.SignUpFormGroup;
  }

  ngOnInit() {
  }

  onContinue(){
    this.signUpService.announceContinue();
  }

}
