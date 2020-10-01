package com.domgarr.RedditClone.service;

import com.domgarr.RedditClone.dto.*;
import com.domgarr.RedditClone.exception.DataIntegrityError;
import com.domgarr.RedditClone.exception.SpringRedditException;
import com.domgarr.RedditClone.model.NotificationEmail;
import com.domgarr.RedditClone.model.User;
import com.domgarr.RedditClone.model.VerificationToken;
import com.domgarr.RedditClone.repository.UserRepository;
import com.domgarr.RedditClone.repository.VerificationTokenRepository;
import com.domgarr.RedditClone.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final ValidationService validationService;

    private final String successfulSignUpString = "User Registration successful.";

    @Transactional
    public String signup(RegisterRequest registerRequest){

        String errorMessage = validationService.validate(registerRequest);
        if(!errorMessage.isEmpty()){
            throw new DataIntegrityError(errorMessage);
        }

        //Check if username is unique.
        if(checkUsernameExists(registerRequest.getUsername())){
            throw new DataIntegrityError("username is being used.");
        }

        //Check if email is unique.
        if(checkEmailExists(registerRequest.getEmail())){
            throw new DataIntegrityError("email is being used.");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        User savedUser = userRepository.save(user);
        String token = generateVerificationToken(savedUser);
        mailService.sendMail(new NotificationEmail("Please activate your account", user.getEmail(),
                "Please click the link below: http://localhost:8081/api/auth/accountVerification/" + token));

        return successfulSignUpString;
    }

    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken =verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() ->
            new SpringRedditException("Invalid Token")
        );

        fetchUserAndEnable(verificationToken.get());

        //TODO: Delete token from database.

    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new SpringRedditException("Username not found: " + username)
        );
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication, null);
        return  AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getExpirationAmount()))
                .username(loginRequest.getUsername())
                .build();

    }

    @Transactional(readOnly = true) //TODO: Understand why readOnly true is being used here.
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal= (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername()).orElseThrow( () -> new SpringRedditException("User with username " + principal.getUsername() + " not found."));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
         refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
         String token = jwtProvider.generateToken(null, refreshTokenRequest.getUsername());
         return AuthenticationResponse.builder()
                 .authenticationToken(token)
                 .refreshToken(refreshTokenRequest.getRefreshToken())
                 .expiresAt(Instant.now().plusMillis(jwtProvider.getExpirationAmount()))
                 .username(refreshTokenRequest.getUsername())
                 .build();
    }

    public boolean checkUsernameExists(String username) {
        RegisterRequest registerRequest = RegisterRequest.builder().username(username).build();

        String errorMessage = validationService.validateProperty(registerRequest, "username");
        if(!errorMessage.isEmpty()){
            throw new DataIntegrityError(errorMessage);
        }

        return userRepository.existsByUsername(username);
    }

    public boolean checkEmailExists(String email) {
        EmailRequest emailRequest = new EmailRequest(email);
        //Check if the email is given in the proper format before continuing.
        String errorMessage = validationService.validateProperty(emailRequest, "email");
        if(!errorMessage.isEmpty()){
            throw new DataIntegrityError(errorMessage);
        }

        return  userRepository.existsByEmailAndEnabled(email).byteValue() == 1 ? true : false;
    }
}
