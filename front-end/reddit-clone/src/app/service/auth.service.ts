import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { LoginRequest } from '../model/LoginRequest';
import { LoginResponse } from '../model/LoginResponse';
import { SignUpRequest } from '../model/SignUpRequest';
import { map } from 'rxjs/operators';
import { ExistCheckResponse } from '../model/ExistCheckResponse';
import { JwtHelperService } from '@auth0/angular-jwt';



@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly BASE_URL = "/api/auth";
  private readonly SIGN_UP_URL = this.BASE_URL + "/signup";
  private readonly CHECK_EMAIL_EXISTS = this.BASE_URL + "/check/email"; 
  private readonly CHECK_USERNAME_EXISTS = this.BASE_URL + "/check/username/"; 

  private readonly EXPIRES_AT = "expiresAt";
  private readonly REFRESH_TOKEN = "refreshToken";
  private readonly USERNAME = "username";
  private readonly AUTHENTICATION_TOKEN = "authenticationToken";

  private jwtHelper : JwtHelperService;


  constructor(private httpClient : HttpClient, private localStorage : LocalStorageService) {
    this.jwtHelper = new JwtHelperService();
   }

  signUp(signUpRequest : SignUpRequest) : Observable<any>{
    return this.httpClient.post(this.SIGN_UP_URL, signUpRequest, {responseType: 'text'});
  }

  login(loginRequest : LoginRequest) : Observable<any> {
    return this.httpClient.post<LoginResponse>('api/auth/login', loginRequest).pipe(map(response =>{
      this.localStorage.store(this.AUTHENTICATION_TOKEN, response.authenticationToken);
      this.localStorage.store(this.USERNAME, response.username);
      this.localStorage.store(this.REFRESH_TOKEN, response.refreshToken);
      this.localStorage.store(this.EXPIRES_AT, response.expiresAt);
    })
    )
  }

  logout(){
    this.localStorage.clear(this.AUTHENTICATION_TOKEN);
    this.localStorage.clear(this.USERNAME);
    this.localStorage.clear(this.REFRESH_TOKEN);
    this.localStorage.clear(this.EXPIRES_AT);
  }

  existsByEmail(email : string) : Observable<any> {
    return this.httpClient.post<ExistCheckResponse>(this.CHECK_EMAIL_EXISTS, {email : email} ,{ observe: 'response' });
  }

  existsByUsername(username : string) : Observable<any> {
    return this.httpClient.get<ExistCheckResponse>(this.CHECK_USERNAME_EXISTS + username ,{ observe: 'response' });
  }

  isTokenExpired() : boolean{
    let authToken = this.localStorage.retrieve(this.AUTHENTICATION_TOKEN);
    return this.jwtHelper.isTokenExpired(authToken);
  }
}
