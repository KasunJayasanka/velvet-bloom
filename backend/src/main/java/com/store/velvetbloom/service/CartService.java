package com.store.velvetbloom.service;

import com.store.velvetbloom.model.Cart;
import com.store.velvetbloom.model.Product;
import com.store.velvetbloom.repository.CartRepository;
import com.store.velvetbloom.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Cart getCartByCustomer(String customerId) {
        // Use Optional's orElse or orElseThrow to handle the case where the cart doesn't exist
        return cartRepository.findByCustomer_Id(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found for customer ID: " + customerId));
    }

    public Cart updateCart(Cart cart) {
        return cartRepository.save(cart);
    }
}


