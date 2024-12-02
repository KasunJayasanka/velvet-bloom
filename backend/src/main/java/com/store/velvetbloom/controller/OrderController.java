package com.store.velvetbloom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.store.velvetbloom.dto.RecentOrderDTO;
import com.store.velvetbloom.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.store.velvetbloom.model.Order;
import com.store.velvetbloom.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        // Call the service method to get filtered and paginated orders
        Map<String, Object> response = orderService.getFilteredOrders(status, page, pageSize);

        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/{customerID}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Order> createOrder(@PathVariable String customerID, @RequestBody Order orderDetails) {
        return ResponseEntity.ok(orderService.createOrder(customerID, orderDetails));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/customer/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(email));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable String id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrder(
            @PathVariable String orderId,
            @RequestBody Map<String, String> updates) {
        try {
            Order updatedOrder = orderService.updateOrder(orderId, updates);
            return ResponseEntity.ok(updatedOrder);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to update order"));
        }
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<Order> cancelOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RecentOrderDTO> getRecentOrders() {
        return orderService.getRecentOrders();
    }

    @GetMapping("/total")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> getTotalSales() {
        Map<String, String> response = new HashMap<>();
        response.put("totalSales", orderService.getTotalSales());
        return response;
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Long> getOrderCounts() {
        return orderService.getOrderCounts();
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrderById(orderId);
        return ResponseEntity.ok(Map.of(
                "message", "Order with ID " + orderId + " has been deleted successfully.",
                "orderId", orderId
        ));
    }

}

