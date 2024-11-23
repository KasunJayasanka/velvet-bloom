package com.store.velvetbloom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.velvetbloom.repository.OrderRepository;
import com.store.velvetbloom.model.Order;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomer_Id(customerId);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
}

