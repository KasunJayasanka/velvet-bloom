package com.store.velvetbloom.service;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Cart;
import com.store.velvetbloom.model.Product;
import com.store.velvetbloom.repository.CartRepository;
import com.store.velvetbloom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Create Cart for Customer
    public Cart createCart(String customerID) {
        Cart cart = new Cart();
        cart.setCustomerID(customerID);
        cart.setProducts(new ArrayList<>());
        cart.setCreatedAt(LocalDateTime.now().toString());
        cart.setUpdatedAt(LocalDateTime.now().toString());
        return cartRepository.save(cart);
    }

    // Add Product to Cart
    public Cart addProductToCart(String customerID, String productID, String size, String color, int count) {
        Cart cart = cartRepository.findByCustomerID(customerID)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for Customer ID: " + customerID));

        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productID));

        if (product.getProductCount() < count) {
            throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
        }

        // Update Product in Cart
        Cart.CartItem productItem = cart.getProducts().stream()
                .filter(item -> item.getProductID().equals(productID) && item.getSize().equals(size))
                .findFirst()
                .orElse(null);

        if (productItem == null) {
            // Add new product to cart
            Cart.CartItem newItem = new Cart.CartItem();
            newItem.setProductID(productID);
            newItem.setProductName(product.getProductName());
            newItem.setSize(size);
            newItem.setMainImage(product.getMainImgUrl());
            newItem.setPrice(product.getUnitPrice());
            newItem.setColors(new ArrayList<>());
            newItem.getColors().add(new Cart.CartItem.Color(color, count));
            cart.getProducts().add(newItem);
        } else {
            // Update existing product in cart
            Cart.CartItem.Color colorItem = productItem.getColors().stream()
                    .filter(c -> c.getColor().equals(color))
                    .findFirst()
                    .orElse(null);

            if (colorItem == null) {
                productItem.getColors().add(new Cart.CartItem.Color(color, count));
            } else {
                colorItem.setCount(colorItem.getCount() + count);
            }
        }

        // Update timestamps and save
        cart.setUpdatedAt(LocalDateTime.now().toString());
        return cartRepository.save(cart);
    }

    // Remove Product from Cart
    public Cart removeProductFromCart(String customerID, String productID, String size, String color) {
        Cart cart = cartRepository.findByCustomerID(customerID)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for Customer ID: " + customerID));

        cart.getProducts().removeIf(productItem ->
                productItem.getProductID().equals(productID) &&
                        productItem.getSize().equals(size) &&
                        productItem.getColors().removeIf(c -> c.getColor().equals(color)));

        cart.setUpdatedAt(LocalDateTime.now().toString());
        return cartRepository.save(cart);
    }

    // View Cart by Customer
    public Cart viewCart(String customerID) {
        return cartRepository.findByCustomerID(customerID)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for Customer ID: " + customerID));
    }

    public Cart updateCart(String cartId, String productId, String size, String color, int count) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));

        List<Cart.CartItem> items = cart.getProducts();
        boolean itemUpdated = false;

        for (Cart.CartItem item : items) {
            if (item.getProductID().equals(productId) && item.getSize().equals(size) && item.getColors().stream().anyMatch(c -> c.getColor().equals(color))) {
                // Update the count for the matching product, size, and color
                for (Cart.CartItem.Color c : item.getColors()) {
                    if (c.getColor().equals(color)) {
                        c.setCount(count);
                        itemUpdated = true;
                        break;
                    }
                }
            }
        }

        // If no matching item was found, throw an exception or handle as needed
        if (!itemUpdated) {
            throw new RuntimeException("Product not found in the cart with the specified size and color.");
        }

        cart.setUpdatedAt(LocalDateTime.now().toString());
        return cartRepository.save(cart);
    }


    // Checkout Cart
    public void checkoutCart(String customerID) {
        Cart cart = cartRepository.findByCustomerID(customerID)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for Customer ID: " + customerID));

        // Logic to transition Cart to Order
        // ...
    }

    public Cart getCartById(String cartId) {
        // Explicitly check the ID format
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));
    }

    public void updateCartProduct(Cart cart, String productId, Cart.CartItem updatedItem) {
        // Check if the product exists in the cart
        List<Cart.CartItem> products = cart.getProducts();
        if (products == null) {
            products = new ArrayList<>();
        }

        boolean productFound = false;

        // Iterate through the products in the cart
        for (Cart.CartItem item : products) {
            if (item.getProductID().equals(productId)) {
                // Update the existing product's details
                item.setSize(updatedItem.getSize());
                item.setColors(updatedItem.getColors());
                item.setPrice(updatedItem.getPrice());
                productFound = true;
                break;
            }
        }

        // If the product is not found, add it to the cart
        if (!productFound) {
            updatedItem.setProductID(productId);
            products.add(updatedItem);
        }

        // Update the cart's products and timestamp
        cart.setProducts(products);
        cart.setUpdatedAt(LocalDateTime.now().toString());

        // Save the updated cart to the database
        cartRepository.save(cart);
    }

}
