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
        user.setRole("ROLE_USER");

        userRepository.save(user);

        // ➕ Add user info to token
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("role", user.getRole());

        String token = jwtUtil.generateToken(claims, user.getEmail()); // email as sub

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> userOptional = userRepository.findByEmail(loginUser.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        // ➕ Add user info to token
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("role", user.getRole());

        String token = jwtUtil.generateToken(claims, user.getEmail());

        return ResponseEntity.ok(Map.of("token", token));
    }
}
