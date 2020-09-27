import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './header/header.component';

import { SignUpComponent } from './sign-up/sign-up.component';
import { AccountCreationComponent } from './sign-up/account-creation/account-creation.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from './module/material/material.module';
import { MatFormFieldModule } from '@angular/material';
import { SignUpOptionComponent } from './sign-up/sign-up-option/sign-up-option.component';
import { SubredditComponent } from './subreddit/subreddit.component';
import { RoutingModule } from './module/routing/routing.module';
import { SubredditNotFoundComponent } from './subreddit/subreddit-not-found/subreddit-not-found.component';
import { NgxWebstorageModule, StrategyIndex } from 'ngx-webstorage';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SignUpComponent,
    AccountCreationComponent,
    SignUpOptionComponent,
    SubredditComponent,
    SubredditNotFoundComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MaterialModule,
    MatFormFieldModule,
    RoutingModule,
    NgxWebstorageModule.forRoot()    
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [SignUpComponent, AccountCreationComponent]
})
export class AppModule { }
