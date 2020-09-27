import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { LoginRequest } from '../model/LoginRequest';
import { LoginResponse } from '../model/LoginResponse';
import { SignUpRequest } from '../model/SignUpRequest';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly BASE_URL = ""

  constructor(private httpClient : HttpClient, private localStorage : LocalStorageService) { }

  signUp(signUpRequest : SignUpRequest) : Observable<any>{
    return this.httpClient.post('/api/auth/signup', signUpRequest, {responseType: 'text'});
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
}
