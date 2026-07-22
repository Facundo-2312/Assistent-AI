package com.facundo.assistentia.infrastructure.persistence.jpa.repository;

import com.facundo.assistentia.infrastructure.persistence.jpa.entity.WorkspaceUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceUserJpaRepository extends JpaRepository<WorkspaceUserEntity, UUID> {

    Optional<WorkspaceUserEntity> findByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);
}