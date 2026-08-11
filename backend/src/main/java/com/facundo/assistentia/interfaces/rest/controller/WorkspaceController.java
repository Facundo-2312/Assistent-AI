package com.facundo.assistentia.interfaces.rest.controller;

import com.facundo.assistentia.application.auth.dto.WorkspaceCreateRequest;
import com.facundo.assistentia.application.auth.dto.WorkspaceJoinRequest;
import com.facundo.assistentia.application.auth.dto.WorkspaceRegistrationResponse;
import com.facundo.assistentia.application.auth.service.DesktopAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final DesktopAuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkspaceRegistrationResponse create(@RequestBody WorkspaceCreateRequest request) {
        return authenticationService.createWorkspace(
                request.teamName(),
                request.username(),
                request.displayName(),
                request.password()
        );
    }

    @PostMapping("/join")
    public ResponseEntity<WorkspaceRegistrationResponse> join(@RequestBody WorkspaceJoinRequest request) {
        return ResponseEntity.ok(authenticationService.joinWorkspace(
                request.teamCode(),
                request.username(),
                request.displayName(),
                request.password()
        ));
    }
}