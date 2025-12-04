package dev.purppecat.quirksunleashed;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(QuirksUnleashed.MOD_ID)
public class QuirksUnleashed {
    public static final String MOD_ID = "quirksunleashed";
    public static final String MOD_NAME = "Quirks Unleashed";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final int API_VERSION = 1;

    public QuirksUnleashed(IEventBus modEventBus) {}

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
