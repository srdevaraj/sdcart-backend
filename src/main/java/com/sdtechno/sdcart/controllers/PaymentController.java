package com.sdtechno.sdcart.controllers;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private RazorpayClient razorpayClient;

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

    // Verify payment (simplified for Test Mode)
    @PostMapping("/verify")
    public String verifyPayment(@RequestParam String razorpayPaymentId,
                                @RequestParam String razorpayOrderId,
                                @RequestParam String razorpaySignature) {
        // For test mode, assume payment is always successful
        return "Payment Successful! Your order is confirmed.";
    }
}
