package com.facundo.assistentia.interfaces.rest.controller;

import com.facundo.assistentia.application.auth.dto.LoginRequest;
import com.facundo.assistentia.application.auth.dto.RefreshTokenRequest;
import com.facundo.assistentia.application.auth.dto.TokenResponse;
import com.facundo.assistentia.application.auth.service.AuthTokenService;
import com.facundo.assistentia.application.auth.service.DesktopAuthenticationService;
import com.facundo.assistentia.application.auth.service.DesktopSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.facundo.assistentia.application.auth.service.AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final DesktopAuthenticationService authenticationService;
    private final AuthTokenService authTokenService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        DesktopSession session = authenticationService.authenticate(request.username(), request.password())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contrasena incorrectos."));
        return authTokenService.createTokenPair(session);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        try {
            return authTokenService.refresh(request.refreshToken());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, exception.getMessage());
        }
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody RefreshTokenRequest request) {
        authTokenService.revoke(request.refreshToken());
    }

    @GetMapping("/me")
    public DesktopSession me(@AuthenticationPrincipal AuthenticatedPrincipal principal) {
        return authenticationService.getSession(principal.userId());
    }
}
