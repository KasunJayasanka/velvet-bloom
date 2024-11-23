package com.store.velvetbloom.repository;

import com.store.velvetbloom.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {

    // Find a cart by the associated customer ID
    Optional<Cart> findByCustomerID(String customerID);

    // Check if a cart exists for a specific customer
    boolean existsByCustomerID(String customerID);

    // Delete a cart by customer ID
    void deleteByCustomerID(String customerID);

    
}
