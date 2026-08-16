package com.facundo.assistentia.application.auth.dto;

public record LoginRequest(
        String username,
        String password
) {
}
