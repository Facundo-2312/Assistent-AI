package com.facundo.assistentia.interfaces.rest.controller;

import com.facundo.assistentia.application.team.dto.TeamCreateRequest;
import com.facundo.assistentia.application.team.dto.TeamResponse;
import com.facundo.assistentia.application.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse create(@RequestBody TeamCreateRequest request) {
        return teamService.create(request);
    }
}
