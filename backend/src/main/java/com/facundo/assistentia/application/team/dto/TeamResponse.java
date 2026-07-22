package com.facundo.assistentia.application.team.dto;

import java.util.UUID;

public record TeamResponse(UUID id, String name, String slug) {
}
