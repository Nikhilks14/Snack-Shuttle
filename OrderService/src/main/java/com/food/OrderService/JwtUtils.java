package com.food.OrderService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JwtUtils {
    private final String base64Secret = "bW92aW5nbWFwc2Nob29sc2hvcnRncmF2aXR5YmVjb21pbmdicmVlemVmbGFtZWhlcnM=";
    private final SecretKey secretKey;
    private final int jwtExpirationMs = 86400000; // 1 day

    public JwtUtils() {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // log exception
        }
        return false;
    }
}
