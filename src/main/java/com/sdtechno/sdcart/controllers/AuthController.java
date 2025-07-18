package com.sdtechno.sdcart.controllers;

import com.sdtechno.sdcart.models.User;
import com.sdtechno.sdcart.repositories.UserRepository;
import com.sdtechno.sdcart.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> user = userRepository.findByEmail(loginUser.getEmail());

        if (user.isEmpty() || !passwordEncoder.matches(loginUser.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.get().getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
