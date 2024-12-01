package com.store.velvetbloom.controller;

import com.store.velvetbloom.dto.UserResponseDTO;
import com.store.velvetbloom.model.Customer;
import com.store.velvetbloom.model.User;
import com.store.velvetbloom.service.AuthService;
import com.store.velvetbloom.service.CustomerService;


import java.io.IOException;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @Autowired
    private CustomerService customerService;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User user) {
//        String token = authService.login(user.getEmail(), user.getPassword());
//
//        Map<String, String> response = new HashMap<>();
//        response.put("token", token);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Authenticate the user and generate a token
        String token = authService.login(user.getEmail(), user.getPassword());

        // Fetch user details to determine the redirect URL and customer ID
        Optional<User> loggedInUser = authService.getUserByEmail(user.getEmail());
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials."));
        }

        User userDetails = loggedInUser.get();
        String redirectUrl;
        String customerId = null;

        if ("ADMIN".equals(userDetails.getRole())) {
            redirectUrl = "http://localhost:3001/"; // Admin frontend
        } else if ("CUSTOMER".equals(userDetails.getRole())) {
            redirectUrl = "http://localhost:3000/"; // User frontend

            // Fetch the customer ID for the logged-in user
            Optional<Customer> customer = customerService.getCustomerByUserId(userDetails.getId());
            if (customer.isPresent()) {
                customerId = customer.get().getId();
            }
        } else {
            redirectUrl = "http://localhost:3000/"; // Default frontend
        }

        // Return token, redirect URL, and customer ID (if applicable) as part of the JSON response
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("redirectUrl", redirectUrl);
        if (customerId != null) {
            response.put("customerId", customerId);
        }

        return ResponseEntity.ok(response);
    }






    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        User newUser = authService.signup(user);

        if ("CUSTOMER".equalsIgnoreCase(newUser.getRole())) {
            customerService.createCustomer(newUser.getId());
        }
        
        UserResponseDTO responseDTO = new UserResponseDTO(
            newUser.getId(),
            newUser.getFirstName(),
            newUser.getLastName(),
            newUser.getEmail(),
            newUser.getMobileNo(),
            newUser.getRole(),
            newUser.getCountry(),
            newUser.getCity(),
            newUser.getAddress(),
            newUser.getSecondaryAddress()
    );

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('ADMIN')") // Restrict access to users with the ADMIN role
    public ResponseEntity<?> createAdmin(@RequestBody User user) {
        // Ensure the role is explicitly set to ADMIN
        user.setRole("ADMIN");
        User newAdmin = authService.signup(user);
        return ResponseEntity.ok(newAdmin);
    }

    @PostMapping("/create-first-admin")
    public ResponseEntity<?> createFirstAdmin(@RequestBody User user) {
        
        // Set role as ADMIN and create the user
        user.setRole("ADMIN");
        User admin = authService.signup(user);
        return ResponseEntity.ok(admin);
    }
    
}
