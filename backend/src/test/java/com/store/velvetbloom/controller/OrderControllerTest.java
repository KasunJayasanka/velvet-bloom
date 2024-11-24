package com.store.velvetbloom.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.store.velvetbloom.model.Order;
import com.store.velvetbloom.model.Order.OrderItem;
import com.store.velvetbloom.model.Order.ShippingAddress;
import com.store.velvetbloom.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.ArrayList;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOrders_Success() {
        // Given
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId("1");
        order.setContactMail("test@example.com");
        orders.add(order);

        when(orderService.getAllOrders()).thenReturn(orders);

        // When
        ResponseEntity<List<Order>> response = orderController.getAllOrders();

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("test@example.com", response.getBody().get(0).getContactMail());
    }

    @Test
    void createOrder_Success() {
        // Given
        String customerID = "123";
        Order orderDetails = new Order();
        orderDetails.setContactName("John Doe");
        orderDetails.setContactMail("john.doe@example.com");

        Order savedOrder = new Order();
        savedOrder.setId("1");
        savedOrder.setContactName("John Doe");

        when(orderService.createOrder(eq(customerID), any(Order.class))).thenReturn(savedOrder);

        // When
        ResponseEntity<Order> response = orderController.createOrder(customerID, orderDetails);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getContactName());
    }

    @Test
    void getOrderById_Success() {
        // Given
        String orderId = "1";
        Order order = new Order();
        order.setId(orderId);
        order.setContactName("Jane Doe");

        when(orderService.getOrderById(orderId)).thenReturn(order);

        // When
        ResponseEntity<Order> response = orderController.getOrderById(orderId);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Jane Doe", response.getBody().getContactName());
    }

    @Test
    void getOrdersByCustomer_Success() {
        // Given
        String email = "customer@example.com";
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId("1");
        order.setContactMail(email);
        orders.add(order);

        when(orderService.getOrdersByCustomer(email)).thenReturn(orders);

        // When
        ResponseEntity<List<Order>> response = orderController.getOrdersByCustomer(email);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(email, response.getBody().get(0).getContactMail());
    }

    @Test
    void updateOrderStatus_Success() {
        // Given
        String orderId = "1";
        String newStatus = "shipped";
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(newStatus);

        when(orderService.updateOrderStatus(orderId, newStatus)).thenReturn(order);

        // When
        ResponseEntity<Order> response = orderController.updateOrderStatus(orderId, newStatus);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(newStatus, response.getBody().getStatus());
    }

    @Test
    void cancelOrder_Success() {
        // Given
        String orderId = "1";
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("cancelled");

        when(orderService.cancelOrder(orderId)).thenReturn(order);

        // When
        ResponseEntity<Order> response = orderController.cancelOrder(orderId);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("cancelled", response.getBody().getStatus());
    }
}
