package com.food.authservice;

import com.food.authservice.Repo.RefreshTokenRepository;
import com.food.authservice.entity.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenCleanupTask {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 2 * * *") // runs every day at 2 AM
    @Transactional
    public void cleanExpiredTokens() {
        Instant now = Instant.now();
        List<RefreshToken> expired = refreshTokenRepository.findAll().stream()
                .filter(t -> t.getExpiryDate().isBefore(now))
                .toList();

        if (!expired.isEmpty()) {
            refreshTokenRepository.deleteAll(expired);
            System.out.println("🧹 Cleaned up " + expired.size() + " expired refresh tokens.");
        }
    }
}
