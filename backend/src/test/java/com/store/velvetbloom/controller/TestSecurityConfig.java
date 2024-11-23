package com.store.velvetbloom.controller;

import com.store.velvetbloom.config.JwtConfig;
import com.store.velvetbloom.middleware.AuthenticationFilter;
import com.store.velvetbloom.util.JwtUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    @Primary
    public JwtConfig jwtConfig() {
        // Mocking JwtConfig for testing purposes
        System.setProperty("jwt.secret", "test-secret-key-that-is-long-enough-for-testing-purposes-123456789");
        System.setProperty("jwt.expiration", "3600");
        return new JwtConfig();
    }

    @Bean
    @Primary
    public JwtUtil jwtUtil() {
        return new JwtUtil(); // Using no-args constructor
    }

    @Bean
    @Primary
    public AuthenticationFilter authenticationFilter(JwtUtil jwtUtil) {
        return new AuthenticationFilter(jwtUtil);
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationFilter authenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/images/upload", "/test-mongo-connection").permitAll()
                        .requestMatchers("/auth/login", "/auth/signup", "/auth/create-first-admin").permitAll()
                        .requestMatchers("/auth/create-admin").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/products/**").permitAll()
                        .requestMatchers("/categories/**").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}