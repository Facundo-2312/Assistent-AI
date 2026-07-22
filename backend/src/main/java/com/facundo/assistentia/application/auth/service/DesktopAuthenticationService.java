package com.facundo.assistentia.application.auth.service;

import com.facundo.assistentia.domain.user.model.User;
import com.facundo.assistentia.domain.user.model.UserRole;
import com.facundo.assistentia.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class DesktopAuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DesktopAuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public DesktopSession register(String username, String displayName, String password) {
        String normalizedUsername = normalizeUsername(username);
        String normalizedDisplayName = requireDisplayName(displayName);
        validatePassword(password);

        if (userRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new IllegalArgumentException("Ese nombre de usuario ya esta en uso.");
        }

        UserRole role = userRepository.count() == 0 ? UserRole.ADMIN : UserRole.MEMBER;
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(normalizedUsername)
                .firstName(normalizedDisplayName)
                .email(normalizedUsername + "@local.assistentia")
                .passwordHash(passwordEncoder.encode(password))
                .role(role)
                .build();

        return toSession(userRepository.save(user));
    }

    public Optional<DesktopSession> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }

        return userRepository.findByUsername(normalizeUsername(username))
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .map(this::toSession);
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Ingresa un nombre de usuario.");
        }

        String normalized = username.trim().toLowerCase(Locale.ROOT);
        if (!normalized.matches("[a-z0-9._-]{3,40}")) {
            throw new IllegalArgumentException("El usuario debe tener entre 3 y 40 caracteres: letras, numeros, punto, guion o guion bajo.");
        }
        return normalized;
    }

    private String requireDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("Ingresa el nombre del miembro.");
        }
        return displayName.trim();
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
    }

    private DesktopSession toSession(User user) {
        String displayName = user.getLastName() == null || user.getLastName().isBlank()
                ? user.getFirstName()
                : user.getFirstName() + " " + user.getLastName();

        return new DesktopSession(user.getId(), user.getUsername(), displayName, user.getRole());
    }
}