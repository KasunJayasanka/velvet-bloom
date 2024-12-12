package com.store.velvetbloom.service;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Cart;
import com.store.velvetbloom.model.Customer;
import com.store.velvetbloom.model.Order;
import com.store.velvetbloom.model.Product;
import com.store.velvetbloom.repository.CartRepository;
import com.store.velvetbloom.repository.CustomerRepository;
import com.store.velvetbloom.repository.OrderRepository;
import com.store.velvetbloom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CustomerRepository customerRepository;
    
    // Create Cart for Customer
    public Cart createCart(String customerID) {
        Cart cart = new Cart();
        cart.setCustomerID(customerID);
        cart.setProducts(new ArrayList<>());
        cart.setCreatedAt(LocalDateTime.now().toString());
        cart.setUpdatedAt(LocalDateTime.now().toString());
        return cartRepository.save(cart);
    }

    public Cart ensureCartExists(String customerID) {
        return cartRepository.findByCustomerID(customerID)
                .orElseGet(() -> createCart(customerID));
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


    // Checkout Cart and transition to Order
//    public Order checkoutCart(String customerID, Order orderDetails) {
//        // Step 1: Retrieve the Cart
//        Cart cart = cartRepository.findByCustomerID(customerID)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for Customer ID: " + customerID));
//
//        if (cart.getProducts() == null || cart.getProducts().isEmpty()) {
//            throw new RuntimeException("Cart is empty, cannot proceed with checkout.");
//        }
//
//        // Step 2: Transform Cart items into Order items
//        List<Order.OrderItem> orderItems = new ArrayList<>();
//        double totalAmount = 0.0;
//
//        for (Cart.CartItem cartItem : cart.getProducts()) {
//            // Fetch product details
//            Product product = productRepository.findById(cartItem.getProductID())
//                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + cartItem.getProductID()));
//
//            // Verify stock availability
//            // Verify stock availability
//            int totalRequested = (cartItem.getColors() != null)
//                    ? cartItem.getColors().stream().mapToInt(Cart.CartItem.Color::getCount).sum()
//                    : 0;
//            if (product.getProductCount() < totalRequested) {
//                throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
//            }
//
//            // Deduct stock from Product collection
//            product.setProductCount(product.getProductCount() - totalRequested);
//            productRepository.save(product);
//
//            // Calculate total amount
//            totalAmount += cartItem.getPrice() * totalRequested;
//
//            // Map CartItem to OrderItem
//            Order.OrderItem orderItem = new Order.OrderItem();
//            orderItem.setProductID(cartItem.getProductID());
//            orderItem.setProductName(cartItem.getProductName());
//            orderItem.setSize(cartItem.getSize());
//            // Map Cart.CartItem.Color to Order.OrderItem.Color
//            List<Order.OrderItem.Color> orderColors = (cartItem.getColors() != null)
//                    ? cartItem.getColors().stream()
//                    .map(cartColor -> {
//                        Order.OrderItem.Color orderColor = new Order.OrderItem.Color();
//                        orderColor.setColor(cartColor.getColor());
//                        orderColor.setCount(cartColor.getCount());
//                        return orderColor;
//                    })
//                    .collect(Collectors.toList())
//                    : new ArrayList<>();
//        }
//
//        // Step 3: Populate Order details
//        Order order = new Order();
//        order.setOrderDate(LocalDateTime.now().toString());
//        order.setUpdatedAt(LocalDateTime.now().toString());
//        order.setDeliverDate(orderDetails.getDeliverDate());
//        order.setContactName(orderDetails.getContactName());
//        order.setContactMail(orderDetails.getContactMail());
//        order.setContactNumber(orderDetails.getContactNumber());
//        order.setShippingAddress(orderDetails.getShippingAddress());
//        order.setStatus("ordered");
//        order.setPayMethod(orderDetails.getPayMethod());
//        order.setTotalAmount(totalAmount);
//        order.setOrderItems(orderItems);
//
//        // Step 4: Save Order
//        Order savedOrder = orderRepository.save(order);
//
//        // Step 5: Clear the Cart
//        clearCart(customerID);
//
//        return savedOrder;
//    }

    public String checkoutCart(String customerID, Order orderDetails) {
        // Step 1: Retrieve the Cart
        Cart cart = cartRepository.findByCustomerID(customerID)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for Customer ID: " + customerID));

        if (cart.getProducts() == null || cart.getProducts().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot proceed with checkout.");
        }

        // Step 2: Retrieve Customer Details
        Customer customer = customerRepository.findById(customerID)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerID));

        // Step 3: Transform Cart items into Order items
        List<Order.OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (Cart.CartItem cartItem : cart.getProducts()) {
            Product product = productRepository.findById(cartItem.getProductID())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + cartItem.getProductID()));

            int totalRequested = cartItem.getColors() != null
                    ? cartItem.getColors().stream().mapToInt(Cart.CartItem.Color::getCount).sum()
                    : 0;

            if (product.getProductCount() < totalRequested) {
                throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
            }

            totalAmount += cartItem.getPrice() * totalRequested;

            // Map CartItem to OrderItem
            Order.OrderItem orderItem = new Order.OrderItem();
            orderItem.setProductID(cartItem.getProductID());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setSize(cartItem.getSize());
            orderItem.setColors(cartItem.getColors().stream()
                    .map(cartColor -> new Order.OrderItem.Color(cartColor.getColor(), cartColor.getCount()))
                    .collect(Collectors.toList()));
            orderItem.setMainImgUrl(cartItem.getMainImage());

            orderItems.add(orderItem);
        }

        // Step 4: Create a preliminary order record
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now().toString());
        order.setUpdatedAt(LocalDateTime.now().toString());
        order.setDeliverDate(orderDetails.getDeliverDate());
        order.setContactName(customer.getUser().getFirstName() + " " + customer.getUser().getLastName());
        order.setContactMail(customer.getUser().getEmail());
        order.setContactNumber(customer.getUser().getMobileNo());
        order.setShippingAddress(orderDetails.getShippingAddress());
        order.setStatus("new"); // Mark as pending
        order.setPayMethod(orderDetails.getPayMethod());
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        
        System.out.println(customer.getUser().getEmail());
        Order savedOrder = orderRepository.save(order);

        // Step 5: Initiate payment via PaymentService
        return paymentService.initiatePayment(savedOrder); // Returns payment URL
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

    public Cart getCartByCustomerID(String customerID) {
        return cartRepository.findByCustomerID(customerID)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for customer ID: " + customerID));
    }

    public void clearCart(String customerID) {
        // Fetch the cart by customer ID
        Cart cart = cartRepository.findByCustomerID(customerID)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for customer with ID: " + customerID));

        // Clear the product list
        cart.setProducts(null);

        // Update the cart's updatedAt timestamp
        cart.setUpdatedAt(LocalDateTime.now().toString());

        // Save the updated cart back to the repository
        cartRepository.save(cart);
    }

}
