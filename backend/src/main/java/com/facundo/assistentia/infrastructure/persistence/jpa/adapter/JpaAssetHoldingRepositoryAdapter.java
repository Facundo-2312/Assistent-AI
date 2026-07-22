package com.facundo.assistentia.infrastructure.persistence.jpa.adapter;

import com.facundo.assistentia.domain.asset.model.AssetHolding;
import com.facundo.assistentia.domain.asset.repository.AssetHoldingRepository;
import com.facundo.assistentia.infrastructure.persistence.jpa.entity.AssetHoldingEntity;
import com.facundo.assistentia.infrastructure.persistence.jpa.repository.AssetHoldingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaAssetHoldingRepositoryAdapter implements AssetHoldingRepository {

    private final AssetHoldingJpaRepository assetHoldingJpaRepository;

    @Override
    public Optional<AssetHolding> findByOwnerIdAndAssetName(UUID ownerId, String assetName) {
        return assetHoldingJpaRepository.findByOwnerIdAndAssetNameIgnoreCase(ownerId, assetName)
                .map(this::toDomain);
    }

    @Override
    public List<AssetHolding> findAll() {
        return assetHoldingJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public AssetHolding save(AssetHolding holding) {
        return toDomain(assetHoldingJpaRepository.save(toEntity(holding)));
    }

    private AssetHoldingEntity toEntity(AssetHolding holding) {
        return AssetHoldingEntity.builder()
                .id(holding.getId())
                .ownerId(holding.getOwnerId())
                .assetName(holding.getAssetName())
                .quantity(holding.getQuantity())
                .updatedAt(holding.getUpdatedAt())
                .build();
    }

    private AssetHolding toDomain(AssetHoldingEntity holding) {
        return AssetHolding.builder()
                .id(holding.getId())
                .ownerId(holding.getOwnerId())
                .assetName(holding.getAssetName())
                .quantity(holding.getQuantity())
                .updatedAt(holding.getUpdatedAt())
                .build();
    }
}