package com.facundo.assistentia.application.auth.service;

import com.facundo.assistentia.domain.user.model.UserRole;

import java.util.UUID;

public record DesktopSession(
        UUID userId,
        String username,
        String displayName,
        UserRole role,
        UUID teamId,
        String teamCode,
        String teamName
) {
}