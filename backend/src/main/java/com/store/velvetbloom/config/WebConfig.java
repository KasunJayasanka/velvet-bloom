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
                        "https://3493-2402-4000-2180-e980-18d3-140a-3339-9377.ngrok-free.app" // Replace with your ngrok public URL
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow cookies/credentials if needed
    }
}