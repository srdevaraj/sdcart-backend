package com.sdtechno.sdcart.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
            "cloud_name", "your_cloud_name",      // Replace with your Cloudinary cloud name
            "api_key", "your_api_key",            // Replace with your API key
            "api_secret", "your_api_secret"       // Replace with your API secret
        );
        return new Cloudinary(config);
    }
}
