import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SignUpOptionComponent } from './sign-up-option.component';

describe('SignUpOptionComponent', () => {
  let component: SignUpOptionComponent;
  let fixture: ComponentFixture<SignUpOptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SignUpOptionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignUpOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
