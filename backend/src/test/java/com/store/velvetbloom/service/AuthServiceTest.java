package com.store.velvetbloom.service;

import com.store.velvetbloom.model.User;
import com.store.velvetbloom.repository.UserRepository;
import com.store.velvetbloom.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        // Mock inputs
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "$2a$10$encodedPassword";
        String token = "mockJwtToken";

        // Mock user
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(encodedPassword);
        mockUser.setRole("CUSTOMER");

        // Mock repository and dependencies
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(email, "CUSTOMER")).thenReturn(token);

        // Execute the login method
        String resultToken = authService.login(email, password);

        // Assertions
        assertNotNull(resultToken);
        assertEquals(token, resultToken);

        // Verify repository and dependency interactions
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(jwtUtil, times(1)).generateToken(email, "CUSTOMER");
    }

    @Test
    void testLogin_UserNotFound() {
        // Mock inputs
        String email = "test@example.com";
        String password = "password123";

        // Mock repository behavior
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Execute the method and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(email, password));
        assertEquals("User not found with email: " + email, exception.getMessage());

        // Verify repository interactions
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Mock inputs
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "$2a$10$encodedPassword";

        // Mock user
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(encodedPassword);

        // Mock repository and dependencies
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        // Execute the method and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(email, password));
        assertEquals("Invalid credentials", exception.getMessage());

        // Verify repository interactions
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    void testSignup_Success() {
        // Mock inputs
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password123");

        User savedUser = new User();
        savedUser.setId("userId123");
        savedUser.setEmail("test@example.com");
        savedUser.setRole("CUSTOMER");

        // Mock repository behavior
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Execute the signup method
        User result = authService.signup(newUser);

        // Assertions
        assertNotNull(result);
        assertEquals("userId123", result.getId());
        assertEquals("CUSTOMER", result.getRole());

        // Verify repository interactions
        verify(userRepository, times(1)).findByEmail(newUser.getEmail());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void testSignup_EmailAlreadyExists() {
        // Mock inputs
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password123");

        // Mock repository behavior
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(newUser));

        // Execute the method and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.signup(newUser));
        assertEquals("Email already exists: test@example.com", exception.getMessage());

        // Verify repository interactions
        verify(userRepository, times(1)).findByEmail(newUser.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testIsAdminAlreadyExists() {
        // Mock repository behavior
        when(userRepository.findByRole("ADMIN")).thenReturn(Optional.of(new User()));

        // Execute the method
        boolean result = authService.isAdminAlreadyExists();

        // Assertions
        assertTrue(result);

        // Verify repository interactions
        verify(userRepository, times(1)).findByRole("ADMIN");
    }

    @Test
    void testGetUserByEmail_Success() {
        // Mock inputs
        String email = "test@example.com";

        // Mock user
        User mockUser = new User();
        mockUser.setEmail(email);

        // Mock repository behavior
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Execute the method
        Optional<User> result = authService.getUserByEmail(email);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());

        // Verify repository interactions
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUserByEmail_NotFound() {
        // Mock inputs
        String email = "test@example.com";

        // Mock repository behavior
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Execute the method
        Optional<User> result = authService.getUserByEmail(email);

        // Assertions
        assertTrue(result.isEmpty());

        // Verify repository interactions
        verify(userRepository, times(1)).findByEmail(email);
    }
}
