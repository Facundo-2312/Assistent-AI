package com.facundo.assistentia.domain.asset.model;

import java.util.List;
import java.util.Locale;

public final class AssetCatalog {

    private static final List<String> NAMES = List.of(
            "NETS",
            "HMAP COIN",
            "GICO COIN",
            "GAME GOS COIN",
            "REEX COIN",
            "HEXA COIN",
            "MLC COIN",
            "7PT COUPON",
            "ANTALLAGI COIN",
            "DOMINION COIN",
            "NLT COIN",
            "9PT COUPON",
            "INT COIN",
            "7PT PRO COUPON",
            "REEX.MINER",
            "PREFACTORY DX",
            "FACTORY.DRONEX",
            "FACTORY.DRONEX+"
    );

    private AssetCatalog() {
    }

    public static List<String> names() {
        return NAMES;
    }

    public static String resolveName(String requestedName) {
        if (requestedName == null) {
            throw new IllegalArgumentException("Selecciona un activo.");
        }

        String normalizedName = requestedName.trim().toLowerCase(Locale.ROOT);
        return NAMES.stream()
                .filter(name -> name.toLowerCase(Locale.ROOT).equals(normalizedName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El activo seleccionado no existe en el catalogo compartido."));
    }
}