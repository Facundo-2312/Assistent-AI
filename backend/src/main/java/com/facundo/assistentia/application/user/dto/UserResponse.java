package com.facundo.assistentia.application.user.dto;

import com.facundo.assistentia.domain.user.model.UserRole;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        UUID teamId
) {
}
