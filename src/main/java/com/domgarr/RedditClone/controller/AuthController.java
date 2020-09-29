package com.domgarr.RedditClone.controller;

import com.domgarr.RedditClone.dto.*;
import com.domgarr.RedditClone.exception.DataIntegrityError;
import com.domgarr.RedditClone.service.AuthService;
import com.domgarr.RedditClone.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){

        return new ResponseEntity<>(authService.signup(registerRequest), HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    private ResponseEntity<String> verifyAccount(@PathVariable String token){
         authService.verifyAccount(token);
         return new ResponseEntity<>("Account was verified.", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){

        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/login")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token deleted successfully.");
    }


    @GetMapping("/check/username/{username}") public ResponseEntity<String> checkUsernameExists(@PathVariable String username){
        boolean exists = authService.checkUsernameExists(username);

        String response;
        HttpStatus status;

        if(exists){
            response = "Username is taken";
            status = HttpStatus.BAD_REQUEST;
        }else{
            response = "Username is unique.";
            status = HttpStatus.OK;
        }

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/check/email") public ResponseEntity<String> checkEmailExists(@RequestBody EmailRequest emailRequest){
        boolean exists = authService.checkEmailExists(emailRequest.getEmail());

        String response;
        HttpStatus status;

        if(exists){
            response = "Email is being used.";
            status = HttpStatus.BAD_REQUEST;
        }else{
            response = "Email is unique.";
            status = HttpStatus.OK;
        }

        return ResponseEntity.status(status).body(response);
    }

}
