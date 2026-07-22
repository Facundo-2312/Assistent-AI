package com.facundo.assistentia.infrastructure.persistence.inmemory;

import com.facundo.assistentia.domain.team.model.Team;
import com.facundo.assistentia.domain.team.repository.TeamRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TeamInMemoryRepository implements TeamRepository {

    private final Map<UUID, Team> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Team> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Team save(Team team) {
        if (team.getId() == null) {
            team.setId(UUID.randomUUID());
        }
        storage.put(team.getId(), team);
        return team;
    }
}
