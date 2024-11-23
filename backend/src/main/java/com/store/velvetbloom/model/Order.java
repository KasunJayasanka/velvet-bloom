package com.store.velvetbloom.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Order")
public class Order {

    @Id
    private String id;

    @DBRef
    private Customer customer;

    private List<OrderItem> items; // List of products in the order
    private String shippingAddress;
    private String paymentMethod;
    private String status;
    private Date orderDate;

    public static class OrderItem {
        @DBRef
        private Product product;
        private int quantity;
    }

    // Getters and setters
}

