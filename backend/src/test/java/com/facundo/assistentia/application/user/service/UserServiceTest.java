package com.facundo.assistentia.application.user.service;

import com.facundo.assistentia.application.team.dto.TeamCreateRequest;
import com.facundo.assistentia.application.team.dto.TeamResponse;
import com.facundo.assistentia.application.team.service.TeamService;
import com.facundo.assistentia.application.user.dto.UserCreateRequest;
import com.facundo.assistentia.application.user.dto.UserResponse;
import com.facundo.assistentia.domain.team.model.Team;
import com.facundo.assistentia.domain.user.model.User;
import com.facundo.assistentia.domain.user.model.UserRole;
import com.facundo.assistentia.domain.team.repository.TeamRepository;
import com.facundo.assistentia.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private UserAccountService userAccountService;

    private void initService() {
        userAccountService = new UserAccountService(userRepository, teamRepository, passwordEncoder);
        userService = new UserService(userAccountService);
    }

    @Test
    void shouldRegisterUserWithEncodedPassword() {
        initService();

        UUID teamId = UUID.randomUUID();
        Team team = Team.builder()
                .id(teamId)
                .name("Alpha")
                .slug("alpha")
                .build();

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(userRepository.existsByEmail("ana@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Secret123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.register(new UserCreateRequest(
                "Ana",
                "Martinez",
                "ana@example.com",
                "Secret123",
                UserRole.MEMBER,
                teamId
        ));

        assertThat(response.email()).isEqualTo("ana@example.com");
        assertThat(response.role()).isEqualTo(UserRole.MEMBER);
        verify(passwordEncoder).encode("Secret123");
    }
}
