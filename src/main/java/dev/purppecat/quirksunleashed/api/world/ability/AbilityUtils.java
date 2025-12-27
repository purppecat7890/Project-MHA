package dev.purppecat.quirksunleashed.api.world.ability;

import dev.purppecat.quirksunleashed.api.world.ability.context.AbilityContext;
import dev.purppecat.quirksunleashed.api.world.ability.context.BlockAbilityContext;
import dev.purppecat.quirksunleashed.api.world.ability.context.EntityAbilityContext;
import dev.purppecat.quirksunleashed.api.world.ability.handler.AbilityHandler;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.api.world.quirk.QuirksData;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class AbilityUtils {
    /**
     * Performs all provided abilities with the given context until one consumes.
     *
     * @param level            The level to perform the abilities in
     * @param performer        The performer of the abilities
     * @param data             The relevant performer {@link AbilityData}
     * @param handler          The relevant {@link dev.purppecat.quirksunleashed.api.world.ability.handler.AbilityHandler}
     * @param context          The context of the abilities
     * @param passiveAbilities The abilities to perform
     * @return The resulting {@link Ability.State} of the performed abilities
     */
    public static Ability.State performPassiveAbilities(ServerLevel level, LivingEntity performer, AbilityData data, AbilityHandler handler, @Nullable AbilityContext context, HolderSet<Ability> passiveAbilities) {
        for (Holder<Ability> ability : passiveAbilities) {
            Ability.State state = ability.value().perform(data, level, performer, handler, context);
            if (state.shouldStop()) {
                return state;
            }
        }
        return Ability.State.PASS;
    }

    /**
     * Performs the provided ability if power is active.
     *
     * @param level         The level to perform the ability in
     * @param performer     The performer of the ability
     * @param abilityData   The relevant performer {@link AbilityData}
     * @param handler       The relevant {@link AbilityHandler}
     * @param context       The context of the ability
     * @param activeAbility The ability to perform
     * @return The resulting {@link Ability.State} of the performed ability,
     *         or {@link Ability.State#CANCEL} if power is inactive.
     */
    public static Ability.State performActiveAbility(ServerLevel level, LivingEntity performer, AbilityData abilityData, AbilityHandler handler, @Nullable AbilityContext context, Optional<Holder<Ability>> activeAbility) {
        if (abilityData.powerActive()) {
            return activeAbility.map(ability -> ability.value().perform(abilityData, level, performer, handler, context)).orElse(Ability.State.CANCEL);
        }
        return Ability.State.CANCEL;
    }

    /**
     * Performs {@link dev.purppecat.quirksunleashed.api.world.quirk.Quirk} abilities with an {@link dev.purppecat.quirksunleashed.api.world.ability.context.EntityAbilityContext} of the provided target.
     *
     * @param level     The level to perform the abilities in
     * @param performer The performer of the abilities
     * @param target    The target to make the context for
     */
    public static void performEntityAbilities(ServerLevel level, LivingEntity performer, Entity target) {
        performAbilitiesInternal(level, performer, new EntityAbilityContext(target));
    }

    /**
     * Performs {@link dev.purppecat.quirksunleashed.api.world.quirk.Quirk} abilities with a {@link dev.purppecat.quirksunleashed.api.world.ability.context.BlockAbilityContext} of the provided position.
     *
     * @param level     The level to perform the abilities in
     * @param performer The performer of the abilities
     * @param pos       The position to make the context for
     */
    public static void performBlockAbilities(ServerLevel level, LivingEntity performer, BlockPos pos) {
        performAbilitiesInternal(level, performer, new BlockAbilityContext(pos));
    }

    private static void performAbilitiesInternal(ServerLevel level, LivingEntity performer, @Nullable AbilityContext context) {
        QuirksData miraculousesData = performer.getData(QuirksUnleashedAttachmentTypes.QUIRKS);
        miraculousesData.getQuirks().forEach(miraculous -> miraculousesData.get(miraculous).performAbilities(level, performer, miraculous, context));
    }
}
