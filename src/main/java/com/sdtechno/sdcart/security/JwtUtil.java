package com.sdtechno.sdcart.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

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

    
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    private <T>T extractAllClaims(String token,Function<Claims,T>claimResolver){
    	Claims claims = getClaims(token);
    	return claimResolver.apply(claims);
    }
    
    public String extractUsername(String token) {
        return extractAllClaims(token,Claims::getSubject);
    }

    // ✅ Optional alias for clarity
    public String extractEmail(String token) {
        return extractUsername(token);
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }
    
    public Date extractExpiration(String token) {
    	return extractAllClaims(token,Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
    	return extractExpiration(token).before(new Date());
    }
    public boolean validateToken(String token,UserDetails userDetails) {
    	final String username = extractAllClaims(token,Claims::getSubject);
    	return username.equals(username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
