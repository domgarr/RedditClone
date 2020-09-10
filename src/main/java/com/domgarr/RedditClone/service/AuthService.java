package com.domgarr.RedditClone.service;

import com.domgarr.RedditClone.dto.RegisterRequest;
import com.domgarr.RedditClone.exception.SpringRedditException;
import com.domgarr.RedditClone.model.NotificationEmail;
import com.domgarr.RedditClone.model.User;
import com.domgarr.RedditClone.model.VerificationToken;
import com.domgarr.RedditClone.repository.UserRepository;
import com.domgarr.RedditClone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional
    public void signup(RegisterRequest registerRequest){
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
}
