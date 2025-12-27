package dev.purppecat.quirksunleashed.api.world.ability;

import com.google.common.collect.ImmutableList;
import dev.purppecat.quirksunleashed.api.world.quirk.QuirkData;
import net.minecraft.nbt.CompoundTag;

/**
 * Holds relevant {@link Ability} information.
 *
 * @param powerLevel     The power level of the performer
 * @param powerActive    Whether the performer's power is active
 * @param storedEntities Entities stored in the ability performer's data
 */
public record AbilityData(int powerLevel, boolean powerActive, ImmutableList<CompoundTag> storedEntities) {
    public static AbilityData of(QuirkData data) {
        return new AbilityData(data.powerLevel(), data.powerActive(), data.storedEntities());
    }
}
