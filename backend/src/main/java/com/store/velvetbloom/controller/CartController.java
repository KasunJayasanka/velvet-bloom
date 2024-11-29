package com.store.velvetbloom.controller;

import com.store.velvetbloom.model.Cart;
import com.store.velvetbloom.model.Order;
import com.store.velvetbloom.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{customerID}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<Cart> createCart(@PathVariable String customerID) {
        return ResponseEntity.ok(cartService.createCart(customerID));
    }

    @PostMapping("/{customerID}/products/{productID}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable String customerID,
            @PathVariable String productID,
            @RequestParam String size,
            @RequestParam String color,
            @RequestParam int count) {
        return ResponseEntity.ok(cartService.addProductToCart(customerID, productID, size, color, count));
    }

    @DeleteMapping("/{customerID}/products/{productID}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<Cart> removeProductFromCart(
            @PathVariable String customerID,
            @PathVariable String productID,
            @RequestParam String size,
            @RequestParam String color) {
        return ResponseEntity.ok(cartService.removeProductFromCart(customerID, productID, size, color));
    }

    @GetMapping("/{customerID}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<Cart> viewCart(@PathVariable String customerID) {
        return ResponseEntity.ok(cartService.viewCart(customerID));
    }

    @PostMapping("/{customerID}/checkout")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<?> checkoutCart(@PathVariable String customerID, @RequestBody Order orderDetails) {
        String paymentUrl = cartService.checkoutCart(customerID, orderDetails);
        return ResponseEntity.ok(Map.of(
                "message", "Checkout successful, redirect to payment gateway",
                "paymentUrl", paymentUrl
        ));
    }
    
    @PatchMapping("/{cartId}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<?> updateCartProduct(
            @PathVariable String cartId,
            @PathVariable String productId,
            @RequestBody Cart.CartItem updatedItem) {

        Cart cart = cartService.getCartById(cartId); // Ensure this line fetches the correct cart
        cartService.updateCartProduct(cart, productId, updatedItem);

        return ResponseEntity.ok(Map.of("message", "Product updated in cart", "cartId", cartId, "productId", productId));
    }
}
