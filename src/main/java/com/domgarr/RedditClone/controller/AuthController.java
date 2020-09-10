package com.domgarr.RedditClone.controller;

import com.domgarr.RedditClone.dto.RegisterRequest;
import com.domgarr.RedditClone.model.VerificationToken;
import com.domgarr.RedditClone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

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
}
