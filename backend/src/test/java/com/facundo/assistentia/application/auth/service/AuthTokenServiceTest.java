package com.facundo.assistentia.application.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.facundo.assistentia.domain.auth.model.RefreshToken;
import com.facundo.assistentia.domain.auth.repository.RefreshTokenRepository;
import com.facundo.assistentia.infrastructure.persistence.inmemory.TeamInMemoryRepository;
import com.facundo.assistentia.infrastructure.persistence.inmemory.UserInMemoryRepository;
import com.facundo.assistentia.shared.config.SecurityProperties;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthTokenServiceTest {

    @Test
    void shouldCreateSignedAccessTokenAndRotateRefreshToken() {
        UserInMemoryRepository users = new UserInMemoryRepository();
        WorkspaceAccessService workspaceAccessService = new WorkspaceAccessService(
                users,
                new TeamInMemoryRepository(),
                new BCryptPasswordEncoder()
        );
        var session = workspaceAccessService.createWorkspace(
                "Equipo Norte",
                "facundo",
                "Facundo",
                "Password123!"
        ).session();
        SecurityProperties properties = new SecurityProperties(
                "test-secret-with-at-least-thirty-two-characters",
                "assistentia-test",
                java.time.Duration.ofMinutes(15),
                java.time.Duration.ofDays(30)
        );
        JwtTokenService jwtTokenService = new JwtTokenService(new ObjectMapper(), properties);
        AuthTokenService authTokenService = new AuthTokenService(
                jwtTokenService,
                new InMemoryRefreshTokenRepository(),
                workspaceAccessService,
                properties
        );

        var initialTokens = authTokenService.createTokenPair(session);
        var principal = jwtTokenService.parseAndValidate(initialTokens.accessToken());
        var refreshedTokens = authTokenService.refresh(initialTokens.refreshToken());

        assertThat(initialTokens.tokenType()).isEqualTo("Bearer");
        assertThat(principal.userId()).isEqualTo(session.userId());
        assertThat(principal.roles()).containsExactly("ADMIN");
        assertThat(refreshedTokens.refreshToken()).isNotEqualTo(initialTokens.refreshToken());
        assertThatThrownBy(() -> authTokenService.refresh(initialTokens.refreshToken()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static final class InMemoryRefreshTokenRepository implements RefreshTokenRepository {
        private final Map<String, RefreshToken> tokens = new HashMap<>();

        @Override
        public Optional<RefreshToken> findByTokenHash(String tokenHash) {
            return Optional.ofNullable(tokens.get(tokenHash));
        }

        @Override
        public RefreshToken save(RefreshToken refreshToken) {
            tokens.put(refreshToken.getTokenHash(), refreshToken);
            return refreshToken;
        }
    }
}
