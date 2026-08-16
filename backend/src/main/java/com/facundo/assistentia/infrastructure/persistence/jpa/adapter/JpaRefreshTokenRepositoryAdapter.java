package com.facundo.assistentia.infrastructure.persistence.jpa.adapter;

import com.facundo.assistentia.domain.auth.model.RefreshToken;
import com.facundo.assistentia.domain.auth.repository.RefreshTokenRepository;
import com.facundo.assistentia.infrastructure.persistence.jpa.entity.RefreshTokenEntity;
import com.facundo.assistentia.infrastructure.persistence.jpa.repository.RefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaRefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return refreshTokenJpaRepository.findByTokenHash(tokenHash).map(this::toDomain);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return toDomain(refreshTokenJpaRepository.save(toEntity(refreshToken)));
    }

    private RefreshTokenEntity toEntity(RefreshToken token) {
        return RefreshTokenEntity.builder()
                .id(token.getId())
                .userId(token.getUserId())
                .tokenHash(token.getTokenHash())
                .createdAt(token.getCreatedAt())
                .expiresAt(token.getExpiresAt())
                .revokedAt(token.getRevokedAt())
                .build();
    }

    private RefreshToken toDomain(RefreshTokenEntity token) {
        return RefreshToken.builder()
                .id(token.getId())
                .userId(token.getUserId())
                .tokenHash(token.getTokenHash())
                .createdAt(token.getCreatedAt())
                .expiresAt(token.getExpiresAt())
                .revokedAt(token.getRevokedAt())
                .build();
    }
}
