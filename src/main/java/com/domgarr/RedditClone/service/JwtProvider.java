package com.domgarr.RedditClone.service;

import com.domgarr.RedditClone.exception.SpringRedditException;
import io.jsonwebtoken.Jwts;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.security.*;

@Service
public class JwtProvider {
    private KeyStore keyStore;

    //@PostConstruct is used here because we would like to run this method containing initializing logic after DI is performed.
    @PostConstruct
    public void init(){
        try{
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            Resource resource = new ClassPathResource("/redditclone.jks");
            InputStream resourceInputStream = resource.getInputStream();
            //InputStream resourceAsStream = getClass().getResourceAsStream("redditclone.jks");
            keyStore.load(resourceInputStream, "password".toCharArray());
        }catch (Exception e){
            throw new SpringRedditException("Exception occured when loading keystore.");
        }
    }


   public String generateToken(Authentication authentication){
       User principle = (User) authentication.getPrincipal();
       return Jwts.builder()
               .setSubject(principle.getUsername())
               .signWith(getPrivateKey())
               .compact();
   }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("redditclonekey", "password".toCharArray());
        } catch (Exception e) {
            throw new SpringRedditException("Exception occured while retrieving public key from the keystore." + e);
        }
    }


}
