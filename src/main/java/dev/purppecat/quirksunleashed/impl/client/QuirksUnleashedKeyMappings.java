package dev.purppecat.quirksunleashed.impl.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.impl.network.ServerboundToggleCombatMode;
import dev.purppecat.quirksunleashed.impl.network.ServerboundToggleQuirk;
import dev.thomasglasser.tommylib.api.client.ClientUtils;
import dev.thomasglasser.tommylib.api.client.ExtendedKeyMapping;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;

public class QuirksUnleashedKeyMappings {
    public static final String QUIRKS_UNLEASHED_CATEGORY = "key.categories.quirksunleashed";

    public static final ExtendedKeyMapping TOGGLE_COMBAT = register("toggle_combat", InputConstants.KEY_G, QUIRKS_UNLEASHED_CATEGORY, QuirksUnleashedKeyMappings::handleToggleCombat);
    public static final ExtendedKeyMapping ACTIVATE_ABILITY = register("activate_ability", InputConstants.KEY_R, QUIRKS_UNLEASHED_CATEGORY, QuirksUnleashedKeyMappings::handleActivateAbility);
    public static final ExtendedKeyMapping ACTIVATE_QUIRK = register("activate_quirk", InputConstants.KEY_C, QUIRKS_UNLEASHED_CATEGORY, QuirksUnleashedKeyMappings::handleActivateQuirk);

    public static ExtendedKeyMapping register(String name, int key, String category, Runnable onClick) {
        return ClientUtils.registerKeyMapping(QuirksUnleashed.modLoc(name), key, category, onClick);
    }

    public static ExtendedKeyMapping register(String name, int key, String category, Runnable onClick, Runnable onNoClick) {
        return ClientUtils.registerKeyMapping(QuirksUnleashed.modLoc(name), key, category, onClick, onNoClick);
    }

    private static void handleActivateAbility() {}

    private static void handleActivateQuirk() {
        TommyLibServices.NETWORK.sendToServer(ServerboundToggleQuirk.INSTANCE);
    }

    private static void handleToggleCombat() {
        TommyLibServices.NETWORK.sendToServer(ServerboundToggleCombatMode.INSTANCE);
    }

    public static void init() {}
}
