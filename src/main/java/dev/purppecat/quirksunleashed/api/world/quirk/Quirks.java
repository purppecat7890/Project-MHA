package dev.purppecat.quirksunleashed.api.world.quirk;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.ability.Abilities;
import dev.purppecat.quirksunleashed.api.world.ability.Ability;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

public class Quirks {
    /// Powerless.
    public static final ResourceKey<Quirk> QUIRKLESS = create("quirkless");
    public static final ResourceKey<Quirk> LEAP = create("leap");

    private static ResourceKey<Quirk> create(String name) {
        return ResourceKey.create(QuirksUnleashedRegistries.QUIRK, QuirksUnleashed.modLoc(name));
    }

    @ApiStatus.Internal
    public static void bootstrap(BootstrapContext<Quirk> context) {
        HolderGetter<Ability> abilities = context.lookup(QuirksUnleashedRegistries.ABILITY);

        context.register(QUIRKLESS, new Quirk(
                TextColor.fromRgb(0xFFFFFF),
                abilities.getOrThrow(Abilities.NONE),
                HolderSet.direct(abilities.getOrThrow(Abilities.NONE))));
        context.register(LEAP, new Quirk(
                TextColor.fromRgb(0x515166),
                abilities.getOrThrow(Abilities.NONE),
                HolderSet.direct(abilities.getOrThrow(Abilities.HIGH_JUMP)))
        );
    }
}
