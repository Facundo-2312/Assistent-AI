package com.facundo.assistentia.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "asset_holdings",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_asset_holdings_owner_asset",
                columnNames = {"owner_id", "asset_name"}
        ),
        indexes = @Index(name = "idx_asset_holdings_asset_name", columnList = "asset_name")
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetHoldingEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "owner_id", nullable = false, updatable = false)
    private UUID ownerId;

    @Column(name = "asset_name", nullable = false, length = 80)
    private String assetName;

    @Column(nullable = false, precision = 24, scale = 8)
    private BigDecimal quantity;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}