package com.store.velvetbloom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.velvetbloom.model.Customer;
import com.store.velvetbloom.model.User;
import com.store.velvetbloom.repository.CustomerRepository;
import com.store.velvetbloom.repository.UserRepository;

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
}
