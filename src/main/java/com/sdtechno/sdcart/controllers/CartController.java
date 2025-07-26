package com.sdtechno.sdcart.controllers;

import com.sdtechno.sdcart.models.CartItem;
import com.sdtechno.sdcart.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        throw new RuntimeException("Invalid or missing Authorization header");
    }

    // Test endpoint to verify controller is active
    @GetMapping("/test")
    public String testController() {
        return "CartController is active!";
    }

    @PostMapping("/add")
    public CartItem addToCart(@RequestHeader("Authorization") String authHeader,
                              @RequestBody CartItem item) {
        System.out.println("Adding to cart: " + item);
        return cartService.addToCart(extractToken(authHeader), item);
    }

    @GetMapping
    public List<CartItem> getCart(@RequestHeader("Authorization") String authHeader) {
        System.out.println("Fetching cart items...");
        return cartService.getCartItems(extractToken(authHeader));
    }

    @DeleteMapping("/remove/{id}")
    public void removeItem(@RequestHeader("Authorization") String authHeader,
                           @PathVariable Long id) {
        System.out.println("Removing item with ID: " + id);
        cartService.removeFromCart(id, extractToken(authHeader));
    }

    @DeleteMapping("/clear")
    public void clearCart(@RequestHeader("Authorization") String authHeader) {
        System.out.println("Clearing cart...");
        cartService.clearCart(extractToken(authHeader));
    }
}
