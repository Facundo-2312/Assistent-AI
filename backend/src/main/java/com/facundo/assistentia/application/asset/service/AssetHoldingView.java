package com.facundo.assistentia.application.asset.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AssetHoldingView(
        UUID id,
        String assetName,
        BigDecimal quantity,
        String ownerName,
        String ownerUsername,
        Instant updatedAt
) {
}