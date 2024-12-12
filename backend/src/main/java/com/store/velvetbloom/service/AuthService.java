package com.store.velvetbloom.service;

import com.store.velvetbloom.model.User;
import com.store.velvetbloom.repository.UserRepository;
import com.store.velvetbloom.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SignatureException;
import java.util.Optional;

import static javax.crypto.Cipher.SECRET_KEY;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.secret}")
    private String secret;

    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }
        if (!passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userOpt.get();
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }

    public User signup(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        // Set default role if not provided
        if (user.getRole() == null) {
            user.setRole("CUSTOMER"); // Default to CUSTOMER
        }

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the database
        return userRepository.save(user);
    }

    public boolean isAdminAlreadyExists() {
        return userRepository.findByRole("ADMIN").isPresent();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Verifies the JWT token, extracts claims, and constructs a User object.
     * @param token The JWT token to verify.
     * @return User object containing extracted user information.
     * @throws RuntimeException if the token is invalid or expired.
     */
    public User verifyToken(String token) {
        try {
            // Parse and validate the token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Extract information from the claims
            String role = claims.get("role", String.class); // Custom claim for role

            // Construct a User object
            User user = new User();
            user.setRole(role);

            return user;

        } catch (Exception e) {
            throw new RuntimeException("Token validation failed", e);
        }
    }
    
}

