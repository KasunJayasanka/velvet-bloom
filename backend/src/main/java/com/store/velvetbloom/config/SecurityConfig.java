package com.store.velvetbloom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @Profile("test")  // Only applies in the 'test' profile
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/images/upload","/test-mongo-connection").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf().disable();  // Disable CSRF for testing

        return http.build();
    }

    @Bean
    @Profile("!test")  // Applies in all profiles except 'test'
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()
                )
                .csrf().disable();

        return http.build();
    }
}
