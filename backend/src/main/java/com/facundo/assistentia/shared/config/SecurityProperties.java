package com.facundo.assistentia.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
        String jwtSecret,
        String jwtIssuer,
        Duration accessTokenTtl,
        Duration refreshTokenTtl
) {

    public SecurityProperties {
        if (jwtSecret == null || jwtSecret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET debe tener al menos 32 caracteres.");
        }
        if (jwtIssuer == null || jwtIssuer.isBlank()) {
            throw new IllegalStateException("JWT_ISSUER no puede estar vacio.");
        }
        if (accessTokenTtl == null || accessTokenTtl.isNegative() || accessTokenTtl.isZero()) {
            throw new IllegalStateException("JWT_ACCESS_TOKEN_TTL debe ser una duracion positiva.");
        }
        if (refreshTokenTtl == null || refreshTokenTtl.isNegative() || refreshTokenTtl.isZero()) {
            throw new IllegalStateException("JWT_REFRESH_TOKEN_TTL debe ser una duracion positiva.");
        }
    }
}
