package com.sdtechno.sdcart.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sdtechno.sdcart.services.CustomUserDetailsService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        logger.info("Request URI: {}", request.getRequestURI());
        logger.info("Authorization Header Present: {}", authHeader != null);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("No Bearer token found");
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String token = authHeader.substring(7);

            logger.info("Token received");

            String username = jwtUtil.extractUsername(token);

            logger.info("Username extracted from token: {}", username);

            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                logger.info("User loaded from DB: {}", userDetails.getUsername());
                logger.info("Authorities: {}", userDetails.getAuthorities());

                boolean valid =
                        jwtUtil.validateToken(token, userDetails);

                logger.info("Token valid: {}", valid);

                if (valid) {

                    logger.info("Setting authentication in SecurityContext");

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);

                    logger.info("Authentication successfully set");
                } else {
                    logger.warn("Token validation failed");
                }
            }

        } catch (JwtException e) {

            logger.error("JWT Exception: {}", e.getMessage(), e);

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}