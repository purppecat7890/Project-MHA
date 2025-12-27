package dev.purppecat.quirksunleashed.api.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record QuirkEffect (int amplifier, boolean toggleable) {
    public static final Codec<QuirkEffect> SIMPLE_CODEC = ExtraCodecs.intRange(0, 255).xmap(QuirkEffect::new, QuirkEffect::amplifier);
    public static final Codec<QuirkEffect> CODEC = Codec.withAlternative(RecordCodecBuilder.create(instance -> instance.group(
                    ExtraCodecs.intRange(0, 255).fieldOf("amplifier").forGetter(QuirkEffect::amplifier),
                    Codec.BOOL.optionalFieldOf("toggleable", false).forGetter(QuirkEffect::toggleable)).apply(instance, QuirkEffect::new)),
            SIMPLE_CODEC);

    public QuirkEffect(int amplifier) {
        this(amplifier, false);
    }
}
