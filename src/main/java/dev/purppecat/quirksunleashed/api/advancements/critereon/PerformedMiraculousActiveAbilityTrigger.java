package dev.purppecat.quirksunleashed.api.advancements.critereon;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.purppecat.quirksunleashed.api.advancements.QuirksUnleashedCriteriaTriggers;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.quirk.Quirk;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;

public class PerformedMiraculousActiveAbilityTrigger extends SimpleCriterionTrigger<PerformedMiraculousActiveAbilityTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ResourceKey<Quirk> miraculous, String context) {
        this.trigger(player, instance -> instance.matches(miraculous, context));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ResourceKey<Quirk>> miraculous, Optional<List<String>> contexts) implements SimpleInstance {

        static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                ResourceKey.codec(QuirksUnleashedRegistries.QUIRK).optionalFieldOf("miraculous").forGetter(TriggerInstance::miraculous),
                Codec.STRING.listOf().optionalFieldOf("contexts").forGetter(TriggerInstance::contexts))
                .apply(instance, TriggerInstance::new));
        public static Criterion<TriggerInstance> performedActiveAbility() {
            return performedActiveAbility(Optional.empty(), Optional.empty(), Optional.empty());
        }

        public static Criterion<TriggerInstance> performedActiveAbility(ResourceKey<Quirk> miraculous, String... contexts) {
            return performedActiveAbility(Optional.empty(), Optional.of(miraculous), contexts.length == 0 ? Optional.empty() : Optional.of(ImmutableList.copyOf(contexts)));
        }

        public static Criterion<TriggerInstance> performedActiveAbility(Optional<ContextAwarePredicate> player, Optional<ResourceKey<Quirk>> miraculous, Optional<List<String>> contexts) {
            return QuirksUnleashedCriteriaTriggers.PERFORMED_MIRACULOUS_ACTIVE_ABILITY.get().createCriterion(new TriggerInstance(player, miraculous, contexts));
        }

        public boolean matches(ResourceKey<Quirk> miraculous, String context) {
            return this.miraculous.map(key -> key == miraculous).orElse(true) && this.contexts.map(contexts -> contexts.contains(context)).orElse(true);
        }
    }
}
