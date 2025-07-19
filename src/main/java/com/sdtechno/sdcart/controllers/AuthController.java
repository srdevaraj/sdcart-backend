package com.sdtechno.sdcart.controllers;

import com.sdtechno.sdcart.models.User;
import com.sdtechno.sdcart.payload.RegisterRequest;
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
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        System.out.println("Incoming register request:");
        System.out.println("Email: " + request.getEmail());
        System.out.println("First Name: " + request.getFirstName());
        System.out.println("Last Name: " + request.getLastName());
        System.out.println("DOB: " + request.getDob());
        System.out.println("Mobile: " + request.getMobile());
        System.out.println("Alt Mobile: " + request.getAltMobile());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        user.setMobile(request.getMobile());
        user.setAltMobile(request.getAltMobile());

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
