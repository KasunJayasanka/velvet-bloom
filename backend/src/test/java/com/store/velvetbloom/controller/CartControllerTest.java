package com.store.velvetbloom.controller;

import com.store.velvetbloom.model.Cart;
import com.store.velvetbloom.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCart() {
        String customerID = "customer123";
        Cart mockCart = new Cart();
        mockCart.setCustomerID(customerID);

        when(cartService.createCart(customerID)).thenReturn(mockCart);

        ResponseEntity<Cart> response = cartController.createCart(customerID);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customerID, response.getBody().getCustomerID());
        verify(cartService, times(1)).createCart(customerID);
    }

    @Test
    void testAddProductToCart() {
        String customerID = "customer123";
        String productID = "product456";
        String size = "M";
        String color = "Red";
        int count = 2;

        Cart mockCart = new Cart();
        when(cartService.addProductToCart(customerID, productID, size, color, count)).thenReturn(mockCart);

        ResponseEntity<Cart> response = cartController.addProductToCart(customerID, productID, size, color, count);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCart, response.getBody());
        verify(cartService, times(1)).addProductToCart(customerID, productID, size, color, count);
    }

    @Test
    void testRemoveProductFromCart() {
        String customerID = "customer123";
        String productID = "product456";
        String size = "M";
        String color = "Red";

        Cart mockCart = new Cart();
        when(cartService.removeProductFromCart(customerID, productID, size, color)).thenReturn(mockCart);

        ResponseEntity<Cart> response = cartController.removeProductFromCart(customerID, productID, size, color);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCart, response.getBody());
        verify(cartService, times(1)).removeProductFromCart(customerID, productID, size, color);
    }

    @Test
    void testViewCart() {
        String customerID = "customer123";

        Cart mockCart = new Cart();
        mockCart.setCustomerID(customerID);

        when(cartService.viewCart(customerID)).thenReturn(mockCart);

        ResponseEntity<Cart> response = cartController.viewCart(customerID);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCart, response.getBody());
        verify(cartService, times(1)).viewCart(customerID);
    }

    @Test
    void testCheckoutCart() {
        String customerID = "customer123";

        doNothing().when(cartService).checkoutCart(customerID);

        ResponseEntity<?> response = cartController.checkoutCart(customerID);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Checkout successful", response.getBody());
        verify(cartService, times(1)).checkoutCart(customerID);
    }

    @Test
    void testUpdateCartProduct() {
        String cartId = "cart789";
        String productId = "product456";

        Cart.CartItem updatedItem = new Cart.CartItem();
        updatedItem.setProductID(productId);
        updatedItem.setProductName("Product Name");

        Cart mockCart = new Cart();
        mockCart.setId(cartId);

        when(cartService.getCartById(cartId)).thenReturn(mockCart);
        doNothing().when(cartService).updateCartProduct(mockCart, productId, updatedItem);

        ResponseEntity<?> response = cartController.updateCartProduct(cartId, productId, updatedItem);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Product updated in cart", ((Map<?, ?>) response.getBody()).get("message"));
        verify(cartService, times(1)).getCartById(cartId);
        verify(cartService, times(1)).updateCartProduct(mockCart, productId, updatedItem);
    }
}
