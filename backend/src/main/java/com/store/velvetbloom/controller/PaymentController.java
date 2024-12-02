package com.store.velvetbloom.controller;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Order;
import com.store.velvetbloom.repository.OrderRepository;
import com.store.velvetbloom.service.OrderService;
import com.store.velvetbloom.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> handlePayHereRedirect(@RequestBody Map<String, Object> data) {
        try {
            // Prepare the PayHere request
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
            String payHereUrl = "https://sandbox.payhere.lk/pay/checkout";

            ResponseEntity<String> response = restTemplate.postForEntity(payHereUrl, request, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error forwarding request to PayHere");
        }
    }

}
