package com.store.velvetbloom.repository;

import com.store.velvetbloom.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {

    // Find the cart by customer ID
    Optional<Cart> findByCustomer_Id(String customerId);
}
