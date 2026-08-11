package com.facundo.assistentia.application.auth.dto;

public record WorkspaceJoinRequest(
        String teamCode,
        String username,
        String displayName,
        String password
) {
}