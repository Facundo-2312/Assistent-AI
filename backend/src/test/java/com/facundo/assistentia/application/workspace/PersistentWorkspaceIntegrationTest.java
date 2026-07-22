package com.facundo.assistentia.application.workspace;

import com.facundo.assistentia.application.asset.service.AssetHoldingView;
import com.facundo.assistentia.application.asset.service.AssetWorkspaceService;
import com.facundo.assistentia.application.auth.service.DesktopAuthenticationService;
import com.facundo.assistentia.application.auth.service.DesktopSession;
import com.facundo.assistentia.domain.user.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:desktop-workspace;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.flyway.enabled=false"
        }
)
class PersistentWorkspaceIntegrationTest {

    @Autowired
    private DesktopAuthenticationService authenticationService;

    @Autowired
    private AssetWorkspaceService assetWorkspaceService;

    @Test
    void shouldStoreARegisteredMemberAndTheirAssetHolding() {
        DesktopSession session = authenticationService.register(
                "equipoactivo",
                "Equipo Activo",
                "Password123!"
        );

        AssetHoldingView savedHolding = assetWorkspaceService.saveHolding(
                session,
                "FACTORY.DRONEX",
                new BigDecimal("2")
        );

        assertThat(session.role()).isEqualTo(UserRole.ADMIN);
        assertThat(authenticationService.authenticate("equipoactivo", "Password123!")).contains(session);
        assertThat(assetWorkspaceService.getSharedHoldings())
                                .singleElement()
                                .satisfies(holding -> {
                                        assertThat(holding.id()).isEqualTo(savedHolding.id());
                                        assertThat(holding.assetName()).isEqualTo("FACTORY.DRONEX");
                                        assertThat(holding.quantity()).isEqualByComparingTo("2");
                                        assertThat(holding.ownerName()).isEqualTo("Equipo Activo");
                                        assertThat(holding.ownerUsername()).isEqualTo("equipoactivo");
                                });
    }
}