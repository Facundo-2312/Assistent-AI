package com.facundo.assistentia.application.auth.service;

import com.facundo.assistentia.application.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DesktopAuthenticationService {

    private final UserAccountService userAccountService;

    public DesktopSession register(String username, String displayName, String password) {
        return userAccountService.registerDesktopUser(username, displayName, password);
    }

    public Optional<DesktopSession> authenticate(String username, String password) {
        return userAccountService.authenticateDesktopUser(username, password);
    }
}