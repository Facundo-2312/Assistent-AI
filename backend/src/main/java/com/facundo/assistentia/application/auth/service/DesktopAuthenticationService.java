package com.facundo.assistentia.application.auth.service;

import com.facundo.assistentia.application.auth.dto.WorkspaceRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DesktopAuthenticationService {

    private final WorkspaceAccessService workspaceAccessService;

    public DesktopSession register(String username, String displayName, String password) {
        return workspaceAccessService.createWorkspace(displayName, username, displayName, password).session();
    }

    public WorkspaceRegistrationResponse createWorkspace(String teamName, String username, String displayName, String password) {
        return workspaceAccessService.createWorkspace(teamName, username, displayName, password);
    }

    public WorkspaceRegistrationResponse joinWorkspace(String teamCode, String username, String displayName, String password) {
        return workspaceAccessService.joinWorkspace(teamCode, username, displayName, password);
    }

    public Optional<DesktopSession> authenticate(String username, String password) {
        return workspaceAccessService.authenticate(username, password);
    }

    public DesktopSession getSession(UUID userId) {
        return workspaceAccessService.getSession(userId);
    }
}
