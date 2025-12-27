package dev.purppecat.quirksunleashed.api.world.ability;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.ApiStatus;

public class Abilities {
    public static final ResourceKey<Ability> NONE = create("none");
    public static final ResourceKey<Ability> HIGH_JUMP = create("high_jump");

    private static ResourceKey<Ability> create(String name) {
        return ResourceKey.create(QuirksUnleashedRegistries.ABILITY, QuirksUnleashed.modLoc(name));
    }

    @ApiStatus.Internal
    public static void bootstrap(BootstrapContext<Ability> context) {
        context.register(NONE, new PassiveEffectsAbility(HolderSet.empty(), 1));
        context.register(HIGH_JUMP, new PassiveEffectsAbility(HolderSet.direct(MobEffects.JUMP, MobEffects.SLOW_FALLING), 2));
    }
}
