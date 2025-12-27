package dev.purppecat.quirksunleashed.api.advancements;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.advancements.critereon.PerformedMiraculousActiveAbilityTrigger;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.ApiStatus;

public class QuirksUnleashedCriteriaTriggers {
    private static final DeferredRegister<CriterionTrigger<?>> CRITERION_TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, QuirksUnleashed.MOD_ID);

    // Miraculous
    public static final DeferredHolder<CriterionTrigger<?>, PerformedMiraculousActiveAbilityTrigger> PERFORMED_MIRACULOUS_ACTIVE_ABILITY = CRITERION_TRIGGERS.register("performed_miraculous_active_ability", PerformedMiraculousActiveAbilityTrigger::new);

    @ApiStatus.Internal
    public static void init() {}
}
