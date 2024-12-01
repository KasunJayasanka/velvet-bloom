package com.store.velvetbloom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.velvetbloom.model.Customer;
import com.store.velvetbloom.model.User;
import com.store.velvetbloom.repository.CustomerRepository;
import com.store.velvetbloom.repository.UserRepository;
import com.store.velvetbloom.exception.ResourceNotFoundException;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public Customer createCustomer(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Check if the user already has a customer profile
        if (customerRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Customer profile already exists for user ID: " + userId);
        }

        // Create a new Customer and link the User
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setAddresses(List.of(user.getAddress(), user.getSecondaryAddress())); // Example
        return customerRepository.save(customer);
    }

    
    // Get Customer by ID
    public Customer getCustomerById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
    }

    // Update Customer
    public Customer updateCustomer(String id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        // Update referenced User details
        if (updatedCustomer.getUser() != null) {
            User updatedUser = updatedCustomer.getUser();
            User existingUser = existingCustomer.getUser();

            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setMobileNo(updatedUser.getMobileNo());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setSecondaryAddress(updatedUser.getSecondaryAddress());
            existingUser.setCity(updatedUser.getCity());
            existingUser.setCountry(updatedUser.getCountry());

            userRepository.save(existingUser);
        }

        // Update other customer details
        existingCustomer.setAddresses(updatedCustomer.getAddresses());
        existingCustomer.setOrderIds(updatedCustomer.getOrderIds());

        return customerRepository.save(existingCustomer);
    }

    // Delete Customer (Soft Delete)
    public void deleteCustomer(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        // Optional: Add a field like `isDeleted` in the Customer model and set it to true
        customerRepository.delete(customer); // Hard delete (use soft delete logic if required)
    }

    // List All Customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerByUserId(String userId) {
        return customerRepository.findByUserId(userId);
    }
}
