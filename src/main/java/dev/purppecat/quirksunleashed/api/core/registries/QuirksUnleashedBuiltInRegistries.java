package dev.purppecat.quirksunleashed.api.core.registries;

import com.mojang.serialization.MapCodec;
import dev.purppecat.quirksunleashed.api.world.ability.Ability;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.ApiStatus;

public class QuirksUnleashedBuiltInRegistries {
    /// The static registry for {@link QuirksUnleashedRegistries#ABILITY_SERIALIZER}.
    public static final Registry<MapCodec<? extends Ability>> ABILITY_SERIALIZER = new RegistryBuilder<>(QuirksUnleashedRegistries.ABILITY_SERIALIZER).create();

    @ApiStatus.Internal
    public static void init() {}
}
