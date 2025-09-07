package com.sdtechno.sdcart.controllers;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.sdtechno.sdcart.dto.VerifyPaymentRequest;
import com.sdtechno.sdcart.security.JwtUtil; // Import JwtUtil to validate token
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private JwtUtil jwtUtil; // ✅ Inject JwtUtil

    // Create Razorpay order
    @PostMapping("/create-order")
    public String createOrder(@RequestParam int amount) throws Exception {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(orderRequest);
        return order.toString();
    }

    // Verify payment using JSON body
    @PostMapping("/verify")
    public String verifyPayment(@RequestBody VerifyPaymentRequest request) {
        // In production: validate signature using HMAC_SHA256
        // For now: Test Mode = always successful
        return "Payment Successful for Order: " + request.getRazorpayOrderId();
    }

    // ✅ New Checkout endpoint with token validation
    @GetMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestParam String order_id, @RequestParam String token) {
        try {
            // ✅ Validate the token
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing token");
            }

            String email = jwtUtil.extractUsername(token); // Extract username/email from token
            if (email == null || !jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid token");
            }

            // ✅ Token is valid
            // You can forward this to the payment page or return details
            String checkoutUrl = "https://checkout.razorpay.com/v1/checkout.js?order_id=" + order_id;

            return ResponseEntity.ok("Payment page URL: " + checkoutUrl);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: " + e.getMessage());
        }
    }
}
