package com.store.velvetbloom.controller;

import com.store.velvetbloom.dto.UserResponseDTO;
import com.store.velvetbloom.model.Customer;
import com.store.velvetbloom.model.User;
import com.store.velvetbloom.service.AuthService;
import com.store.velvetbloom.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginWithAdminRole() {
        // Mock input
        User mockUser = new User();
        mockUser.setEmail("admin@example.com");
        mockUser.setPassword("password");

        // Mock service responses
        when(authService.login(mockUser.getEmail(), mockUser.getPassword())).thenReturn("mockToken");

        // Create admin user using setters instead of constructor
        User adminUser = new User();
        adminUser.setId("adminID");
        adminUser.setFirstName("Admin");
        adminUser.setRole("ADMIN");
        when(authService.getUserByEmail(mockUser.getEmail()))
                .thenReturn(Optional.of(adminUser));

        // Call the controller method
        ResponseEntity<?> response = authController.login(mockUser);

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("mockToken", responseBody.get("token"));
        assertEquals("http://localhost:3001/dashboard", responseBody.get("redirectUrl"));

        verify(authService, times(1)).login(mockUser.getEmail(), mockUser.getPassword());
    }

    @Test
    void testSignupAsCustomer() {
        // Mock input
        User mockUser = new User();
        mockUser.setId("userID");
        mockUser.setRole("CUSTOMER");
        mockUser.setEmail("user@example.com");

        // Mock service responses
        when(authService.signup(mockUser)).thenReturn(mockUser);

        // Call the controller method
        ResponseEntity<?> response = authController.signup(mockUser);

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        UserResponseDTO responseBody = (UserResponseDTO) response.getBody();
        assertEquals(mockUser.getEmail(), responseBody.getEmail());

        // Verify customer creation
        verify(customerService, times(1)).createCustomer(mockUser.getId());
        verify(authService, times(1)).signup(mockUser);
    }

    @Test
    void testSignupAsAdmin() {
        // Mock input
        User mockUser = new User();
        mockUser.setId("adminID");
        mockUser.setRole("ADMIN");

        // Mock service responses
        when(authService.signup(mockUser)).thenReturn(mockUser);

        // Call the controller method
        ResponseEntity<?> response = authController.createAdmin(mockUser);

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        User responseBody = (User) response.getBody();
        assertEquals("ADMIN", responseBody.getRole());

        verify(authService, times(1)).signup(mockUser);
    }

    @Test
    void testLoginInvalidUser() {
        // Mock input
        User mockUser = new User();
        mockUser.setEmail("unknown@example.com");
        mockUser.setPassword("password");

        // Mock service responses
        when(authService.login(mockUser.getEmail(), mockUser.getPassword())).thenReturn("mockToken");
        when(authService.getUserByEmail(mockUser.getEmail())).thenReturn(Optional.empty());

        // Call the controller method
        ResponseEntity<?> response = authController.login(mockUser);

        // Assertions
        assertEquals(401, response.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Invalid credentials.", responseBody.get("message"));

        verify(authService, times(1)).login(mockUser.getEmail(), mockUser.getPassword());
    }

    @Test
    void testCreateFirstAdmin() {
        // Mock input
        User mockUser = new User();
        mockUser.setId("firstAdminID");
        mockUser.setRole("ADMIN");
        mockUser.setEmail("firstadmin@example.com");

        // Mock service responses
        when(authService.signup(mockUser)).thenReturn(mockUser);

        // Call the controller method
        ResponseEntity<?> response = authController.createFirstAdmin(mockUser);

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        User responseBody = (User) response.getBody();
        assertEquals("ADMIN", responseBody.getRole());
        assertEquals(mockUser.getEmail(), responseBody.getEmail());

        verify(authService, times(1)).signup(mockUser);
    }
}
