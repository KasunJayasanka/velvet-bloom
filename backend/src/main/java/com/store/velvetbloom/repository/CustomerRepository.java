package com.store.velvetbloom.repository;

import com.store.velvetbloom.model.Customer;
import com.store.velvetbloom.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByUser(User user);
}
