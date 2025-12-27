package dev.purppecat.quirksunleashed.api.world.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.purppecat.quirksunleashed.api.world.ability.context.AbilityContext;
import dev.purppecat.quirksunleashed.api.world.ability.handler.AbilityHandler;
import dev.purppecat.quirksunleashed.api.world.entity.QuirksUnleashedEntityUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public record PassiveFiniteEffectsAbility(HolderSet<MobEffect> effects,int duration, int startAmplifier) implements Ability {
    public static final MapCodec<PassiveFiniteEffectsAbility> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            HolderSetCodec.create(Registries.MOB_EFFECT, MobEffect.CODEC, false).fieldOf("effects").forGetter(PassiveFiniteEffectsAbility::effects),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("start_amplifier", 0).forGetter(PassiveFiniteEffectsAbility::startAmplifier),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("duration", 200).forGetter(PassiveFiniteEffectsAbility::duration)).apply(instance, PassiveFiniteEffectsAbility::new));

    @Override
    public State perform(AbilityData data, ServerLevel level, LivingEntity performer, AbilityHandler handler, @Nullable AbilityContext context) {
        if (context == null) {
            for (Holder<MobEffect> effect : effects) {
                if (!performer.hasEffect(effect)) {
                    QuirksUnleashedEntityUtils.applyFiniteHiddenEffect(performer, effect, duration,startAmplifier + (data.powerLevel() / 10));
                }
            }
        }
        return State.PASS;
    }

    @Override
    public void add(AbilityData data, ServerLevel level, LivingEntity performer) {
        if (effects.size() > 0) {
            effects.forEach(effect -> QuirksUnleashedEntityUtils.applyFiniteHiddenEffect(performer, effect, duration,startAmplifier + (data.powerLevel() / 10)));
        }
    }

    @Override
    public void remove(AbilityData data, ServerLevel level, LivingEntity performer) {
        if (effects.size() > 0) {
            effects.forEach(performer::removeEffect);
        }
    }

    @Override
    public MapCodec<? extends Ability> codec() {
        return AbilitySerializers.FINITE_PASSIVE_EFFECTS.get();
    }
}
