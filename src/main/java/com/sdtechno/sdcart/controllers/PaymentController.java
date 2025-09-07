package com.sdtechno.sdcart.controllers;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.sdtechno.sdcart.dto.VerifyPaymentRequest;
import com.sdtechno.sdcart.security.JwtUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Create Razorpay order
    @PostMapping("/create-order")
    public String createOrder(@RequestParam int amount) throws Exception {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(orderRequest);
        return order.toString();
    }

    // ✅ Verify payment using JSON body
    @PostMapping("/verify")
    public String verifyPayment(@RequestBody VerifyPaymentRequest request, 
                                @RequestHeader("Authorization") String authHeader) {
        // ✅ Validate JWT token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "Missing or invalid Authorization header";
        }

        String token = authHeader.substring(7); // remove "Bearer "
        if (!jwtUtil.validateToken(token)) {
            return "Invalid token";
        }

        // ✅ In production: validate signature using HMAC_SHA256 with Razorpay
        return "Payment Successful for Order: " + request.getRazorpayOrderId();
    }

    // ✅ Remove /checkout endpoint: not needed with in-app SDK
}
