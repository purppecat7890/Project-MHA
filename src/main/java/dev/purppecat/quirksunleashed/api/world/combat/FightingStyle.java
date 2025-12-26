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
            ResourceLocation.CODEC.fieldOf("punch1").forGetter((FightingStyle o) -> o.punch1),
            ResourceLocation.CODEC.fieldOf("punch2").forGetter((FightingStyle o) -> o.punch2),
            ResourceLocation.CODEC.fieldOf("punch3").forGetter((FightingStyle o) -> o.punch3),
            ResourceLocation.CODEC.fieldOf("stance").forGetter((FightingStyle o) -> o.stance),
            ResourceLocation.CODEC.fieldOf("stance_sneak").forGetter((FightingStyle o) -> o.stanceSneak),
            ResourceLocation.CODEC.fieldOf("block").forGetter((FightingStyle o) -> o.block),
            ResourceLocation.CODEC.fieldOf("counter").forGetter((FightingStyle o) -> o.counter),
            ResourceLocation.CODEC.fieldOf("heavy").forGetter((FightingStyle o) -> o.heavy)).apply(instance, FightingStyle::new));

    public static final Codec<Holder<FightingStyle>> CODEC = RegistryFixedCodec.create(QuirksUnleashedRegistries.FIGHTING_STYLE);

    public static final StreamCodec<RegistryFriendlyByteBuf, FightingStyle> DIRECT_STREAM_CODEC = TommyLibExtraStreamCodecs.composite(
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyle o) -> o.punch1,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyle o) -> o.punch2,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyle o) -> o.punch3,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyle o) -> o.stance,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyle o) -> o.stanceSneak,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyle o) -> o.block,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyle o) -> o.counter,
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC), (FightingStyle o) -> o.heavy,
            FightingStyle::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<FightingStyle>> STREAM_CODEC = ByteBufCodecs.holder(QuirksUnleashedRegistries.FIGHTING_STYLE, DIRECT_STREAM_CODEC);
}
