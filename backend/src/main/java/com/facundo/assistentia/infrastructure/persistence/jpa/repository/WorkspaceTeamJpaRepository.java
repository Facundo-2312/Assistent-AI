package com.facundo.assistentia.infrastructure.persistence.jpa.repository;

import com.facundo.assistentia.infrastructure.persistence.jpa.entity.WorkspaceTeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceTeamJpaRepository extends JpaRepository<WorkspaceTeamEntity, UUID> {

    Optional<WorkspaceTeamEntity> findBySlugIgnoreCase(String slug);
}