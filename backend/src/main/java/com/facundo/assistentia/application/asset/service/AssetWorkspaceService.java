package com.facundo.assistentia.application.asset.service;

import com.facundo.assistentia.application.auth.service.DesktopSession;
import com.facundo.assistentia.domain.asset.model.AssetCatalog;
import com.facundo.assistentia.domain.asset.model.AssetHolding;
import com.facundo.assistentia.domain.asset.repository.AssetHoldingRepository;
import com.facundo.assistentia.domain.team.model.Team;
import com.facundo.assistentia.domain.user.model.User;
import com.facundo.assistentia.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetWorkspaceService {

    private final AssetHoldingRepository assetHoldingRepository;
    private final UserRepository userRepository;

    public List<String> getCatalogNames() {
        return AssetCatalog.names();
    }

    public AssetHoldingView saveHolding(DesktopSession session, String requestedAssetName, BigDecimal quantity) {
        if (session == null) {
            throw new IllegalArgumentException("Inicia sesion antes de registrar un activo.");
        }
        if (quantity == null || quantity.signum() < 0) {
            throw new IllegalArgumentException("La cantidad debe ser cero o mayor.");
        }

        String assetName = AssetCatalog.resolveName(requestedAssetName);
        AssetHolding holding = assetHoldingRepository.findByOwnerIdAndAssetName(session.userId(), assetName)
                .orElseGet(() -> AssetHolding.builder()
                        .id(UUID.randomUUID())
                        .ownerId(session.userId())
                        .assetName(assetName)
                        .build());

        holding.setQuantity(quantity.stripTrailingZeros());
        holding.setUpdatedAt(Instant.now());

        AssetHolding savedHolding = assetHoldingRepository.save(holding);
        return toView(savedHolding, Map.of(session.userId(), session));
    }

    public List<AssetHoldingView> getSharedHoldings() {
        Map<UUID, DesktopSession> members = userRepository.findAll().stream()
                .map(this::toSession)
                .collect(Collectors.toMap(DesktopSession::userId, Function.identity()));

        return assetHoldingRepository.findAll().stream()
                .map(holding -> toView(holding, members))
                .sorted(Comparator.comparing(AssetHoldingView::assetName)
                        .thenComparing(AssetHoldingView::ownerName))
                .toList();
    }

    private AssetHoldingView toView(AssetHolding holding, Map<UUID, DesktopSession> members) {
        DesktopSession owner = members.get(holding.getOwnerId());
        String ownerName = owner == null ? "Miembro no disponible" : owner.displayName();
        String ownerUsername = owner == null ? "-" : owner.username();

        return new AssetHoldingView(
                holding.getId(),
                holding.getAssetName(),
                holding.getQuantity(),
                ownerName,
                ownerUsername,
                holding.getUpdatedAt()
        );
    }

    private DesktopSession toSession(User user) {
        Team team = user.getTeam();
        String displayName = user.getLastName() == null || user.getLastName().isBlank()
                ? user.getFirstName()
                : user.getFirstName() + " " + user.getLastName();

        return new DesktopSession(
            user.getId(),
            user.getUsername(),
            displayName,
            user.getRole(),
            team == null ? null : team.getId(),
            team == null ? null : team.getSlug(),
            team == null ? null : team.getName()
        );
    }
}