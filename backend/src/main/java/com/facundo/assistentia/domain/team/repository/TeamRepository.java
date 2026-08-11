package com.facundo.assistentia.domain.team.repository;

import com.facundo.assistentia.domain.team.model.Team;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository {
    Optional<Team> findById(UUID id);
    Optional<Team> findBySlug(String slug);
    Team save(Team team);
}
