package com.facundo.assistentia.application.auth.service;

import com.facundo.assistentia.application.auth.dto.WorkspaceRegistrationResponse;
import com.facundo.assistentia.infrastructure.persistence.inmemory.TeamInMemoryRepository;
import com.facundo.assistentia.infrastructure.persistence.inmemory.UserInMemoryRepository;
import com.facundo.assistentia.domain.user.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class WorkspaceAccessServiceTest {

    @Test
    void shouldCreateWorkspaceAndAllowJoiningWithInviteCode() {
        UserInMemoryRepository userRepository = new UserInMemoryRepository();
        TeamInMemoryRepository teamRepository = new TeamInMemoryRepository();
        WorkspaceAccessService accessService = new WorkspaceAccessService(
                userRepository,
                teamRepository,
                new BCryptPasswordEncoder()
        );

        WorkspaceRegistrationResponse created = accessService.createWorkspace(
                "Equipo Alpha",
                "adminalpha",
                "Admin Alpha",
                "Password123!"
        );

        assertThat(created.teamCode()).isNotBlank();
        assertThat(created.teamName()).isEqualTo("Equipo Alpha");
        assertThat(created.session().role()).isEqualTo(UserRole.ADMIN);

        WorkspaceRegistrationResponse joined = accessService.joinWorkspace(
                created.teamCode(),
                "mariaalpha",
                "Maria Alpha",
                "Password123!"
        );

        assertThat(joined.teamCode()).isEqualTo(created.teamCode());
        assertThat(joined.teamName()).isEqualTo("Equipo Alpha");
        assertThat(joined.session().role()).isEqualTo(UserRole.MEMBER);
        assertThat(accessService.authenticate("mariaalpha", "Password123!")).contains(joined.session());
    }
}
