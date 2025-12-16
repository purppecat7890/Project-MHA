package dev.purppecat.quirksunleashed.api.world.combat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.thomasglasser.tommylib.api.util.TommyLibExtraStreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record FightingStyleData(ResourceLocation punch1, ResourceLocation punch2, ResourceLocation punch3, ResourceLocation stance, ResourceLocation stanceSneak, ResourceLocation block, ResourceLocation counter, ResourceLocation heavy) {

    public static final Codec<FightingStyleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("punch1").forGetter(FightingStyleData::punch1),
            ResourceLocation.CODEC.fieldOf("punch2").forGetter(FightingStyleData::punch2),
            ResourceLocation.CODEC.fieldOf("punch3").forGetter(FightingStyleData::punch3),
            ResourceLocation.CODEC.fieldOf("stance").forGetter(FightingStyleData::stance),
            ResourceLocation.CODEC.fieldOf("stance_sneak").forGetter(FightingStyleData::stanceSneak),
            ResourceLocation.CODEC.fieldOf("block").forGetter(FightingStyleData::block),
            ResourceLocation.CODEC.fieldOf("counter").forGetter(FightingStyleData::counter),
            ResourceLocation.CODEC.fieldOf("heavy").forGetter(FightingStyleData::heavy)).apply(instance, FightingStyleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FightingStyleData> STREAM_CODEC = TommyLibExtraStreamCodecs.composite(
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyleData::punch1,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyleData::punch2,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyleData::punch3,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyleData::stance,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyleData::stanceSneak,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyleData::block,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyleData::counter,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyleData::heavy,
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
        this(QuirksUnleashed.modLoc("punch"), QuirksUnleashed.modLoc("punch2"), QuirksUnleashed.modLoc("punch3"), QuirksUnleashed.modLoc("stance"), QuirksUnleashed.modLoc("stance_sneak"), QuirksUnleashed.modLoc("block"), QuirksUnleashed.modLoc("counter"), QuirksUnleashed.modLoc("heavy"));
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
                style.heavy()
        );
    }

}
