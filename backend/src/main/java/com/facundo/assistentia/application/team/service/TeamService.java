package com.facundo.assistentia.application.team.service;

import com.facundo.assistentia.application.team.dto.TeamCreateRequest;
import com.facundo.assistentia.application.team.dto.TeamResponse;
import com.facundo.assistentia.domain.team.model.Team;
import com.facundo.assistentia.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamResponse create(TeamCreateRequest request) {
        Team team = Team.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .slug(request.slug())
                .build();

        Team saved = teamRepository.save(team);

        return new TeamResponse(saved.getId(), saved.getName(), saved.getSlug());
    }
}
