package dev.purppecat.quirksunleashed.api.world.combat;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

public class FightingStyles {
    /// Default
    public static final ResourceKey<FightingStyle> DEFAULT = create("default");

    private static ResourceKey<FightingStyle> create(String name) {
        return ResourceKey.create(QuirksUnleashedRegistries.FIGHTING_STYLE, QuirksUnleashed.modLoc(name));
    }

    @ApiStatus.Internal
    public static void bootstrap(BootstrapContext<FightingStyle> context) {
        String path = "fighting_styles/";
        context.register(DEFAULT, new FightingStyle(
                ResourceLocation.fromNamespaceAndPath(QuirksUnleashed.MOD_ID, DEFAULT.location().getPath() + "_" + "punch"),
                ResourceLocation.fromNamespaceAndPath(QuirksUnleashed.MOD_ID, DEFAULT.location().getPath() + "_" + "punch2"),
                ResourceLocation.fromNamespaceAndPath(QuirksUnleashed.MOD_ID, DEFAULT.location().getPath() + "_" + "punch3"),
                ResourceLocation.fromNamespaceAndPath(QuirksUnleashed.MOD_ID, DEFAULT.location().getPath() + "_" + "stance"),
                ResourceLocation.fromNamespaceAndPath(QuirksUnleashed.MOD_ID, DEFAULT.location().getPath() + "_" + "stance_sneak"),
                ResourceLocation.fromNamespaceAndPath(QuirksUnleashed.MOD_ID, DEFAULT.location().getPath() + "_" + "block"),
                ResourceLocation.fromNamespaceAndPath(QuirksUnleashed.MOD_ID, DEFAULT.location().getPath() + "_" + "counter"),
                ResourceLocation.fromNamespaceAndPath(QuirksUnleashed.MOD_ID, DEFAULT.location().getPath() + "_" + "heavy")));
    }
}
