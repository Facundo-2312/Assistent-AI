package com.facundo.assistentia.application.auth.service;

import java.util.List;
import java.util.UUID;

public record AuthenticatedPrincipal(
        UUID userId,
        String username,
        List<String> roles
) {
}
