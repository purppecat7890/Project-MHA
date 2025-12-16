package dev.purppecat.quirksunleashed.api.world.combat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.thomasglasser.tommylib.api.util.TommyLibExtraStreamCodecs;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;

public record FightingStyle(ResourceLocation punch1, ResourceLocation punch2, ResourceLocation punch3, ResourceLocation stance, ResourceLocation stanceSneak, ResourceLocation block, ResourceLocation counter, ResourceLocation heavy) {
    public static final Codec<FightingStyle> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("punch1").forGetter(FightingStyle::punch1),
            ResourceLocation.CODEC.fieldOf("punch2").forGetter(FightingStyle::punch2),
            ResourceLocation.CODEC.fieldOf("punch3").forGetter(FightingStyle::punch3),
            ResourceLocation.CODEC.fieldOf("stance").forGetter(FightingStyle::stance),
            ResourceLocation.CODEC.fieldOf("stance_sneak").forGetter(FightingStyle::stanceSneak),
            ResourceLocation.CODEC.fieldOf("block").forGetter(FightingStyle::block),
            ResourceLocation.CODEC.fieldOf("counter").forGetter(FightingStyle::counter),
            ResourceLocation.CODEC.fieldOf("heavy").forGetter(FightingStyle::heavy)).apply(instance, FightingStyle::new));

    public static final Codec<Holder<FightingStyle>> CODEC = RegistryFixedCodec.create(QuirksUnleashedRegistries.FIGHTING_STYLE);

    public static final StreamCodec<RegistryFriendlyByteBuf, FightingStyle> DIRECT_STREAM_CODEC = TommyLibExtraStreamCodecs.composite(
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyle::punch1,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyle::punch2,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyle::punch3,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyle::stance,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyle::stanceSneak,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyle::block,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyle::counter,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), FightingStyle::heavy,
            FightingStyle::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<FightingStyle>> STREAM_CODEC = ByteBufCodecs.holder(QuirksUnleashedRegistries.FIGHTING_STYLE, DIRECT_STREAM_CODEC);
}
