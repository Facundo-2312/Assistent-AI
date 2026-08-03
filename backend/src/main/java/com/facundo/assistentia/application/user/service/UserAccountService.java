package com.facundo.assistentia.application.user.service;

import com.facundo.assistentia.application.auth.service.DesktopSession;
import com.facundo.assistentia.application.user.dto.UserCreateRequest;
import com.facundo.assistentia.application.user.dto.UserResponse;
import com.facundo.assistentia.domain.team.model.Team;
import com.facundo.assistentia.domain.team.repository.TeamRepository;
import com.facundo.assistentia.domain.user.model.User;
import com.facundo.assistentia.domain.user.model.UserRole;
import com.facundo.assistentia.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerTeamMember(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        User saved = userRepository.save(buildTeamUser(request, team));
        return toResponse(saved);
    }

    public DesktopSession registerDesktopUser(String username, String displayName, String password) {
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

    public Optional<DesktopSession> authenticateDesktopUser(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }

        return userRepository.findByUsername(normalizeUsername(username))
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .map(this::toSession);
    }

    private User buildTeamUser(UserCreateRequest request, Team team) {
        return User.builder()
                .id(UUID.randomUUID())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .username(usernameFrom(request.email()))
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(request.role())
                .team(team)
                .build();
    }

    private UserResponse toResponse(User saved) {
        return new UserResponse(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getRole(),
                saved.getTeam().getId()
        );
    }

    private DesktopSession toSession(User user) {
        String displayName = user.getLastName() == null || user.getLastName().isBlank()
                ? user.getFirstName()
                : user.getFirstName() + " " + user.getLastName();

        return new DesktopSession(user.getId(), user.getUsername(), displayName, user.getRole());
    }

    private String usernameFrom(String email) {
        int separator = email.indexOf('@');
        return separator > 0 ? email.substring(0, separator).toLowerCase(Locale.ROOT) : email.toLowerCase(Locale.ROOT);
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
}