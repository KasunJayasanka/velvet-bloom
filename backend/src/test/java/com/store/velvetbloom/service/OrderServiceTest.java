package com.store.velvetbloom.service;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Cart;
import com.store.velvetbloom.model.Order;
import com.store.velvetbloom.model.Product;
import com.store.velvetbloom.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    private Cart mockCart;
    private Product mockProduct;
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a mock Cart
        mockCart = new Cart();
        mockCart.setCustomerID("customer1");
        Cart.CartItem cartItem = new Cart.CartItem();
        cartItem.setProductID("product1");
        cartItem.setProductName("Test Product");
        cartItem.setSize("M");
        cartItem.setPrice(100.0);
        Cart.CartItem.Color color = new Cart.CartItem.Color("Red", 2);
        cartItem.setColors(Collections.singletonList(color));
        mockCart.setProducts(Collections.singletonList(cartItem));

        // Create a mock Product
        mockProduct = new Product();
        mockProduct.setId("product1"); // Using setId instead of setProductID
        mockProduct.setProductName("Test Product");
        mockProduct.setProductCount(10);

        // Create a mock Order
        mockOrder = new Order();
        mockOrder.setId("order1");
        mockOrder.setContactMail("customer@example.com");
    }

    @Test
    void createOrder_Success() {
        when(cartService.getCartByCustomerID("customer1")).thenReturn(mockCart);
        when(productService.getProductById("product1")).thenReturn(mockProduct);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order createdOrder = orderService.createOrder("customer1", mockOrder);

        assertNotNull(createdOrder);
        assertEquals("order1", createdOrder.getId());
        verify(cartService, times(1)).clearCart("customer1");
    }

    @Test
    void createOrder_CartEmpty_ThrowsException() {
        mockCart.setProducts(Collections.emptyList());
        when(cartService.getCartByCustomerID("customer1")).thenReturn(mockCart);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.createOrder("customer1", mockOrder));
        assertEquals("Cart is empty, cannot create an order.", exception.getMessage());
    }

    @Test
    void getOrderById_Success() {
        when(orderRepository.findById("order1")).thenReturn(Optional.of(mockOrder));

        Order foundOrder = orderService.getOrderById("order1");

        assertNotNull(foundOrder);
        assertEquals("order1", foundOrder.getId());
    }

    @Test
    void getOrderById_NotFound_ThrowsException() {
        when(orderRepository.findById("order1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrderById("order1"));
        assertEquals("Order not found with ID: order1", exception.getMessage());
    }

    @Test
    void cancelOrder_Success() {
        mockOrder.setStatus("ordered");
        when(orderRepository.findById("order1")).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order cancelledOrder = orderService.cancelOrder("order1");

        assertNotNull(cancelledOrder);
        assertEquals("cancelled", cancelledOrder.getStatus());
    }

    @Test
    void cancelOrder_Shipped_ThrowsException() {
        mockOrder.setStatus("shipped");
        when(orderRepository.findById("order1")).thenReturn(Optional.of(mockOrder));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.cancelOrder("order1"));
        assertEquals("Cannot cancel a shipped order.", exception.getMessage());
    }
}
