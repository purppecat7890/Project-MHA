package dev.purppecat.quirksunleashed;

import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedBuiltInRegistries;
import dev.purppecat.quirksunleashed.api.world.ability.AbilitySerializers;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.impl.client.QuirksUnleashedKeyMappings;
import dev.purppecat.quirksunleashed.impl.commands.QuirksUnleashedCommandEvents;
import dev.purppecat.quirksunleashed.impl.core.QuirksUnleashedCoreEvents;
import dev.purppecat.quirksunleashed.impl.data.QuirksUnleashedDataGenerators;
import dev.purppecat.quirksunleashed.impl.network.QuirksUnleashedPayloads;
import dev.purppecat.quirksunleashed.impl.world.entity.QuirksUnleashedEntityEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(QuirksUnleashed.MOD_ID)
public class QuirksUnleashed {
    public static final String MOD_ID = "quirksunleashed";
    public static final String MOD_NAME = "Quirks Unleashed";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final int API_VERSION = 1;

    public QuirksUnleashed(IEventBus modEventBus) {
        AbilitySerializers.init();
        QuirksUnleashedBuiltInRegistries.init();
        QuirksUnleashedKeyMappings.init();
        QuirksUnleashedAttachmentTypes.init();

        modEventBus.addListener(QuirksUnleashedDataGenerators::onGatherData);
        modEventBus.addListener(QuirksUnleashedPayloads::onRegisterPackets);
        modEventBus.addListener(QuirksUnleashedCoreEvents::onNewDataPackRegistry);
        modEventBus.addListener(QuirksUnleashedCoreEvents::FMLClientSetup);
        modEventBus.addListener(QuirksUnleashedCoreEvents::onNewRegistry);

        NeoForge.EVENT_BUS.addListener(QuirksUnleashedEntityEvents::onEntityInteract);
        NeoForge.EVENT_BUS.addListener(QuirksUnleashedCommandEvents::onCommandsRegister);
        NeoForge.EVENT_BUS.addListener(QuirksUnleashedEntityEvents::OnHitEntity);
        NeoForge.EVENT_BUS.addListener(QuirksUnleashedEntityEvents::onPostEntityTick);
        NeoForge.EVENT_BUS.addListener(QuirksUnleashedEntityEvents::onEntityJoinedLevel);
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    /**
     * Converts a {@link ResourceKey} to a language key.
     *
     * @param key The key to convert
     * @return The language key
     */
    public static String toLanguageKey(ResourceKey<?> key) {
        return key.location().toLanguageKey(key.registry().getPath());
    }
}
