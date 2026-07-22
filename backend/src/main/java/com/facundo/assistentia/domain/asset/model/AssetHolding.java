package com.facundo.assistentia.domain.asset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetHolding {

    private UUID id;
    private UUID ownerId;
    private String assetName;
    private BigDecimal quantity;
    private Instant updatedAt;
}