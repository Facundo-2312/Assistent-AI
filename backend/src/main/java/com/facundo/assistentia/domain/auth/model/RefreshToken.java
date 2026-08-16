package com.facundo.assistentia.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    private UUID id;
    private UUID userId;
    private String tokenHash;
    private Instant createdAt;
    private Instant expiresAt;
    private Instant revokedAt;

    public boolean isActiveAt(Instant instant) {
        return revokedAt == null && expiresAt.isAfter(instant);
    }
}
