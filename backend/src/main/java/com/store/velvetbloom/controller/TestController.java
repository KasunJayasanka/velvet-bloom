// TestController.java
package com.store.velvetbloom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.velvetbloom.repository.TestRepository;

@RestController
@Profile("test") // Only activates this controller in the 'test' profile
public class TestController {

    @Autowired
    private TestRepository testRepository;

    @GetMapping("/test-mongo-connection")
    public String testMongoConnection() {
        try {
            // Attempt to fetch documents to verify MongoDB connection
            testRepository.findAll();
            return "MongoDB connection is successful!";
        } catch (Exception e) {
            return "MongoDB connection failed: " + e.getMessage();
        }
    }
}
