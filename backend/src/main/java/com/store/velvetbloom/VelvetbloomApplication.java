package com.store.velvetbloom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize
@EnableMongoRepositories(basePackages = "com.store.velvetbloom.repository")
public class VelvetbloomApplication {

	public static void main(String[] args) {
		SpringApplication.run(VelvetbloomApplication.class, args);
	}

}
