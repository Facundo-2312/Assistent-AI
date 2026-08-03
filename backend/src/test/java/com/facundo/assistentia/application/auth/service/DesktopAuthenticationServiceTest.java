package com.facundo.assistentia.application.auth.service;

import com.facundo.assistentia.application.user.service.UserAccountService;
import com.facundo.assistentia.infrastructure.persistence.inmemory.TeamInMemoryRepository;
import com.facundo.assistentia.domain.user.model.UserRole;
import com.facundo.assistentia.infrastructure.persistence.inmemory.UserInMemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class DesktopAuthenticationServiceTest {

    @Test
    void shouldRegisterFirstMemberAsAdminAndAuthenticateWithPassword() {
        UserInMemoryRepository userRepository = new UserInMemoryRepository();
        UserAccountService userAccountService = new UserAccountService(
                userRepository,
                new TeamInMemoryRepository(),
                new BCryptPasswordEncoder()
        );
        DesktopAuthenticationService authenticationService = new DesktopAuthenticationService(
                userAccountService
        );

        DesktopSession registeredMember = authenticationService.register(
                "facundo",
                "Facundo",
                "Password123!"
        );

        assertThat(registeredMember.role()).isEqualTo(UserRole.ADMIN);
        assertThat(authenticationService.authenticate("facundo", "Password123!")).contains(registeredMember);
        assertThat(authenticationService.authenticate("facundo", "incorrecta")).isEmpty();
        assertThat(userRepository.findByUsername("facundo").orElseThrow().getPasswordHash())
                .isNotEqualTo("Password123!");
    }
}