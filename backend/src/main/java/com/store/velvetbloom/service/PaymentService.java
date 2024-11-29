package com.store.velvetbloom.service;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.store.velvetbloom.model.Order;
import com.store.velvetbloom.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.return.url}")
    private String returnUrl;

    @Value("${payhere.cancel.url}")
    private String cancelUrl;

    @Value("${payhere.notify.url}")
    private String notifyUrl;

    @Value("${payhere.payment.url}")
    private String payherePaymentUrl;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    public String initiatePayment(Order order) {
        // Prepare the payload

        Map<String, String> names = extractFirstAndLastName(order.getContactName());
        String firstName = names.get("firstName");
        String lastName = names.get("lastName");
        
        Map<String, String> payload = new HashMap<>();
        payload.put("merchant_id", merchantId);
        payload.put("return_url", returnUrl);
        payload.put("cancel_url", cancelUrl);
        payload.put("notify_url", notifyUrl);
        payload.put("order_id", order.getId());
        payload.put("items", "Order Payment");
        payload.put("currency", "LKR");
        payload.put("amount", String.format("%.2f", order.getTotalAmount()));
        payload.put("first_name", firstName);
        payload.put("last_name", lastName);
        payload.put("email", order.getContactMail());
        payload.put("phone", order.getContactNumber());
        payload.put("address", order.getShippingAddress().getAddressOne());
        payload.put("city", order.getShippingAddress().getCity());
        payload.put("country", order.getShippingAddress().getCountry());

        // Generate the hash using the reusable function
        String hash = generatePayHereHash(
                merchantId,
                order.getId(),
                order.getTotalAmount(),
                "LKR",
                merchantSecret
        );
        payload.put("hash", hash);

        // Construct the payment URL
        StringBuilder paymentUrl = new StringBuilder(payherePaymentUrl).append("?");
        payload.forEach((key, value) -> paymentUrl.append(key).append("=").append(java.net.URLEncoder.encode(value, StandardCharsets.UTF_8)).append("&"));
        return paymentUrl.substring(0, paymentUrl.length() - 1);
    }

    public void handleCallback(Map<String, String> callbackData) {
        String orderId = callbackData.get("order_id");
        String statusCode = callbackData.get("status_code");
        String paymentReference = callbackData.get("payment_id");

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        // Verify the payment using the md5sig
        String receivedMd5sig = callbackData.get("md5sig");
        String expectedMd5sig = Hashing.md5()
                .hashString(merchantId + orderId + callbackData.get("payhere_amount") + callbackData.get("payhere_currency") + statusCode +
                                Hashing.md5().hashString(merchantSecret, StandardCharsets.UTF_8).toString().toUpperCase(),
                        StandardCharsets.UTF_8)
                .toString()
                .toUpperCase();

        if (!expectedMd5sig.equals(receivedMd5sig)) {
            throw new RuntimeException("Invalid payment notification received");
        }

        // Update order status based on status_code
        if ("2".equals(statusCode)) {
            order.setStatus("confirmed");
            order.setPaymentStatus("Paid");
            order.setPaymentReference(paymentReference);
        } else {
            order.setStatus("payment_failed");
            order.setPaymentStatus("Failed");
        }
        orderRepository.save(order);
    }

    public Map<String, String> extractFirstAndLastName(String contactName) {
        Map<String, String> names = new HashMap<>();
        if (contactName == null || contactName.isBlank()) {
            names.put("firstName", "DefaultFirstName");
            names.put("lastName", "DefaultLastName");
        } else {
            String[] nameParts = contactName.trim().split("\\s+", 2);
            names.put("firstName", nameParts[0]);
            names.put("lastName", nameParts.length > 1 ? nameParts[1] : "DefaultLastName");
        }
        return names;
    }

    public static String generatePayHereHash(String merchantId, String orderId, double amount, String currency, String merchantSecret) {
        // Format the amount to 2 decimal places
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedAmount = decimalFormat.format(amount);

        // Hash the merchantSecret
        String hashedSecret = Hashing.md5()
                .hashString(merchantSecret, StandardCharsets.UTF_8)
                .toString()
                .toUpperCase();

        // Concatenate merchantId, orderId, formattedAmount, currency, and hashedSecret
        String hashInput = merchantId + orderId + formattedAmount + currency + hashedSecret;

        // Hash the final input
        return Hashing.md5()
                .hashString(hashInput, StandardCharsets.UTF_8)
                .toString()
                .toUpperCase();
    }

}
