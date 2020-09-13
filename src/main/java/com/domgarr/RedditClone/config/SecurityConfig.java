package com.domgarr.RedditClone.config;

import com.domgarr.RedditClone.exception.SpringRedditException;
import com.domgarr.RedditClone.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//https://docs.spring.io/spring-security/site/docs/current/reference/html5/

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() //CORS can defend from CSRF attacks.
        /*
        .authorizeRequests(authorize ->
                authorize.antMatchers("/api/auth/**").permitAll() //Any role can access this.
                .anyRequest().denyAll() // 'This is a good strategy if you do not want to accidentally forget to update your authorization rules.'
        );
        */
         .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated();



        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /*
    PasswordEncoder is an interface, we annotate it with @Bean and
    return a class that implements PasswordEncoder. Now, it is possible to use @Autowired
    allowing Java Spring to inject the class.
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Autowired //Method-Injection AuthenticationManagerBuilder
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder){
        try {
            authenticationManagerBuilder.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new SpringRedditException("Error occurred while encoding password.");
        }
    }

    //Because AuthenticationManger is an interface, Spring wil throw a bean error if not explicitly init , since there is many implementations.
    //Thus, here anytime A bean is injected into AuthenticationManger this method is called.
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
