package com.facundo.assistentia.application.asset.service;

import com.facundo.assistentia.application.auth.service.DesktopAuthenticationService;
import com.facundo.assistentia.application.auth.service.DesktopSession;
import com.facundo.assistentia.application.auth.service.WorkspaceAccessService;
import com.facundo.assistentia.domain.asset.model.AssetHolding;
import com.facundo.assistentia.domain.asset.repository.AssetHoldingRepository;
import com.facundo.assistentia.infrastructure.persistence.inmemory.TeamInMemoryRepository;
import com.facundo.assistentia.infrastructure.persistence.inmemory.UserInMemoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AssetWorkspaceServiceTest {

    @Test
    void shouldShareEveryMemberAssetHoldingAndPreserveTheAssetCatalogNames() {
        UserInMemoryRepository userRepository = new UserInMemoryRepository();
        WorkspaceAccessService workspaceAccessService = new WorkspaceAccessService(
            userRepository,
            new TeamInMemoryRepository(),
            new BCryptPasswordEncoder()
        );
        DesktopAuthenticationService authenticationService = new DesktopAuthenticationService(
            workspaceAccessService
        );
        DesktopSession facundo = authenticationService.register("facundo", "Facundo", "Password123!");
        DesktopSession maria = authenticationService.register("maria", "Maria", "Password123!");

        AssetWorkspaceService assetService = new AssetWorkspaceService(
                new InMemoryAssetHoldingRepository(),
                userRepository
        );

        assetService.saveHolding(facundo, "NETS", new BigDecimal("160.6445"));
        assetService.saveHolding(maria, "HMAP COIN", new BigDecimal("0.403"));

        List<AssetHoldingView> holdings = assetService.getSharedHoldings();

        assertThat(assetService.getCatalogNames()).contains("NETS", "HMAP COIN", "REEX.MINER", "FACTORY.DRONEX");
        assertThat(holdings).extracting(AssetHoldingView::assetName).containsExactly("HMAP COIN", "NETS");
        assertThat(holdings).extracting(AssetHoldingView::ownerName).containsExactly("Maria", "Facundo");
    }

    private static final class InMemoryAssetHoldingRepository implements AssetHoldingRepository {

        private final List<AssetHolding> holdings = new ArrayList<>();

        @Override
        public Optional<AssetHolding> findByOwnerIdAndAssetName(UUID ownerId, String assetName) {
            return holdings.stream()
                    .filter(holding -> holding.getOwnerId().equals(ownerId))
                    .filter(holding -> holding.getAssetName().equalsIgnoreCase(assetName))
                    .findFirst();
        }

        @Override
        public List<AssetHolding> findAll() {
            return List.copyOf(holdings);
        }

        @Override
        public AssetHolding save(AssetHolding holding) {
            holdings.removeIf(existing -> existing.getId().equals(holding.getId()));
            holdings.add(holding);
            return holding;
        }
    }
}