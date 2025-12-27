package dev.purppecat.quirksunleashed.api.datamaps;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class QuirksUnleashedDataMaps {
    // Miraculous Buffs
    /// Multiplied by {@link dev.purppecat.quirksunleashed.api.world.quirk.QuirkData#powerLevel()} when applied to a quirk holder.
    public static final DataMapType<MobEffect, QuirkEffect> QUIRK_EFFECTS = DataMapType.builder(QuirksUnleashed.modLoc("quirk_effects"), Registries.MOB_EFFECT, QuirkEffect.CODEC).build();
    /// Multiplied by {@link dev.purppecat.quirksunleashed.api.world.quirk.QuirkData#powerLevel()} when applied to a quirk holder.
    public static final DataMapType<Attribute, ModifierSettings> QUIRK_ATTRIBUTE_MODIFIERS = DataMapType.builder(QuirksUnleashed.modLoc("quirk_attribute_modifiers"), Registries.ATTRIBUTE, ModifierSettings.CODEC).build();

}
