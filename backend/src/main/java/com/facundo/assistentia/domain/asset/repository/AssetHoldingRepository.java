package com.facundo.assistentia.domain.asset.repository;

import com.facundo.assistentia.domain.asset.model.AssetHolding;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetHoldingRepository {

    Optional<AssetHolding> findByOwnerIdAndAssetName(UUID ownerId, String assetName);

    List<AssetHolding> findAll();

    AssetHolding save(AssetHolding holding);
}