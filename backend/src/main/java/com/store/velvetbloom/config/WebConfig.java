package com.store.velvetbloom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Match all endpoints
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:3001",
                        "https://sandbox.payhere.lk",
                        "http://localhost:8080"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS") // Allow these methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow cookies/credentials if needed
    }
}