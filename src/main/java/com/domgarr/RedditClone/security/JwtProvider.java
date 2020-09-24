package com.domgarr.RedditClone.security;

import com.domgarr.RedditClone.exception.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.security.*;
import java.sql.Date;
import java.time.Instant;

@Service
public class JwtProvider {
    private KeyStore keyStore;



    @Value("${jwt.expiration.time}")
    private Long expirationAmount;


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


   public String generateToken(Authentication authentication, String username){
       if(authentication != null){
           User principle = (User) authentication.getPrincipal();
           username = principle.getUsername();
       }

       return Jwts.builder()
               .setSubject(username)
               .signWith(getPrivateKey()) //Asymmetric
               .setExpiration(Date.from(Instant.now().plusMillis(expirationAmount)))
               .compact();
   }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("redditclonekey", "password".toCharArray());
        } catch (Exception e) {
            throw new SpringRedditException("Exception occured while retrieving public key from the keystore." + e);
        }
    }

    public boolean validateToken(String jwt){
        // https://dev.to/d_tomov/jwt-bearer-authentication-authorization-with-spring-security-5-in-a-spring-boot-app-2cfe
        Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("redditclonekey").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception occurred while getting public key.");
        }
    }

    public String getUsernameFromJwt(String jwt){
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(jwt);
        return claims.getBody().getSubject();
    }

    public Long getExpirationAmount() {
        return expirationAmount;
    }


}
