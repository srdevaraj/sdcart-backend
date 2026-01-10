package com.sdtechno.sdcart.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdtechno.sdcart.dto.AccountInfoUserDetailsDto;
import com.sdtechno.sdcart.exceptions.ResourceNotFoundException;
import com.sdtechno.sdcart.models.User;
import com.sdtechno.sdcart.repositories.UserRepository;
import com.sdtechno.sdcart.security.JwtUtil;
import com.sdtechno.sdcart.services.AccountUserInfoService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AccountUserInfoService accountUserInfoService;

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = tokenHeader.substring(7); // Remove "Bearer "
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        String email = jwtUtil.extractEmail(token);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        return ResponseEntity.ok(user.get());
    }
    
    @GetMapping("/userinfo")
    public AccountInfoUserDetailsDto getUserAccountInfo(@RequestHeader("Authorization") String tokenHeader) {
  
         String token = tokenHeader.substring(7); // Remove "Bearer "
 
         String email = jwtUtil.extractEmail(token);
         
         AccountInfoUserDetailsDto user = accountUserInfoService.getUserAccDetails(email);
         
         return user;
    }
}
