package com.facundo.assistentia.application.user.service;

import com.facundo.assistentia.application.user.dto.UserCreateRequest;
import com.facundo.assistentia.application.user.dto.UserResponse;
import com.facundo.assistentia.domain.team.model.Team;
import com.facundo.assistentia.domain.team.repository.TeamRepository;
import com.facundo.assistentia.domain.user.model.User;
import com.facundo.assistentia.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName(request.firstName())
                .lastName(request.lastName())
            .username(usernameFrom(request.email()))
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(request.role())
                .team(team)
                .build();

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getRole(),
                saved.getTeam().getId()
        );
    }

    private String usernameFrom(String email) {
        int separator = email.indexOf('@');
        return separator > 0 ? email.substring(0, separator).toLowerCase() : email.toLowerCase();
    }
}
