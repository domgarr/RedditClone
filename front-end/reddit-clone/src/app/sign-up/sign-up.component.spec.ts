import { HttpClientModule } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatExpansionPanelDescription } from '@angular/material/expansion';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AccountCreationComponent } from './account-creation/account-creation.component';
import { MaterialModule } from '../module/material/material.module';
import { AuthService } from '../service/auth.service';
import { SignUpService } from '../service/sign-up.service';

import { SignUpComponent } from './sign-up.component';

describe('SignUpComponent', () => {
  let component: SignUpComponent;
  let fixture: ComponentFixture<SignUpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SignUpComponent, AccountCreationComponent ],
      imports:[BrowserAnimationsModule, HttpClientModule, MaterialModule, FormsModule, ReactiveFormsModule],
      providers:[AuthService, SignUpService]
    })
    .compileComponents()
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignUpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  

  describe("Test email form control.", () => {
    it('form should be valid', async(()=>{
      component.emailForm.get("email").setValue('test@gmail');
      expect(component.emailForm.valid).toBeTruthy();
    }));

    it('given a empty email value, form should be invalid', async(()=>{
      component.emailForm.get("email").setValue('');
      expect(component.emailForm.invalid).toBeTruthy();
    }));
  
    it('given a email string with incorrect email syntax, form should be invalid', async(()=>{
      component.emailForm.get("email").setValue('test');
      expect(component.emailForm.invalid).toBeTruthy();
    }));
  });
  describe("createSignUpFormGroup()", () => {

  it("Calling createSignUpFormGroup(), should instiate FormGroup emailForm", async(()=>{
      component.createSignUpFormGroup();
      let emailFormGroup : FormGroup = component.emailForm;
  
      expect(emailFormGroup).toBeDefined();
  }));

  it("Calling createSignUpFormGroup(), should contain a FormControl named 'email'", async(()=>{
    component.createSignUpFormGroup();
    let emailFormControl : AbstractControl = component.emailForm.get("email");

    expect(emailFormControl).toBeDefined();
  }));

});

describe('subscribeToSignUpService()', ()=>{
  it('After calling subscribeToSignUpService(), three subscriptions should be defined.', ()=>{
    let signUpService : SignUpService = new SignUpService();
    component.subscribeToSignUpService(signUpService);

    expect(component.signUpSubscription).toBeDefined();
    expect(component.backingSubscription).toBeDefined();
    expect(component.continuingSubscription).toBeDefined();

  });
});

describe('renderAccountCreation()', ()=>{
    it('After called, isContinuing should equal true.', ()=>{
      //By default isContinuning is set to false.
      expect(component.isContinuing).toBeFalsy();

      component.renderAccountCreation();
      expect(component.isContinuing).toBeTruthy();
    });
    it('After called, AccountCreation component should render.', ()=>{
      component.renderAccountCreation();
      fixture.detectChanges();

      let accountCreationComp = fixture.debugElement.query(By.css("#account-creation"));
      expect(accountCreationComp.properties["disabled"]).toBeFalsy();
    });
    it('By default, AccountCreation component should NOT render, thus the component should be null.', ()=>{
      let accountCreationComp = fixture.debugElement.query(By.css("#account-creation"));
      expect(accountCreationComp).toBeNull();
    });
  });

  describe('renderSignUpOptions()', ()=>{
    it('By default isContinuing is false after called, isContinuing should equal false.', ()=>{
      //By default isContinuning is set to false.
      expect(component.isContinuing).toBeFalsy();

      //Set to true
      component.isContinuing = true;
      expect(component.isContinuing).toBeTruthy();

      component.renderSignUpOptions();
      expect(component.isContinuing).toBeFalsy();
    });

    it('isContinuing is set to true after called, SignUpOptions should be rendered.', ()=>{
      component.isContinuing = true;
      fixture.detectChanges();

      let signUpOptionsComp = fixture.debugElement.query(By.css("#sign-up-options"));
      expect(signUpOptionsComp).toBeNull();

      component.renderSignUpOptions();
      fixture.detectChanges();
      
      signUpOptionsComp = fixture.debugElement.query(By.css("#sign-up-options"));
      expect(signUpOptionsComp).toBeDefined();

    });

  });

  describe('instantiateSignUpRequest()', ()=>{
    

    it('')
  });
  

});
