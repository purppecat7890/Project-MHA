package dev.purppecat.quirksunleashed.api.core.registries;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyle;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class QuirksUnleashedRegistries {
    /// Data-driven registry holding {@link FightingStyle}es containing animation entries.
    public static final ResourceKey<Registry<FightingStyle>> FIGHTING_STYLE = create("fighting_style");

    private static <T> ResourceKey<Registry<T>> create(String name) {
        return ResourceKey.createRegistryKey(QuirksUnleashed.modLoc(name));
    }
}
