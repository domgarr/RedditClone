import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { LoginRequest } from '../model/LoginRequest';
import { LoginResponse } from '../model/LoginResponse';
import { SignUpRequest } from '../model/SignUpRequest';
import { map } from 'rxjs/operators';
import { ExistCheckResponse } from '../model/ExistCheckResponse';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly BASE_URL = "/api/auth";
  private readonly SIGN_UP_URL = this.BASE_URL + "/signup";
  private readonly CHECK_EMAIL_EXISTS = this.BASE_URL + "/check/email"; 
  private readonly CHECK_USERNAME_EXISTS = this.BASE_URL + "/check/username/"; 


  constructor(private httpClient : HttpClient, private localStorage : LocalStorageService) { }

  signUp(signUpRequest : SignUpRequest) : Observable<any>{
    return this.httpClient.post(this.SIGN_UP_URL, signUpRequest, {responseType: 'text'});
  }

  login(loginRequest : LoginRequest) : Observable<any> {
    return this.httpClient.post<LoginResponse>('api/auth/login', loginRequest).pipe(map(response =>{
      this.localStorage.store('authenticationToken', response.authenticationToken);
      this.localStorage.store('username', response.username);
      this.localStorage.store('refreshToken', response.refreshToken);
      this.localStorage.store('expiresAt', response.expiresAt);
    })
    )
  }

  existsByEmail(email : string) : Observable<any> {
    return this.httpClient.post<ExistCheckResponse>(this.CHECK_EMAIL_EXISTS, {email : email} ,{ observe: 'response' });
  }

  existsByUsername(username : string) : Observable<any> {
    return this.httpClient.get<ExistCheckResponse>(this.CHECK_USERNAME_EXISTS + username ,{ observe: 'response' });
  }
}
