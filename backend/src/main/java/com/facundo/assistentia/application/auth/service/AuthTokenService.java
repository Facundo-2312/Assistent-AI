package com.facundo.assistentia.application.auth.service;

import com.facundo.assistentia.application.auth.dto.TokenResponse;
import com.facundo.assistentia.domain.auth.model.RefreshToken;
import com.facundo.assistentia.domain.auth.repository.RefreshTokenRepository;
import com.facundo.assistentia.shared.config.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final JwtTokenService jwtTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final WorkspaceAccessService workspaceAccessService;
    private final SecurityProperties securityProperties;

    public TokenResponse createTokenPair(DesktopSession session) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(securityProperties.accessTokenTtl());
        String accessToken = jwtTokenService.createAccessToken(session, issuedAt, expiresAt);
        String refreshToken = createRefreshToken(session.userId(), issuedAt);

        return new TokenResponse(
                accessToken,
                refreshToken,
                "Bearer",
                securityProperties.accessTokenTtl().toSeconds(),
                session
        );
    }

    public TokenResponse refresh(String rawRefreshToken) {
        Instant now = Instant.now();
        RefreshToken existing = refreshTokenRepository.findByTokenHash(hash(rawRefreshToken))
                .filter(token -> token.isActiveAt(now))
                .orElseThrow(() -> new IllegalArgumentException("El refresh token es invalido o vencio."));

        existing.setRevokedAt(now);
        refreshTokenRepository.save(existing);
        return createTokenPair(workspaceAccessService.getSession(existing.getUserId()));
    }

    public void revoke(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            return;
        }

        refreshTokenRepository.findByTokenHash(hash(rawRefreshToken))
                .filter(token -> token.getRevokedAt() == null)
                .ifPresent(token -> {
                    token.setRevokedAt(Instant.now());
                    refreshTokenRepository.save(token);
                });
    }

    private String createRefreshToken(UUID userId, Instant now) {
        byte[] randomBytes = new byte[48];
        SECURE_RANDOM.nextBytes(randomBytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        refreshTokenRepository.save(RefreshToken.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .tokenHash(hash(rawToken))
                .createdAt(now)
                .expiresAt(now.plus(securityProperties.refreshTokenTtl()))
                .build());

        return rawToken;
    }

    private String hash(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new IllegalArgumentException("El refresh token es obligatorio.");
        }

        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 no esta disponible.", exception);
        }
    }
}
