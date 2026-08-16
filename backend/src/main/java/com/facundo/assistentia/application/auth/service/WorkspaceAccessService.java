package com.facundo.assistentia.application.auth.service;

import com.facundo.assistentia.application.auth.dto.WorkspaceRegistrationResponse;
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
public class WorkspaceAccessService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

    public WorkspaceRegistrationResponse createWorkspace(String teamName, String username, String displayName, String password) {
        String normalizedTeamName = requireTeamName(teamName);
        String normalizedUsername = normalizeUsername(username);
        String normalizedDisplayName = requireDisplayName(displayName);
        validatePassword(password);

        if (userRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new IllegalArgumentException("Ese nombre de usuario ya esta en uso.");
        }

        Team team = teamRepository.save(Team.builder()
                .id(UUID.randomUUID())
                .name(normalizedTeamName)
                .slug(generateTeamCode(normalizedTeamName))
                .build());

        User user = buildUser(normalizedUsername, normalizedDisplayName, password, UserRole.ADMIN, team);
        return new WorkspaceRegistrationResponse(toSession(userRepository.save(user)), team.getName(), team.getSlug());
    }

    public WorkspaceRegistrationResponse joinWorkspace(String teamCode, String username, String displayName, String password) {
        String normalizedTeamCode = requireTeamCode(teamCode);
        String normalizedUsername = normalizeUsername(username);
        String normalizedDisplayName = requireDisplayName(displayName);
        validatePassword(password);

        if (userRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new IllegalArgumentException("Ese nombre de usuario ya esta en uso.");
        }

        Team team = teamRepository.findBySlug(normalizedTeamCode)
                .orElseThrow(() -> new IllegalArgumentException("Codigo de equipo invalido."));

        User user = buildUser(normalizedUsername, normalizedDisplayName, password, UserRole.MEMBER, team);
        return new WorkspaceRegistrationResponse(toSession(userRepository.save(user)), team.getName(), team.getSlug());
    }

    public Optional<DesktopSession> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }

        return userRepository.findByUsername(normalizeUsername(username))
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .map(this::toSession);
    }

    public DesktopSession getSession(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toSession)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta ya no existe."));
    }

    private User buildUser(String username, String displayName, String password, UserRole role, Team team) {
        return User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .firstName(displayName)
                .email(username + "@local.assistentia")
                .passwordHash(passwordEncoder.encode(password))
                .role(role)
                .team(team)
                .build();
    }

    private DesktopSession toSession(User user) {
        Team team = user.getTeam() == null ? null : teamRepository.findById(user.getTeam().getId()).orElse(user.getTeam());
        String displayName = user.getLastName() == null || user.getLastName().isBlank()
                ? user.getFirstName()
                : user.getFirstName() + " " + user.getLastName();

        return new DesktopSession(
                user.getId(),
                user.getUsername(),
                displayName,
                user.getRole(),
                team == null ? null : team.getId(),
                team == null ? null : team.getSlug(),
                team == null ? null : team.getName()
        );
    }

    private String generateTeamCode(String teamName) {
        String prefix = teamName.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        String base = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase(Locale.ROOT);
        String candidate = (prefix.isBlank() ? "TEAM" : prefix.substring(0, Math.min(4, prefix.length()))) + "-" + base;

        while (teamRepository.findBySlug(candidate).isPresent()) {
            base = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase(Locale.ROOT);
            candidate = (prefix.isBlank() ? "TEAM" : prefix.substring(0, Math.min(4, prefix.length()))) + "-" + base;
        }

        return candidate;
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

    private String requireTeamName(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            throw new IllegalArgumentException("Ingresa el nombre del equipo.");
        }
        return teamName.trim();
    }

    private String requireTeamCode(String teamCode) {
        if (teamCode == null || teamCode.isBlank()) {
            throw new IllegalArgumentException("Ingresa el codigo del equipo.");
        }
        return teamCode.trim().toUpperCase(Locale.ROOT);
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
    }
}
