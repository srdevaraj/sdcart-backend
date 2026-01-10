package com.sdtechno.sdcart.controllers;

import com.sdtechno.sdcart.models.DeliveryAddress;
import com.sdtechno.sdcart.models.User;
import com.sdtechno.sdcart.repositories.UserRepository;
import com.sdtechno.sdcart.services.DeliveryAddressService;
import com.sdtechno.sdcart.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class DeliveryAddressController {

    private final DeliveryAddressService addressService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public DeliveryAddressController(DeliveryAddressService addressService,
                                     JwtUtil jwtUtil,
                                     UserRepository userRepository) {
        this.addressService = addressService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    // ✅ Helper method: extract user from JWT
    private User getUserFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // "Bearer <token>"
        String email = jwtUtil.extractUsername(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/add")
    public ResponseEntity<DeliveryAddress> addAddress(@RequestBody DeliveryAddress address,
                                                      HttpServletRequest request) {
        User user = getUserFromRequest(request);
        return ResponseEntity.ok(addressService.addAddress(user, address));
    }

    @GetMapping
    public ResponseEntity<DeliveryAddress> getUserAddress(HttpServletRequest request) {
        User user = getUserFromRequest(request);
        return ResponseEntity.ok(addressService.getUserAddress(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryAddress> updateAddress(@PathVariable Long id,
                                                         @RequestBody DeliveryAddress address,
                                                         HttpServletRequest request) {
        User user = getUserFromRequest(request);
        return ResponseEntity.ok(addressService.updateAddress(id, address, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id,
                                                HttpServletRequest request) {
        User user = getUserFromRequest(request);
        addressService.deleteAddress(id, user);
        return ResponseEntity.ok("Address deleted successfully");
    }
    
    
}
