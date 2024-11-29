package com.store.velvetbloom.service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.store.velvetbloom.dto.RecentOrderDTO;
import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.store.velvetbloom.repository.OrderRepository;
import com.store.velvetbloom.model.Order;
import com.store.velvetbloom.model.Product;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    public Order createOrder(String customerID, Order orderDetails) {
        // Retrieve cart for the customer
        Cart cart = cartService.getCartByCustomerID(customerID);
        if (cart.getProducts().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot create an order.");
        }

        // Calculate total amount and create order items
        double totalAmount = 0.0;
        List<Order.OrderItem> orderItems = new ArrayList<>();
        for (Cart.CartItem cartItem : cart.getProducts()) {
            Product product = productService.getProductById(cartItem.getProductID());

            // Deduct stock
            product.setProductCount(product.getProductCount() - cartItem.getColors().stream()
                    .mapToInt(Cart.CartItem.Color::getCount).sum());
            productService.updateProductStock(product);

            totalAmount += cartItem.getPrice() * cartItem.getColors().stream()
                    .mapToInt(Cart.CartItem.Color::getCount).sum();

            // Map colors from CartItem to OrderItem
            List<Order.OrderItem.Color> orderColors = cartItem.getColors().stream()
                    .map(color -> new Order.OrderItem.Color(color.getColor(), color.getCount()))
                    .toList();

            // Create OrderItem
            Order.OrderItem orderItem = new Order.OrderItem();
            orderItem.setProductID(cartItem.getProductID());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setSize(cartItem.getSize());
            orderItem.setColors(orderColors);
            orderItems.add(orderItem);
        }

        // Populate Order
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now().toString());
        order.setUpdatedAt(LocalDateTime.now().toString());
        order.setDeliverDate(orderDetails.getDeliverDate()); // Optional
        order.setContactName(orderDetails.getContactName());
        order.setContactMail(orderDetails.getContactMail());
        order.setContactNumber(orderDetails.getContactNumber());
        order.setShippingAddress(orderDetails.getShippingAddress());
        order.setStatus("ordered");
        order.setPayMethod(orderDetails.getPayMethod());
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        // Save Order and clear cart
        cartService.clearCart(customerID);
        return orderRepository.save(order);
    }


    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }

    public List<Order> getOrdersByCustomer(String customerEmail) {
        return orderRepository.findByContactMail(customerEmail);
    }

    public Order updateOrderStatus(String orderId, String newStatus) {
        Order order = getOrderById(orderId);
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now().toString());
        return orderRepository.save(order);
    }

    public Order cancelOrder(String orderId) {
        Order order = getOrderById(orderId);
        if ("shipped".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Cannot cancel a shipped order.");
        }
        order.setStatus("cancelled");
        order.setUpdatedAt(LocalDateTime.now().toString());
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<RecentOrderDTO> getRecentOrders() {
        // Fetch all orders sorted by date (most recent first)
        List<Order> orders = orderRepository.findAllByOrderByOrderDateDesc();

        // Format the orders for the response
        return orders.stream()
                .map(order -> {
                    String totalFormatted = NumberFormat.getCurrencyInstance(Locale.US).format(order.getTotalAmount());
                    return new RecentOrderDTO(
                            order.getId(),
                            order.getContactName(),
                            order.getOrderDate(),
                            order.getStatus(),
                            totalFormatted
                    );
                })
                .collect(Collectors.toList());
    }

    public String getTotalSales() {
        // Sum up the total amount of all completed orders
        double totalSales = orderRepository.findAll().stream()
                .filter(order -> "Completed".equalsIgnoreCase(order.getStatus()))
                .mapToDouble(order -> order.getTotalAmount())
                .sum();

        // Format the total sales amount in currency format
        return NumberFormat.getCurrencyInstance(Locale.US).format(totalSales);
    }

    public Map<String, Long> getOrderCounts() {
        // Get the counts for "New" and "Pending" orders
        Map<String, Long> orderCounts = orderRepository.findAll().stream()
                .filter(order -> "new".equalsIgnoreCase(order.getStatus()) || "Pending".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.groupingBy(order -> order.getStatus().toLowerCase(), Collectors.counting()));

        // Return counts for "newOrders" and "pendingOrders"
        return Map.of(
                "newOrders", orderCounts.getOrDefault("new", 0L),
                "pendingOrders", orderCounts.getOrDefault("pending", 0L)
        );
    }

    public Map<String, Object> getFilteredOrders(String status, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Order> orderPage;
        if (status != null && !status.isEmpty()) {
            // Filter by status
            orderPage = orderRepository.findByStatus(status, pageable);
        } else {
            // No filter applied
            orderPage = orderRepository.findAll(pageable);
        }

        // Build the response
        Map<String, Object> response = new HashMap<>();
        response.put("orders", orderPage.getContent());
        response.put("currentPage", orderPage.getNumber());
        response.put("totalItems", orderPage.getTotalElements());
        response.put("totalPages", orderPage.getTotalPages());

        return response;
    }

    public Order updateOrder(String orderId, Map<String, String> updates) {
        // Fetch the order by ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Update fields if provided
        if (updates.containsKey("status")) {
            order.setStatus(updates.get("status"));
        }

        if (updates.containsKey("createdAt")) {
            order.setCreatedAt(updates.get("createdAt"));
        }

        // Save the updated order
        return orderRepository.save(order);
    }

    public void deleteOrderById(String orderId) {
        // Check if the order exists
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order with ID " + orderId + " not found.");
        }
        // Delete the order
        orderRepository.deleteById(orderId);
    }
}


