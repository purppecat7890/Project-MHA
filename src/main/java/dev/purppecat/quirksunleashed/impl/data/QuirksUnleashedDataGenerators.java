package dev.purppecat.quirksunleashed.impl.data;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyles;
import dev.purppecat.quirksunleashed.impl.data.advancements.QuirksUnleashedAdvancementProvider;
import dev.purppecat.quirksunleashed.impl.data.lang.QuirksUnleashedEnUsLanguageProvider;
import dev.thomasglasser.tommylib.api.data.DataGenerationUtils;
import net.minecraft.core.RegistrySetBuilder;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class QuirksUnleashedDataGenerators {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(QuirksUnleashedRegistries.FIGHTING_STYLE, FightingStyles::bootstrap);

    public static void onGatherData(GatherDataEvent event) {
        event.createDatapackRegistryObjects(BUILDER);
        DataGenerationUtils.createRegistryDumpReport(event, QuirksUnleashed.MOD_ID);

        // Common
        DataGenerationUtils.createLangDependent(event, QuirksUnleashedEnUsLanguageProvider::new, QuirksUnleashedAdvancementProvider::new);

    }
}
