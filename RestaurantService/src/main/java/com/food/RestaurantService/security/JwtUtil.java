package com.food.RestaurantService.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key key;

    @PostConstruct
    public void init() {
        // create HMAC key from secret bytes
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Validate token signature and expiry
     */
    public Jws<Claims> validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            throw e; // controller/filter will handle and reject
        }
    }

    public String getUsername(String token) {
        Claims claims = validateToken(token).getBody();
        return claims.getSubject();
    }

    public List<String> getRoles(String token) {
        Claims claims = validateToken(token).getBody();
        Object roles = claims.get("roles");
        if (roles instanceof List) {
            return (List<String>) roles;
        } else if (roles instanceof String) {
            return List.of(((String) roles).split(","));
        }
        return Collections.emptyList();
    }
}
