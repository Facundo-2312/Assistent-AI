package com.facundo.assistentia.application.auth.dto;

public record WorkspaceCreateRequest(
        String teamName,
        String username,
        String displayName,
        String password
) {
}