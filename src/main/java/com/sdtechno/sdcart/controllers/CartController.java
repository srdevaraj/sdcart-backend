package com.sdtechno.sdcart.controllers;

import com.sdtechno.sdcart.models.CartItem;
import com.sdtechno.sdcart.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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

    // ✅ Test endpoint to verify controller is active
    @GetMapping("/test")
    public String testController() {
        return "CartController is active!";
    }

    // ✅ Add item to cart
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody CartItem item) {
        try {
            System.out.println("Adding to cart: " + item);
            CartItem addedItem = cartService.addToCart(extractToken(authHeader), item);
            return ResponseEntity.ok(addedItem);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add item to cart");
        }
    }

    // ✅ Get user cart
    @GetMapping
    public ResponseEntity<?> getCart(@RequestHeader("Authorization") String authHeader) {
        try {
            System.out.println("Fetching cart items...");
            List<CartItem> cartItems = cartService.getCartItems(extractToken(authHeader));
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch cart items");
        }
    }

    // ✅ Remove specific item from cart (with safe handling)
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeItem(@RequestHeader("Authorization") String authHeader,
                                        @PathVariable Long id) {
        try {
            System.out.println("Removing item with ID: " + id);
            cartService.removeFromCart(id, extractToken(authHeader));
            return ResponseEntity.ok("Item removed from cart");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing item from cart");
        }
    }

    // ✅ Clear entire cart
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestHeader("Authorization") String authHeader) {
        try {
            System.out.println("Clearing cart...");
            cartService.clearCart(extractToken(authHeader));
            return ResponseEntity.ok("Cart cleared");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error clearing cart");
        }
    }
}
