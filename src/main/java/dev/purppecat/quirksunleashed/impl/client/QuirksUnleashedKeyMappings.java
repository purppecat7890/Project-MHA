package dev.purppecat.quirksunleashed.impl.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.impl.network.ServerboundToggleCombatMode;
import dev.thomasglasser.tommylib.api.client.ClientUtils;
import dev.thomasglasser.tommylib.api.client.ExtendedKeyMapping;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;

public class QuirksUnleashedKeyMappings {
    public static final String QUIRKS_UNLEASHED_CATEGORY = "key.categories.quirksunleashed";

    public static final ExtendedKeyMapping TOGGLE_COMBAT = register("toggle_combat", InputConstants.KEY_G, QUIRKS_UNLEASHED_CATEGORY, QuirksUnleashedKeyMappings::handleToggleCombat);

    public static ExtendedKeyMapping register(String name, int key, String category, Runnable onClick) {
        return ClientUtils.registerKeyMapping(QuirksUnleashed.modLoc(name), key, category, onClick);
    }

    public static ExtendedKeyMapping register(String name, int key, String category, Runnable onClick, Runnable onNoClick) {
        return ClientUtils.registerKeyMapping(QuirksUnleashed.modLoc(name), key, category, onClick, onNoClick);
    }

    private static void handleToggleCombat() {
        TommyLibServices.NETWORK.sendToServer(ServerboundToggleCombatMode.INSTANCE);
    }

    public static void init() {}
}
