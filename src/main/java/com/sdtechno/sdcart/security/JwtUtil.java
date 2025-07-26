package com.sdtechno.sdcart.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET = "my-super-secret-key-that-should-be-long-enough";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    private final long EXPIRATION_TIME = 86400000; // 1 day

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // usually email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getBody().getSubject();
    }

    // ✅ Optional alias for clarity
    public String extractEmail(String token) {
        return extractUsername(token);
    }

    public String extractRole(String token) {
        return getClaims(token).getBody().get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
    }
}
