package com.store.velvetbloom.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
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
}


