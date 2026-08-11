package com.facundo.assistentia.infrastructure.persistence.jpa.repository;

import com.facundo.assistentia.domain.team.model.Team;
import com.facundo.assistentia.domain.team.repository.TeamRepository;
import com.facundo.assistentia.infrastructure.persistence.jpa.entity.WorkspaceTeamEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("!test")
@Primary
@RequiredArgsConstructor
public class WorkspaceTeamRepositoryAdapter implements TeamRepository {

    private final WorkspaceTeamJpaRepository teamJpaRepository;

    @Override
    public Optional<Team> findById(UUID id) {
        return teamJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Team> findBySlug(String slug) {
        return teamJpaRepository.findBySlugIgnoreCase(slug).map(this::toDomain);
    }

    @Override
    public Team save(Team team) {
        return toDomain(teamJpaRepository.save(toEntity(team)));
    }

    private WorkspaceTeamEntity toEntity(Team team) {
        return WorkspaceTeamEntity.builder()
                .id(team.getId())
                .name(team.getName())
                .slug(team.getSlug())
                .build();
    }

    private Team toDomain(WorkspaceTeamEntity team) {
        return Team.builder()
                .id(team.getId())
                .name(team.getName())
                .slug(team.getSlug())
                .build();
    }
}