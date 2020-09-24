package com.domgarr.RedditClone.controller;

import com.domgarr.RedditClone.dto.AuthenticationResponse;
import com.domgarr.RedditClone.dto.RefreshTokenRequest;
import com.domgarr.RedditClone.dto.RegisterRequest;
import com.domgarr.RedditClone.dto.LoginRequest;
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
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration successful.", HttpStatus.OK);
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

}
