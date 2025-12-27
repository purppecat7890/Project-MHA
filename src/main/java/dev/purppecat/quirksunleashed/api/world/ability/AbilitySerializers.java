package dev.purppecat.quirksunleashed.api.world.ability;

import com.mojang.serialization.MapCodec;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

public class AbilitySerializers {
    private static final DeferredRegister<MapCodec<? extends Ability>> ABILITIES = DeferredRegister.create(QuirksUnleashedRegistries.ABILITY_SERIALIZER, QuirksUnleashed.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Ability>, MapCodec<PassiveEffectsAbility>> PASSIVE_EFFECTS = ABILITIES.register("passive_effects", () -> PassiveEffectsAbility.CODEC);
    public static final DeferredHolder<MapCodec<? extends Ability>, MapCodec<PassiveFiniteEffectsAbility>> FINITE_PASSIVE_EFFECTS = ABILITIES.register("finite_passive_effects", () -> PassiveFiniteEffectsAbility.CODEC);

    @ApiStatus.Internal
    public static void init() {}
}
