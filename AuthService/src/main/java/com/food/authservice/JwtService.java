package com.food.authservice;

import com.food.authservice.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String SECRET_KEY ;

    // In-memory blacklist (can replace with Redis later)
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public String generateAccessToken(AppUser user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 mins
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(AppUser user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())  // ensures uniqueness each time
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 )) // 7 days
                .setId(UUID.randomUUID().toString())
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }

    public void blacklistToken(String token){
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token){
        return blacklistedTokens.contains(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64
                .decode(Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

