package com.store.velvetbloom.controller;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Order;
import com.store.velvetbloom.repository.OrderRepository;
import com.store.velvetbloom.service.OrderService;
import com.store.velvetbloom.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payhere")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;

    @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    @Value("${payhere.return.url}")
    private String returnUrl;

    @Value("${payhere.notify.url}")
    private String notifyUrl;
    
    @Value("${payhere.cancel.url}")
    private String cancelurl;

    /**
     * Handle the return URL after payment is completed.
     * This redirects users to a success confirmation page.
     */
    @GetMapping("/return")
    public ResponseEntity<String> handleReturn(@RequestParam(required = false) Map<String, String> callbackData) {
        if (callbackData == null || callbackData.isEmpty()) {
            return ResponseEntity.status(400).body("Payment process incomplete. No data received.");
        }

        // Optionally validate the payment status here (e.g., fetch status from DB).
        return ResponseEntity.ok("Payment completed successfully. Thank you!");
    }

    /**
     * Handle the cancel URL if the user cancels the payment process.
     * This redirects users back to the cart or a retry page.
     */
    @GetMapping("/cancel")
    public ResponseEntity<String> handleCancel(@RequestParam(required = false) Map<String, String> callbackData) {
        System.out.println("Payment canceled callback received: " + callbackData);

        if (callbackData != null && !callbackData.isEmpty()) {
            return ResponseEntity.status(200).body("You have canceled the payment. Please try again.");
        } else {
            return ResponseEntity.status(400).body("Payment cancellation callback is incomplete.");
        }
    }

    /**
     * Handle the notify URL for server-to-server payment notifications from PayHere.
     * This endpoint processes the payment result sent by PayHere.
     */

    @PostMapping("/notify")
    public ResponseEntity<String> handleNotification(@RequestParam Map<String, String> callbackData) {
        if (callbackData == null || callbackData.get("order_id") == null) {
            return ResponseEntity.badRequest().body("Missing or null order_id in callback data");
        }

        System.out.println("Callback data received: " + callbackData);
        paymentService.handleCallback(callbackData);
        return ResponseEntity.ok("Notification received successfully.");
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> checkout(@RequestParam String orderId, @RequestBody Map<String, Object> shippingDetails) {
        // Validate order existence
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Update shipping details
        Order.ShippingAddress shippingAddress = new Order.ShippingAddress();
        shippingAddress.setCountry((String) shippingDetails.get("country"));
        shippingAddress.setCity((String) shippingDetails.get("city"));
        shippingAddress.setPostalCode((String) shippingDetails.get("postalCode"));
        shippingAddress.setAddressOne((String) shippingDetails.get("addressOne"));
        shippingAddress.setAddressTwo((String) shippingDetails.get("addressTwo"));
//        shippingAddress.setFname((String) shippingDetails.get("fname"));
//        shippingAddress.setLname((String) shippingDetails.get("lname"));
        order.setShippingAddress(shippingAddress);

        // Save the updated order
        orderRepository.save(order);

        // Initiate payment
        String paymentUrl = paymentService.initiatePayment(order);

        // Create the response JSON
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", paymentUrl);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/redirect")
    public ResponseEntity<Map<String, String>> handlePayHereRedirect(@RequestBody Map<String, Object> requestData) {
        try {
            // Extract and validate fields with default values
            String orderId = (String) requestData.getOrDefault("order_id", "DefaultOrderId");
            String amount = String.valueOf(requestData.getOrDefault("amount", "0.00"));
            String currency = (String) requestData.getOrDefault("currency", "LKR");
            String merchantSecret = this.merchantSecret;

            System.out.println("Order ID: " + orderId);
            System.out.println("Amount: " + amount);
            System.out.println("Currency: " + currency);
            System.out.println("Merchant Secret: " + merchantSecret);

            if (orderId == null || amount == null || currency == null || merchantSecret == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Missing required parameters"));
            }

            // Extract additional fields with default values
            String contactName = (String) requestData.getOrDefault("contact_name", "John Doe");
            String email = (String) requestData.getOrDefault("email", "default@example.com");
            String phone = (String) requestData.getOrDefault("phone", "0000000000");
            String address = (String) requestData.getOrDefault("address", "Default Address");
            String city = (String) requestData.getOrDefault("city", "Default City");
            String country = (String) requestData.getOrDefault("country", "Default Country");

            // Calculate hash
            String hash = PaymentService.generatePayHereHash(
                    merchantId,
                    orderId,
                    Double.parseDouble(amount),
                    currency,
                    merchantSecret
            );

            // Construct payload
            Map<String, String> payload = new HashMap<>();
            payload.put("merchant_id", merchantId);
            payload.put("return_url", returnUrl);
            payload.put("cancel_url", cancelurl);
            payload.put("notify_url", notifyUrl);
            payload.put("order_id", orderId);
            payload.put("items", "Order Payment");
            payload.put("currency", currency);
            payload.put("amount", amount);
            payload.put("first_name", contactName.split("\\s+")[0]);
            payload.put("last_name", contactName.contains(" ") ? contactName.split("\\s+")[1] : "LastName");
            payload.put("email", email);
            payload.put("phone", phone);
            payload.put("address", address);
            payload.put("city", city);
            payload.put("country", country);
            payload.put("hash", hash);

            // Generate redirect URL
            String redirectUrl = paymentService.constructPaymentUrl(payload);

            return ResponseEntity.ok(Map.of("redirectUrl", redirectUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Error constructing payment URL", "error", e.getMessage()));
        }
    }


}
