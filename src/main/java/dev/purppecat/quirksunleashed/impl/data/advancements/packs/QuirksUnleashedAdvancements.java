package dev.purppecat.quirksunleashed.impl.data.advancements.packs;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.thomasglasser.tommylib.api.data.advancements.ExtendedAdvancementGenerator;
import java.util.function.BiConsumer;
import net.minecraft.core.HolderLookup;

public class QuirksUnleashedAdvancements extends ExtendedAdvancementGenerator {
    public QuirksUnleashedAdvancements(BiConsumer<String, String> lang) {
        super(QuirksUnleashed.MOD_ID, "miraculous", lang);
    }

    @Override
    public void generate(HolderLookup.Provider provider) {
    }
}
