package com.store.velvetbloom.controller;

import com.store.velvetbloom.model.Customer;
import com.store.velvetbloom.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCustomerById() {
        // Arrange
        Customer customer = new Customer();
        customer.setId("1");
        customer.setAddresses(Arrays.asList("Address1", "Address2"));

        when(customerService.getCustomerById("1")).thenReturn(customer);

        // Act
        ResponseEntity<Customer> response = customerController.getCustomerById("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customer, response.getBody());
        verify(customerService, times(1)).getCustomerById("1");
    }

    @Test
    void testUpdateCustomer() {
        // Arrange
        Customer customer = new Customer();
        customer.setAddresses(Arrays.asList("Updated Address"));
        customer.setId("1");

        when(customerService.updateCustomer(eq("1"), any(Customer.class))).thenReturn(customer);

        // Act
        ResponseEntity<Customer> response = customerController.updateCustomer("1", customer);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customer, response.getBody());
        verify(customerService, times(1)).updateCustomer(eq("1"), any(Customer.class));
    }

    @Test
    void testDeleteCustomer() {
        // Arrange
        String customerId = "1";

        doNothing().when(customerService).deleteCustomer(customerId);

        // Act
        ResponseEntity<Map<String, String>> response = customerController.deleteCustomer(customerId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Map.of("message", "Customer deleted successfully", "customerId", customerId), response.getBody());
        verify(customerService, times(1)).deleteCustomer(customerId);
    }

    @Test
    void testGetAllCustomers() {
        // Arrange
        Customer customer1 = new Customer();
        customer1.setId("1");

        Customer customer2 = new Customer();
        customer2.setId("2");

        List<Customer> customers = Arrays.asList(customer1, customer2);

        when(customerService.getAllCustomers()).thenReturn(customers);

        // Act
        ResponseEntity<List<Customer>> response = customerController.getAllCustomers();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customers, response.getBody());
        verify(customerService, times(1)).getAllCustomers();
    }
}
