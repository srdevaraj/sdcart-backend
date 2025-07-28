package com.sdtechno.sdcart.services;

import com.sdtechno.sdcart.models.CartItem;
import com.sdtechno.sdcart.models.Product;
import com.sdtechno.sdcart.models.User;
import com.sdtechno.sdcart.repositories.CartItemRepository;
import com.sdtechno.sdcart.repositories.ProductRepository;
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
    private ProductRepository productRepo;

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
            String email = jwtUtil.extractUsername(token);
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

        // ✅ Convert productId if needed
        Long productId = item.getProductId();
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        item.setUser(user);
        item.setName(product.getName());
        item.setImageUrl(product.getImageUrl());
        item.setPrice(product.getPrice());

        return cartItemRepo.save(item);
    }

    public void removeFromCart(Long id, String token) {
        User user = getUserFromToken(token);
        CartItem item = cartItemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + id));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this cart item");
        }

        cartItemRepo.delete(item);
    }

    public void clearCart(String token) {
        User user = getUserFromToken(token);
        cartItemRepo.deleteByUser(user);
    }
}
