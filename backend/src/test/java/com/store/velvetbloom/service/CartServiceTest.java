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

    // Test: Add Product to Cart - Successful case
    @Test
    void testAddProductToCart_Success() {
        String customerID = "12345";
        String productID = "prod001";
        String size = "M";
        String color = "Red";
        int count = 2;

        Cart mockCart = new Cart();
        mockCart.setCustomerID(customerID);
        mockCart.setProducts(new ArrayList<>());

        Product mockProduct = new Product();
        mockProduct.setId(productID);
        mockProduct.setProductName("Test Product");
        mockProduct.setProductCount(10);
        mockProduct.setUnitPrice(100.0);

        when(cartRepository.findByCustomerID(customerID)).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(productID)).thenReturn(Optional.of(mockProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        Cart result = cartService.addProductToCart(customerID, productID, size, color, count);

        assertNotNull(result);
        assertEquals(1, result.getProducts().size());
        assertEquals(productID, result.getProducts().get(0).getProductID());
        assertEquals(size, result.getProducts().get(0).getSize());
        assertEquals(color, result.getProducts().get(0).getColors().get(0).getColor());
        assertEquals(count, result.getProducts().get(0).getColors().get(0).getCount());

        verify(cartRepository).findByCustomerID(customerID);
        verify(productRepository).findById(productID);
        verify(cartRepository).save(any(Cart.class));
    }

    // Test: Add Product to Cart - Cart not found
    @Test
    void testAddProductToCart_CartNotFound() {
        String customerID = "12345";
        String productID = "prod001";
        String size = "M";
        String color = "Red";
        int count = 2;

        when(cartRepository.findByCustomerID(customerID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addProductToCart(customerID, productID, size, color, count));

        assertEquals("Cart not found for Customer ID: " + customerID, exception.getMessage());
        verify(cartRepository).findByCustomerID(customerID);
        verify(productRepository, never()).findById(anyString());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    // Test: Add Product to Cart - Product not found
    @Test
    void testAddProductToCart_ProductNotFound() {
        String customerID = "12345";
        String productID = "prod001";
        String size = "M";
        String color = "Red";
        int count = 2;

        Cart mockCart = new Cart();
        mockCart.setCustomerID(customerID);
        mockCart.setProducts(new ArrayList<>());

        when(cartRepository.findByCustomerID(customerID)).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(productID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addProductToCart(customerID, productID, size, color, count));

        assertEquals("Product not found with ID: " + productID, exception.getMessage());
        verify(cartRepository).findByCustomerID(customerID);
        verify(productRepository).findById(productID);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    // Test: Add Product to Cart - Insufficient Stock
    @Test
    void testAddProductToCart_InsufficientStock() {
        String customerID = "12345";
        String productID = "prod001";
        String size = "M";
        String color = "Red";
        int count = 5;

        Cart mockCart = new Cart();
        mockCart.setCustomerID(customerID);
        mockCart.setProducts(new ArrayList<>());

        Product mockProduct = new Product();
        mockProduct.setId(productID);
        mockProduct.setProductName("Test Product");
        mockProduct.setProductCount(2);
        mockProduct.setUnitPrice(100.0);

        when(cartRepository.findByCustomerID(customerID)).thenReturn(Optional.of(mockCart));
        when(productRepository.findById(productID)).thenReturn(Optional.of(mockProduct));

        Exception exception = assertThrows(RuntimeException.class,
                () -> cartService.addProductToCart(customerID, productID, size, color, count));

        assertEquals("Insufficient stock for product: Test Product", exception.getMessage());
        verify(cartRepository).findByCustomerID(customerID);
        verify(productRepository).findById(productID);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    // Test: Remove Product from Cart
    @Test
    void testRemoveProductFromCart_Success() {
        String customerID = "12345";
        String productID = "prod001";
        String size = "M";
        String color = "Red";

        Cart.CartItem.Color colorItem = new Cart.CartItem.Color(color, 2);
        Cart.CartItem cartItem = new Cart.CartItem();
        cartItem.setProductID(productID);
        cartItem.setSize(size);
        cartItem.setColors(new ArrayList<>());
        cartItem.getColors().add(colorItem);

        Cart mockCart = new Cart();
        mockCart.setCustomerID(customerID);
        mockCart.setProducts(new ArrayList<>());
        mockCart.getProducts().add(cartItem);

        when(cartRepository.findByCustomerID(customerID)).thenReturn(Optional.of(mockCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        Cart result = cartService.removeProductFromCart(customerID, productID, size, color);

        assertNotNull(result);
        assertTrue(result.getProducts().isEmpty());

        verify(cartRepository).findByCustomerID(customerID);
        verify(cartRepository).save(any(Cart.class));
    }
}
