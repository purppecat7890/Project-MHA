package dev.purppecat.quirksunleashed.api.world.ability.context;

/// Can be passed to an {@link dev.purppecat.quirksunleashed.api.world.ability.Ability} for specialized behavior.
public interface AbilityContext {
    /// Passed to an advancement trigger when consumed.
    String advancementContext();
}
