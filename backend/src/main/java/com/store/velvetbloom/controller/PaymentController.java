package com.store.velvetbloom.controller;

import com.store.velvetbloom.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payhere")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

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


}
