package com.facundo.assistentia.domain.auth.repository;

import com.facundo.assistentia.domain.auth.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    RefreshToken save(RefreshToken refreshToken);
}
