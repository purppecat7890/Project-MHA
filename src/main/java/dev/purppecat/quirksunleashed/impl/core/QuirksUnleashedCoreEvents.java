package dev.purppecat.quirksunleashed.impl.core;

import static com.zigythebird.playeranim.PlayerAnimLibMod.ANIMATION_LAYER_ID;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedBuiltInRegistries;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.ability.Ability;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyle;
import dev.purppecat.quirksunleashed.api.world.quirk.Quirk;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public class QuirksUnleashedCoreEvents {
    // Registration
    public static void onNewDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(QuirksUnleashedRegistries.QUIRK, Quirk.DIRECT_CODEC, Quirk.DIRECT_CODEC);
        event.dataPackRegistry(QuirksUnleashedRegistries.ABILITY, Ability.DIRECT_CODEC, Ability.DIRECT_CODEC);
        event.dataPackRegistry(QuirksUnleashedRegistries.FIGHTING_STYLE, FightingStyle.DIRECT_CODEC, FightingStyle.DIRECT_CODEC);
    }

    // Registration
    public static void onNewRegistry(NewRegistryEvent event) {
        event.register(QuirksUnleashedBuiltInRegistries.ABILITY_SERIALIZER);
    }

    // Set up
    public static void FMLClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ANIMATION_LAYER_ID, 1000,
                    player -> new PlayerAnimationController(player,
                            (controller, state, animSetter) -> PlayState.STOP));
        });
    }
}
