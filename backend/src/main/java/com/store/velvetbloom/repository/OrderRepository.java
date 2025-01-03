package com.store.velvetbloom.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.store.velvetbloom.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByContactMail(String contactMail);
    List<Order> findAllByOrderByOrderDateDesc();
    Page<Order> findByStatus(String status, Pageable pageable);
}

