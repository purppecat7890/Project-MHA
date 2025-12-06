package dev.purppecat.quirksunleashed.impl.world.combat;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

public class FightingStyles {
    /// Default
    public static final ResourceKey<FightingStyle> DEFAULT = create("default");

    private static ResourceKey<FightingStyle> create(String name) {
        return ResourceKey.create(QuirksUnleashedRegistries.FIGHTING_STYLE, QuirksUnleashed.modLoc(name));
    }

    @ApiStatus.Internal
    public static void bootstrap(BootstrapContext<FightingStyle> context) {
        context.register(DEFAULT, new FightingStyle(
                QuirksUnleashed.modLoc("punch"),
                QuirksUnleashed.modLoc("punch2"),
                QuirksUnleashed.modLoc("punch3"),
                QuirksUnleashed.modLoc("stance"),
                QuirksUnleashed.modLoc("stance_sneak"),
                QuirksUnleashed.modLoc("block"),
                QuirksUnleashed.modLoc("counter"),
                QuirksUnleashed.modLoc("heavy")));
    }
}
