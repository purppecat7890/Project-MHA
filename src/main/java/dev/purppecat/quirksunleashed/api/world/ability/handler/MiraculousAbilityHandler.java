package dev.purppecat.quirksunleashed.api.world.ability.handler;

import dev.purppecat.quirksunleashed.api.world.quirk.Quirk;
import net.minecraft.core.Holder;

/**
 * An {@link AbilityHandler} for {@link Quirk} abilities.
 *
 * @param miraculous The {@link Quirk} of the performer
 */
public record MiraculousAbilityHandler(Holder<Quirk> miraculous) implements AbilityHandler {}
