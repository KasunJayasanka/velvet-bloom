package com.store.velvetbloom.service;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Cart;
import com.store.velvetbloom.model.Product;
import com.store.velvetbloom.repository.CartRepository;
import com.store.velvetbloom.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductToCart_Success() {
        // Mock inputs
        String customerID = "12345";
        String productID = "prod001";
        String size = "M";
        String color = "Red";
        int count = 2;

        // Mock cart
        Cart mockCart = new Cart();
        mockCart.setCustomerID(customerID);
        mockCart.setProducts(new ArrayList<>()); // Ensure products list is initialized

        // Mock product
        Product mockProduct = new Product();
        mockProduct.setId(productID);
        mockProduct.setProductName("Test Product");
        mockProduct.setProductCount(10);
        mockProduct.setUnitPrice(100.0);

        // Mock repository behavior
        when(cartRepository.findByCustomerID(customerID)).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(productID)).thenReturn(Optional.of(mockProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        // Execute the method
        Cart result = cartService.addProductToCart(customerID, productID, size, color, count);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getProducts().size());
        assertEquals(productID, result.getProducts().get(0).getProductID());
        assertEquals(size, result.getProducts().get(0).getSize());
        assertEquals(color, result.getProducts().get(0).getColors().get(0).getColor());
        assertEquals(count, result.getProducts().get(0).getColors().get(0).getCount());

        // Verify repository calls
        verify(cartRepository, times(1)).findByCustomerID(customerID);
        verify(productRepository, times(1)).findById(productID);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddProductToCart_CartNotFound() {
        // Mock inputs
        String customerID = "12345";
        String productID = "prod001";
        String size = "M";
        String color = "Red";
        int count = 2;

        // Mock repository behavior
        when(cartRepository.findByCustomerID(customerID)).thenReturn(Optional.empty());

        // Execute the method and assert exception
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addProductToCart(customerID, productID, size, color, count));

        assertEquals("Cart not found for Customer ID: " + customerID, exception.getMessage());

        // Verify repository calls
        verify(cartRepository, times(1)).findByCustomerID(customerID);
        verify(productRepository, never()).findById(anyString());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testAddProductToCart_ProductNotFound() {
        // Mock inputs
        String customerID = "12345";
        String productID = "prod001";
        String size = "M";
        String color = "Red";
        int count = 2;

        // Mock cart
        Cart mockCart = new Cart();
        mockCart.setCustomerID(customerID);
        mockCart.setProducts(new ArrayList<>()); // Ensure products list is initialized

        // Mock repository behavior
        when(cartRepository.findByCustomerID(customerID)).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(productID)).thenReturn(Optional.empty());

        // Execute the method and assert exception
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addProductToCart(customerID, productID, size, color, count));

        assertEquals("Product not found with ID: " + productID, exception.getMessage());

        // Verify repository calls
        verify(cartRepository, times(1)).findByCustomerID(customerID);
        verify(productRepository, times(1)).findById(productID);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testAddProductToCart_InsufficientStock() {
        // Mock inputs
        String customerID = "12345";
        String productID = "prod001";
        String size = "M";
        String color = "Red";
        int count = 5;

        // Mock cart
        Cart mockCart = new Cart();
        mockCart.setCustomerID(customerID);
        mockCart.setProducts(new ArrayList<>()); // Ensure products list is initialized

        // Mock product
        Product mockProduct = new Product();
        mockProduct.setId(productID);
        mockProduct.setProductName("Test Product");
        mockProduct.setProductCount(2); // Insufficient stock
        mockProduct.setUnitPrice(100.0);

        // Mock repository behavior
        when(cartRepository.findByCustomerID(customerID)).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(productID)).thenReturn(Optional.of(mockProduct));

        // Execute the method and assert exception
        Exception exception = assertThrows(RuntimeException.class,
                () -> cartService.addProductToCart(customerID, productID, size, color, count));

        assertEquals("Insufficient stock for product: Test Product", exception.getMessage());

        // Verify repository calls
        verify(cartRepository, times(1)).findByCustomerID(customerID);
        verify(productRepository, times(1)).findById(productID);
        verify(cartRepository, never()).save(any(Cart.class));
    }
}
