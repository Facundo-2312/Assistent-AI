package com.facundo.assistentia.application.auth.dto;

import com.facundo.assistentia.application.auth.service.DesktopSession;

public record WorkspaceRegistrationResponse(
        DesktopSession session,
        String teamName,
        String teamCode
) {
}