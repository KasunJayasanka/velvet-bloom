// package com.store.velvetbloom.config;

// import com.store.velvetbloom.middleware.AuthenticationFilter;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// public class SecurityConfig {

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     @Profile("test") // Test profile configuration
//     public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .authorizeHttpRequests(authz -> authz
//                         .requestMatchers("/images/upload", "/test-mongo-connection").permitAll() // Allow specific endpoints for testing
//                         .anyRequest().authenticated()
//                 )
//                 .csrf(csrf -> csrf.disable()); // Disable CSRF for easier testing
//         return http.build();
//     }

//     @Bean
//     @Profile("default") // Default production configuration
//     public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(csrf -> csrf.disable()) // Disable CSRF to simplify API integration; ensure token-based protection
//                 .authorizeHttpRequests(authz -> authz
//                         .requestMatchers("/images/upload", "/test-mongo-connection").permitAll() // Allow specific endpoints
//                         .requestMatchers("/auth/**").permitAll() // Allow unauthenticated access to authentication endpoints
//                         .requestMatchers("/admin/**").hasRole("ADMIN") // Restrict /admin endpoints to ADMIN role
//                         .requestMatchers("/user/**").hasRole("USER") // Restrict /user endpoints to USER role
//                         .anyRequest().authenticated() // Require authentication for all other endpoints
//                 )
//                 .addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Add custom authentication filter

//         return http.build();
//     }
// }


package com.store.velvetbloom.config;

import com.store.velvetbloom.middleware.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationFilter authenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/images/upload", "/test-mongo-connection").permitAll() // Always allow these
                        .requestMatchers("/auth/login", "/auth/signup", "/auth/create-first-admin").permitAll() // Public endpoints
                        .requestMatchers("/auth/create-admin").hasRole("ADMIN") // Restricted to ADMIN role
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Restrict /admin endpoints to ADMIN role
                        .requestMatchers("/products/**").permitAll()
                        .requestMatchers("/categories/**").permitAll()
                        .requestMatchers("/customers/**").permitAll()
                        .requestMatchers("/carts/**").permitAll()
                        .requestMatchers("/user/**").hasRole("USER") // Restrict /user endpoints to USER role
                        .anyRequest().authenticated() // Require authentication for all other endpoints
                )
                        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        ; // Add custom authentication filter

        return http.build();
    }
}
