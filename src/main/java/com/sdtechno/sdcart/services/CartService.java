package com.sdtechno.sdcart.services;

import com.sdtechno.sdcart.models.CartItem;
import com.sdtechno.sdcart.models.User;
import com.sdtechno.sdcart.repositories.CartItemRepository;
import com.sdtechno.sdcart.repositories.UserRepository;
import com.sdtechno.sdcart.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    public User getUserFromToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Token is missing");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            String email = jwtUtil.extractUsername(token); // ✅ Fixed method call
            return userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }

    public List<CartItem> getCartItems(String token) {
        User user = getUserFromToken(token);
        return cartItemRepo.findByUser(user);
    }

    public CartItem addToCart(String token, CartItem item) {
        User user = getUserFromToken(token);
        item.setUser(user);
        return cartItemRepo.save(item);
    }

    public void removeFromCart(Long id, String token) {
        User user = getUserFromToken(token);
        CartItem item = cartItemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to cart item");
        }
        cartItemRepo.delete(item);
    }

    public void clearCart(String token) {
        User user = getUserFromToken(token);
        cartItemRepo.deleteByUser(user);
    }
}
