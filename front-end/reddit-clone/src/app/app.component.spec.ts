import { TestBed, async } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { MaterialModule } from './module/material/material.module';

//Describe to start test block.
describe('AppComponent', () => {
  //Allow all async code to be finished before starting testing.
  beforeEach(async(() => {
    TestBed.configureTestingModule({ //TestBed needs to be created, to create an angular environment for the component being tested.
      declarations: [
        AppComponent,
        HeaderComponent
      ],
      imports :[MaterialModule, BrowserAnimationsModule]
    }).compileComponents(); //Compile component functions.
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent); 
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'reddit-clone'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('reddit-clone');
  });
});
