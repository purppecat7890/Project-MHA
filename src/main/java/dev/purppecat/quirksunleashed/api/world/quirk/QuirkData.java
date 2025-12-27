package dev.purppecat.quirksunleashed.api.world.quirk;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.purppecat.quirksunleashed.api.advancements.QuirksUnleashedCriteriaTriggers;
import dev.purppecat.quirksunleashed.api.datamaps.QuirksUnleashedDataMaps;
import dev.purppecat.quirksunleashed.api.world.ability.Ability;
import dev.purppecat.quirksunleashed.api.world.ability.AbilityData;
import dev.purppecat.quirksunleashed.api.world.ability.AbilityUtils;
import dev.purppecat.quirksunleashed.api.world.ability.context.AbilityContext;
import dev.purppecat.quirksunleashed.api.world.ability.handler.AbilityHandler;
import dev.purppecat.quirksunleashed.api.world.ability.handler.MiraculousAbilityHandler;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.api.world.entity.QuirksUnleashedEntityUtils;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Performs functions of a {@link Quirk}.
 *
 * @param activated      Whether the entity is currently activated
 * @param powerLevel     The current power level of the miraculous
 * @param powerActive    Whether the miraculous holder's power is active
 * @param storedEntities Any entities currently stored in the miraculous
 * @param buffsActive    Whether buffs are currently active
 */
public record QuirkData(boolean activated, Optional<TransformationState> quirkState, int powerLevel, boolean powerActive, ImmutableList<CompoundTag> storedEntities, boolean buffsActive) {

    public static final String NAME_NOT_SET = "miraculous_data.name.not_set";
    public static final int MAX_POWER_LEVEL = 100;

    public static final Codec<QuirkData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("activated").forGetter(QuirkData::activated),
            TransformationState.CODEC.optionalFieldOf("transformation_state").forGetter(QuirkData::quirkState),
            Codec.INT.fieldOf("power_level").forGetter(QuirkData::powerLevel),
            Codec.BOOL.fieldOf("power_active").forGetter(QuirkData::powerActive),
            CompoundTag.CODEC.listOf().xmap(ImmutableList::copyOf, Function.identity()).optionalFieldOf("stored_entities", ImmutableList.of()).forGetter(QuirkData::storedEntities),
            Codec.BOOL.fieldOf("buffs_active").forGetter(QuirkData::buffsActive)).apply(instance, QuirkData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, QuirkData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, QuirkData::activated,
            ByteBufCodecs.optional(TransformationState.STREAM_CODEC), QuirkData::quirkState,
            ByteBufCodecs.VAR_INT, QuirkData::powerLevel,
            ByteBufCodecs.BOOL, QuirkData::powerActive,
            ByteBufCodecs.COMPOUND_TAG.apply(ByteBufCodecs.list()).map(ImmutableList::copyOf, Function.identity()), QuirkData::storedEntities,
            ByteBufCodecs.BOOL, QuirkData::buffsActive,
            QuirkData::new);

    public QuirkData(boolean activated, Optional<TransformationState> quirkState, int powerLevel, boolean powerActive, ImmutableList<CompoundTag> storedEntities, boolean buffsActive) {
        this.activated = activated;
        this.quirkState = quirkState;
        this.powerLevel = Math.clamp(powerLevel, 0, MAX_POWER_LEVEL);
        this.powerActive = powerActive;
        this.storedEntities = storedEntities;
        this.buffsActive = buffsActive;
    }

    public QuirkData() {
        this(false, Optional.empty(), 0, false, ImmutableList.of(), false);
    }

    private boolean hasLimitedPower() {
        return powerLevel < MAX_POWER_LEVEL;
    }

    /**
     * Performs all abilities provided by the provided {@link Quirk} with the provided {@link AbilityContext}.
     *
     * @param level      The level to perform the abilities in
     * @param entity     The performer of the abilities
     * @param miraculous The miraculous to fetch abilities from
     * @param context    The context to perform the abilities with. If {@code null}, abilities will be performed passively
     */
    public void performAbilities(ServerLevel level, LivingEntity entity, Holder<Quirk> miraculous, @Nullable AbilityContext context) {
        AbilityData data = AbilityData.of(this);
        AbilityHandler handler = new MiraculousAbilityHandler(miraculous);
        Ability.State state = AbilityUtils.performPassiveAbilities(level, entity, data, handler, context, miraculous.value().passiveAbilities());
        if (powerActive) {
            if (state.shouldStop()) {
                withPowerActive(false).save(miraculous, entity);
                state = AbilityUtils.performActiveAbility(level, entity, data, handler, context, Optional.of(miraculous.value().activeAbility()));
                if (state.isSuccess()) {
                    if (context != null && entity instanceof ServerPlayer player) {
                        QuirksUnleashedCriteriaTriggers.PERFORMED_MIRACULOUS_ACTIVE_ABILITY.get().trigger(player, miraculous.getKey(), context.advancementContext());
                    }
                } else if (state.shouldStop()) {
                    withPowerActive(false).save(miraculous, entity);
                }
            }
        }
    }

    @ApiStatus.Internal
    public void tick(LivingEntity entity, ServerLevel level, Holder<Quirk> quirk) {
        quirkState.ifPresentOrElse(state -> {
        }, () -> {
            if (entity.getData(QuirksUnleashedAttachmentTypes.QUIRK_ACTIVATED) || activated) {
                level.registryAccess().registryOrThrow(Registries.MOB_EFFECT).getDataMap(QuirksUnleashedDataMaps.QUIRK_EFFECTS).forEach((key, miraculousEffect) -> {
                    Holder<MobEffect> effect = level.holderOrThrow(key);
                    if (!entity.hasEffect(effect)) {
                        QuirksUnleashedEntityUtils.applyInfiniteHiddenEffect(entity, effect, miraculousEffect.amplifier() + ((!miraculousEffect.toggleable() || buffsActive) ? powerLevel / 10 : 0));
                    }
                });

                boolean powerActive = entity.getData(QuirksUnleashedAttachmentTypes.QUIRK_ACTIVATED);
                boolean increasePowerLevel = false;
                AbilityData data = AbilityData.of(this);
                AbilityHandler handler = new MiraculousAbilityHandler(quirk);
                Ability.State state = AbilityUtils.performPassiveAbilities(level, entity, data, handler, null, quirk.value().passiveAbilities());
                if (powerActive) {
                    if (state.shouldStop()) {
                        powerActive = false;
                        entity.setData(QuirksUnleashedAttachmentTypes.QUIRK_ACTIVATED, powerActive);
                        state = AbilityUtils.performActiveAbility(level, entity, data, handler, null, Optional.of(quirk.value().activeAbility()));
                        if (state.shouldStop()) {
                            powerActive = false;
                            entity.setData(QuirksUnleashedAttachmentTypes.QUIRK_ACTIVATED, powerActive);
                            if (state.isSuccess()) {
                                increasePowerLevel = true;
                            }
                        }
                    }
                }
                tickTransformed(increasePowerLevel, powerActive).save(quirk, entity);
            }
        });
    }

    public QuirkData withPowerActive(boolean powerActive) {
        return new QuirkData(activated, quirkState, powerLevel, powerActive, storedEntities, buffsActive);
    }

    public QuirkData withPowerLevel(int powerLevel) {
        return new QuirkData(activated, quirkState, Math.clamp(powerLevel, 0, MAX_POWER_LEVEL), powerActive, storedEntities, buffsActive);
    }

    public QuirkData withStoredEntities(ImmutableList<CompoundTag> storedEntities) {
        return new QuirkData(activated, quirkState, powerLevel, powerActive, storedEntities, buffsActive);
    }

    private QuirkData tickTransformed(boolean increasePowerLevel, boolean powerActive) {
        return new QuirkData(activated, quirkState, increasePowerLevel ? powerLevel + 1 : powerLevel, powerActive, storedEntities, buffsActive);
    }

    public QuirkData toggleBuffsActive() {
        return new QuirkData(activated, quirkState, powerLevel, powerActive, storedEntities, !buffsActive);
    }

    /**
     * Saves this {@link QuirkData} to the provided entity and syncs it.
     *
     * @param miraculous The miraculous to save this data for
     * @param entity     The entity to save this data to
     */
    public void save(Holder<Quirk> miraculous, Entity entity) {
        entity.getData(QuirksUnleashedAttachmentTypes.QUIRKS).put(entity, miraculous, this);
    }
    /**
     * Represents an ongoing de/transformation and remaining frames for it.
     *
     * @param transforming    Whether this is a transformation or detransformation
     * @param remainingFrames The remaining frames for this de/transformation
     */
    public record TransformationState(boolean transforming, int remainingFrames) {
        public static final Codec<TransformationState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.fieldOf("transforming").forGetter(TransformationState::transforming),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("remainingFrames").forGetter(TransformationState::remainingFrames)).apply(instance, TransformationState::new));
        public static final StreamCodec<ByteBuf, TransformationState> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, TransformationState::transforming,
                ByteBufCodecs.VAR_INT, TransformationState::remainingFrames,
                TransformationState::new);

        public TransformationState decrementFrames() {
            return new TransformationState(transforming, remainingFrames - 1);
        }
    }
}
