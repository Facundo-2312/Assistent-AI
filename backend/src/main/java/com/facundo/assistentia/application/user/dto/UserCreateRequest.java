package com.facundo.assistentia.application.user.dto;

import com.facundo.assistentia.domain.user.model.UserRole;

import java.util.UUID;

public record UserCreateRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        UserRole role,
        UUID teamId
) {
}
