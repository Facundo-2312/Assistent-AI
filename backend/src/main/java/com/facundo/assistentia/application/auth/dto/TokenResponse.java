package com.facundo.assistentia.application.auth.dto;

import com.facundo.assistentia.application.auth.service.DesktopSession;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        DesktopSession session
) {
}
