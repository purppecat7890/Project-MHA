package dev.purppecat.quirksunleashed.api.core.registries;

import com.mojang.serialization.MapCodec;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.world.ability.Ability;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyle;
import dev.purppecat.quirksunleashed.api.world.quirk.Quirk;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class QuirksUnleashedRegistries {
    /**
     * Static registry holding {@link MapCodec}s for {@link Ability}s,
     * used in serializing the {@link QuirksUnleashedRegistries#ABILITY} registry.
     */
    public static final ResourceKey<Registry<MapCodec<? extends Ability>>> ABILITY_SERIALIZER = create("ability_serializer");
    /// Data-driven registry holding {@link Ability}s based on {@link QuirksUnleashedRegistries#ABILITY_SERIALIZER} entries.
    public static final ResourceKey<Registry<Ability>> ABILITY = create("ability");
    /// Data-driven registry holding {@link FightingStyle}es containing animation entries.
    public static final ResourceKey<Registry<FightingStyle>> FIGHTING_STYLE = create("fighting_style");
    /// Data-driven registry holding {@link Quirk}s containing {@link QuirksUnleashedRegistries#ABILITY} entries.
    public static final ResourceKey<Registry<Quirk>> QUIRK = create("quirk");

    private static <T> ResourceKey<Registry<T>> create(String name) {
        return ResourceKey.createRegistryKey(QuirksUnleashed.modLoc(name));
    }
}
