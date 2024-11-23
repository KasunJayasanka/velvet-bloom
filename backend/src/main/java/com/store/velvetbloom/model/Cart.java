package com.store.velvetbloom.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Cart")
public class Cart {

    @Id
    private String id;

    @DBRef
    private Customer customer;

    private List<CartItem> items;

    public static class CartItem {
        @DBRef
        private Product product;
        private int quantity;
    }

    
    // Getters and setters
}

