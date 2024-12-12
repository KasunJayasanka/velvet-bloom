package com.store.velvetbloom.service;

import com.store.velvetbloom.model.Customer;
import com.store.velvetbloom.model.User;
import com.store.velvetbloom.repository.CustomerRepository;
import com.store.velvetbloom.repository.UserRepository;
import com.store.velvetbloom.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer_Success() {
        // Mock user with non-null address and secondary address
        String userId = "userId123";
        User user = new User();
        user.setId(userId);
        user.setAddress("Main St, City");  // Set address
        user.setSecondaryAddress("Secondary St, City");  // Set secondary address

        // Create a mock Customer object
        Customer customer = new Customer();
        customer.setId("customerId123");
        customer.setUser(user);
        customer.setAddresses(List.of(user.getAddress(), user.getSecondaryAddress())); // Ensure addresses are set

        // Mock behavior for the repositories
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(customerRepository.findByUser(user)).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Call the method to test
        Customer result = customerService.createCustomer(userId);

        // Assertions
        assertNotNull(result);  // Ensure the result is not null
        assertEquals("customerId123", result.getId());  // Check the customer ID
        assertEquals(userId, result.getUser().getId());  // Check the user ID
        assertEquals(List.of("Main St, City", "Secondary St, City"), result.getAddresses());  // Check if the addresses are correctly set

        // Verify that the correct methods were called
        verify(userRepository, times(1)).findById(userId);
        verify(customerRepository, times(1)).findByUser(user);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }


    @Test
    void createCustomer_UserNotFound() {
        // Mock data
        String userId = "nonExistentUser";

        // Mock behaviors
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Test and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.createCustomer(userId));
        assertEquals("User not found with ID: nonExistentUser", exception.getMessage());

        // Verify interactions
        verify(userRepository, times(1)).findById(userId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_Success() {
        // Mock data
        String customerId = "customerId123";
        Customer customer = new Customer();
        customer.setId(customerId);

        // Mock behaviors
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Test
        Customer result = customerService.getCustomerById(customerId);

        // Assertions
        assertNotNull(result);
        assertEquals(customerId, result.getId());

        // Verify interactions
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void getCustomerById_NotFound() {
        // Mock data
        String customerId = "nonExistentCustomer";

        // Mock behaviors
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Test and assert exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerService.getCustomerById(customerId));
        assertEquals("Customer not found with ID: nonExistentCustomer", exception.getMessage());

        // Verify interactions
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void updateCustomer_Success() {
        // Mock data
        String customerId = "customerId123";
        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);

        User existingUser = new User();
        existingUser.setId("userId123");
        existingUser.setFirstName("John");
        existingCustomer.setUser(existingUser);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setAddresses(List.of("New Address"));
        updatedCustomer.setOrderIds(List.of("order123"));

        User updatedUser = new User();
        updatedUser.setFirstName("Jane");
        updatedCustomer.setUser(updatedUser);

        // Mock behaviors
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        // Test
        Customer result = customerService.updateCustomer(customerId, updatedCustomer);

        // Assertions
        assertNotNull(result);
        assertEquals(List.of("New Address"), result.getAddresses());
        assertEquals("Jane", result.getUser().getFirstName());

        // Verify interactions
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    void deleteCustomer_Success() {
        // Mock data
        String customerId = "customerId123";
        Customer customer = new Customer();
        customer.setId(customerId);

        // Mock behaviors
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        // Test
        customerService.deleteCustomer(customerId);

        // Verify interactions
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void getAllCustomers_Success() {
        // Mock data
        Customer customer1 = new Customer();
        customer1.setId("customerId1");

        Customer customer2 = new Customer();
        customer2.setId("customerId2");

        // Mock behaviors
        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        // Test
        List<Customer> customers = customerService.getAllCustomers();

        // Assertions
        assertEquals(2, customers.size());
        assertEquals("customerId1", customers.get(0).getId());
        assertEquals("customerId2", customers.get(1).getId());

        // Verify interactions
        verify(customerRepository, times(1)).findAll();
    }
}
