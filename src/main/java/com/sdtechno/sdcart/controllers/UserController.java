package com.sdtechno.sdcart.controllers;

import com.sdtechno.sdcart.models.User;
import com.sdtechno.sdcart.repositories.UserRepository;
import com.sdtechno.sdcart.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

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
}
