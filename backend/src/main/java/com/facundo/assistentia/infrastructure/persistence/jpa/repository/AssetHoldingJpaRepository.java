package com.facundo.assistentia.infrastructure.persistence.jpa.repository;

import com.facundo.assistentia.infrastructure.persistence.jpa.entity.AssetHoldingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssetHoldingJpaRepository extends JpaRepository<AssetHoldingEntity, UUID> {

    Optional<AssetHoldingEntity> findByOwnerIdAndAssetNameIgnoreCase(UUID ownerId, String assetName);
}