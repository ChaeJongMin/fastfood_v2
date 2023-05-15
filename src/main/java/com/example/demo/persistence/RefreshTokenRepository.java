package com.example.demo.persistence;

import com.example.demo.config.auth.jwt.domain.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokens,Long> {
    Optional<RefreshTokens> findByKeyId(String key);
    Optional<RefreshTokens> findByValue(String key);
    void deleteByKeyId(String key);
    void deleteByValue(String refreshToken);
    boolean existsByKeyId(String key);
    boolean existsByValue(String token);
}
