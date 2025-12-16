package dev.purppecat.quirksunleashed.impl.core;

import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyle;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class QuirksUnleashedCoreEvents {
    // Registration
    public static void onNewDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(QuirksUnleashedRegistries.FIGHTING_STYLE, FightingStyle.DIRECT_CODEC, FightingStyle.DIRECT_CODEC);
    }
}
