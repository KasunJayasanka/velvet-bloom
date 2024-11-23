package com.store.velvetbloom.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.store.velvetbloom.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByCustomer_Id(String customerId); // Find orders by customer ID
}

