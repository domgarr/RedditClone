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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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


    @GetMapping("/check/username/{username}") public ResponseEntity<Map<Object, Object>> checkUsernameExists(@PathVariable String username){
        boolean exists = authService.checkUsernameExists(username);
        String message = exists ? "Username is taken." : "Username is unique.";
        return ResponseEntity.status(HttpStatus.OK).body(buildExistCheckResponseBody(exists, message));
    }

    @PostMapping("/check/email") public ResponseEntity<Map<Object, Object>> checkEmailExists(@RequestBody EmailRequest emailRequest){
        boolean exists = authService.checkEmailExists(emailRequest.getEmail());
        String message = exists ? "Email is being used." : "Email is unique.";
        return ResponseEntity.status(HttpStatus.OK).body(buildExistCheckResponseBody(exists, message));
    }

    //TODO: If used else where in the program, create a class.
    Map<Object, Object> buildExistCheckResponseBody(boolean exists, String message){
        Map<Object, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("message", message);
        return response;
    }

}
