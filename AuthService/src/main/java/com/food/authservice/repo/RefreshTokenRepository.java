package com.food.authservice.repo;


import com.food.authservice.entity.RefreshToken;
import com.food.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken t SET t.isRevoked = true WHERE t.user = :user")
    void revokeAllTokenByUser(User user);
}