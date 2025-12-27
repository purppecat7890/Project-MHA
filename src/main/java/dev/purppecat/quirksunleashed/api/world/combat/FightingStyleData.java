package dev.purppecat.quirksunleashed.api.world.combat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.thomasglasser.tommylib.api.util.TommyLibExtraStreamCodecs;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public record FightingStyleData(ResourceLocation punch1, ResourceLocation punch2, ResourceLocation punch3, ResourceLocation stance, ResourceLocation stanceSneak, ResourceLocation block, ResourceLocation counter, ResourceLocation heavy) {

    public static final Codec<FightingStyleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("punch1").forGetter((FightingStyleData o) -> o.punch1),
            ResourceLocation.CODEC.fieldOf("punch2").forGetter((FightingStyleData o) -> o.punch2),
            ResourceLocation.CODEC.fieldOf("punch3").forGetter((FightingStyleData o) -> o.punch3),
            ResourceLocation.CODEC.fieldOf("stance").forGetter((FightingStyleData o) -> o.stance),
            ResourceLocation.CODEC.fieldOf("stance_sneak").forGetter((FightingStyleData o) -> o.stanceSneak),
            ResourceLocation.CODEC.fieldOf("block").forGetter((FightingStyleData o) -> o.block),
            ResourceLocation.CODEC.fieldOf("counter").forGetter((FightingStyleData o) -> o.counter),
            ResourceLocation.CODEC.fieldOf("heavy").forGetter((FightingStyleData o) -> o.heavy)).apply(instance, FightingStyleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FightingStyleData> STREAM_CODEC = TommyLibExtraStreamCodecs.composite(
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyleData o) -> o.punch1,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyleData o) -> o.punch2,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyleData o) -> o.punch3,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyleData o) -> o.stance,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyleData o) -> o.stanceSneak,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyleData o) -> o.block,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyleData o) -> o.counter,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyleData o) -> o.heavy,
            FightingStyleData::new);
    public FightingStyleData(ResourceLocation punch1, ResourceLocation punch2, ResourceLocation punch3, ResourceLocation stance, ResourceLocation stanceSneak, ResourceLocation block, ResourceLocation counter, ResourceLocation heavy) {
        this.punch1 = punch1;
        this.punch2 = punch2;
        this.punch3 = punch3;
        this.stance = stance;
        this.stanceSneak = stanceSneak;
        this.block = block;
        this.counter = counter;
        this.heavy = heavy;
    }

    public FightingStyleData() {
        this(null, null, null, null, null, null, null, null);
    }

    public FightingStyleData(FightingStyle style) {
        this(
                style.punch1(),
                style.punch2(),
                style.punch3(),
                style.stance(),
                style.stanceSneak(),
                style.block(),
                style.counter(),
                style.heavy());
    }

    /**
     * Saves this {@link FightingStyleData} to the provided entity and syncs it.
     *
     * @param miraculous The miraculous to save this data for
     * @param entity     The entity to save this data to
     */
    public void save(Holder<FightingStyle> miraculous, Entity entity) {
        entity.getData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLES).put(entity, miraculous, this);
    }
}
