package com.food.authservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , nullable = false , length = 512)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    // isRevoked is a flag indicating whether a refresh token is still valid for use.
    // false → token is active, can be used for refreshing.
    // true → token is revoked, meaning it’s no longer usable (user logged out, rotated, or reuse detected).

    @Column(nullable = false)
    private boolean isRevoked = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
}