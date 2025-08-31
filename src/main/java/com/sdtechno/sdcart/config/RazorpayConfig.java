package com.sdtechno.sdcart.config;

import com.razorpay.RazorpayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Bean
    public RazorpayClient razorpayClient() throws Exception {
        return new RazorpayClient(
            "rzp_test_RBsVpKfL9ky7lP",      // Replace with your Test Key ID from Excel
            "K1SmKeB2ahsoZkmAGlSCndZ3"   // Replace with your Test Key Secret from Excel
        );
    }
}
