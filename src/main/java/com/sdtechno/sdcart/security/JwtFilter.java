package com.sdtechno.sdcart.security;

import com.sdtechno.sdcart.services.CustomUserDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Publicly accessible endpoints
        if (
            path.startsWith("/api/auth") ||
            path.equals("/products/light") ||
            path.startsWith("/products/product/")||
            path.startsWith("/products/test")||
            path.startsWith("/api/ads")||
            path.startsWith("/products/search")||
            path.startsWith("/products/category/")
        ) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Invalid or expired token");
            return;
        }

        String email = jwtUtil.extractUsername(token); // ✅ Changed to extractUsername() for clarity
        String role = jwtUtil.extractRole(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        chain.doFilter(request, response);
    }
}
