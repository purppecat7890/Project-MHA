package dev.purppecat.quirksunleashed.api.world.quirk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.ability.Ability;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;

/**
 * Data-driven entity transformation provided by a {@link Quirk}.
 *
 * @param color            The color of the Miraculous and holder name
 * @param activeAbility    The {@link Ability} that provides the main power
 * @param passiveAbilities Passive {@link Ability}s
 */
public record Quirk(TextColor color, Holder<Ability> activeAbility, HolderSet<Ability> passiveAbilities) {
    public static final Codec<Quirk> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TextColor.CODEC.fieldOf("color").forGetter(Quirk::color),
            Ability.CODEC.fieldOf("active_ability").forGetter(Quirk::activeAbility),
            Ability.HOLDER_SET_CODEC.optionalFieldOf("passive_abilities", HolderSet.empty()).forGetter(Quirk::passiveAbilities)).apply(instance, Quirk::new));
    public static final Codec<Holder<Quirk>> CODEC = RegistryFixedCodec.create(QuirksUnleashedRegistries.QUIRK);

    public static final StreamCodec<RegistryFriendlyByteBuf, Quirk> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(TextColor.CODEC), Quirk::color,
            Ability.STREAM_CODEC, Quirk::activeAbility,
            Ability.HOLDER_SET_STREAM_CODEC, Quirk::passiveAbilities,
            Quirk::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Quirk>> STREAM_CODEC = ByteBufCodecs.holder(QuirksUnleashedRegistries.QUIRK, DIRECT_STREAM_CODEC);
}
