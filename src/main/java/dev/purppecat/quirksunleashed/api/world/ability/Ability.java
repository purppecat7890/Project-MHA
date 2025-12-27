package dev.purppecat.quirksunleashed.api.world.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedBuiltInRegistries;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.ability.context.AbilityContext;
import dev.purppecat.quirksunleashed.api.world.ability.handler.AbilityHandler;
import dev.purppecat.quirksunleashed.api.world.quirk.Quirk;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.Optional;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Performs actions given a context, with additional points of interest.
 * Backed by serializers in {@link QuirksUnleashedRegistries#ABILITY_SERIALIZER} matching {@link Ability#codec()}
 */
public interface Ability {
    Codec<Ability> DIRECT_CODEC = QuirksUnleashedBuiltInRegistries.ABILITY_SERIALIZER.byNameCodec()
            .dispatch(Ability::codec, Function.identity());
    Codec<Holder<Ability>> CODEC = RegistryFileCodec.create(QuirksUnleashedRegistries.ABILITY, DIRECT_CODEC);
    Codec<HolderSet<Ability>> HOLDER_SET_CODEC = RegistryCodecs.homogeneousList(QuirksUnleashedRegistries.ABILITY, DIRECT_CODEC);

    StreamCodec<RegistryFriendlyByteBuf, Holder<Ability>> STREAM_CODEC = ByteBufCodecs.holderRegistry(QuirksUnleashedRegistries.ABILITY);
    StreamCodec<RegistryFriendlyByteBuf, HolderSet<Ability>> HOLDER_SET_STREAM_CODEC = ByteBufCodecs.holderSet(QuirksUnleashedRegistries.ABILITY);

    /**
     * Performs actions based on the given context.
     *
     * @param data      The relevant {@link AbilityData} of the performer
     * @param level     The level the ability is being performed in
     * @param performer The performer of the ability
     * @param handler   The handler of the ability
     * @param context   The context of the ability (null if passive)
     * @return Whether the ability should consume the active state (i.e., stop the ability and trigger completion)
     */
    State perform(AbilityData data, ServerLevel level, LivingEntity performer, AbilityHandler handler, @Nullable AbilityContext context);

    /**
     * Called when the performer transforms.
     * This can mean different things depending on how it's used.
     * It is simply a starting point immediately before the ability becomes relevant and,
     * if passive, starts being performed.
     * 
     * @param data      The relevant {@link AbilityData} of the performer
     * @param level     The level the ability is being performed in
     * @param performer The performer of the ability
     */
    default void add(AbilityData data, ServerLevel level, LivingEntity performer) {}

    /**
     * Called when the performer detransforms.
     * This can mean different things depending on how its used.
     * It is simply a stopping point immediately before the ability is no longer relevant and,
     * if passive, stops being performed.
     * 
     * @param data      The relevant {@link AbilityData} of the performer
     * @param level     The level the ability is being performed in
     * @param performer The performer of the ability
     */
    default void remove(AbilityData data, ServerLevel level, LivingEntity performer) {}

    /**
     * Called when the performer joins a new {@link Level}.
     * Should be used to ensure data is properly handled on dimension change or world entrance.
     * 
     * @param data      The relevant {@link AbilityData} of the performer
     * @param level     The level the performer just joined
     * @param performer The performer of the ability
     */
    default void joinLevel(AbilityData data, ServerLevel level, LivingEntity performer) {}

    /**
     * Called when the performer leaves their current {@link Level}.
     * Should be used to ensure data is properly handled on dimension change or world exit.
     * 
     * @param data      The relevant {@link AbilityData} of the performer
     * @param level     The level the performer just left
     * @param performer The performer of the ability
     */
    default void leaveLevel(AbilityData data, ServerLevel level, LivingEntity performer) {}

    /**
     * The dispatch {@link MapCodec} that defines and constructs the ability.
     * Should point to an entry in {@link QuirksUnleashedRegistries#ABILITY_SERIALIZER}.
     * 
     * @return The ability dispatch codec
     */
    MapCodec<? extends Ability> codec();

    /**
     * Plays a sound (if present) at the performer's block position with the performer's {@link SoundSource}.
     * 
     * @param level     The level the ability is being performed in
     * @param performer The performer of the ability
     * @param sound     The optional sound to play if present
     */
    static void playSound(ServerLevel level, LivingEntity performer, Optional<Holder<SoundEvent>> sound) {
        sound.ifPresent(soundEvent -> level.playSound(null, performer.blockPosition(), soundEvent.value(), performer.getSoundSource(), 1, 1));
    }

    /**
     * Collects all abilities in an ability, including sub abilities.
     * 
     * @param ability The ability to collect sub abilities from
     * @return A sorted set with the passed ability and all contained sub abilities
     */
    static SortedSet<Ability> getAll(Ability ability) {
        SortedSet<Ability> abilities = new ObjectLinkedOpenHashSet<>();
        abilities.add(ability);
        if (ability instanceof AbilityWithSubAbilities abilityWithSubAbilities) {
            abilities.addAll(abilityWithSubAbilities.getAll());
        }
        return abilities;
    }

    /**
     * Collects all abilities matching the provided predicate in an ability, including sub abilities.
     * 
     * @param predicate The predicate to filter abilities with
     * @param ability   The ability to test and collect matching sub abilities from
     * @return A sorted set with any matching of the passed ability and all contained sub abilities
     */
    static SortedSet<Ability> getMatching(Predicate<Ability> predicate, Ability ability) {
        SortedSet<Ability> abilities = new ObjectLinkedOpenHashSet<>();
        if (predicate.test(ability))
            abilities.add(ability);
        if (ability instanceof AbilityWithSubAbilities abilityWithSubAbilities)
            abilities.addAll(abilityWithSubAbilities.getMatching(predicate));
        return abilities;
    }

    /**
     * Finds the first ability or sub ability matching the provided predicate from the provided ability.
     * 
     * @param predicate The predicate to filter abilities with
     * @param ability   The ability to test and test sub abilities from
     * @return The matching ability or first sub ability matching the provided predicate
     */
    static @Nullable Ability getFirstMatching(Predicate<Ability> predicate, Ability ability) {
        SortedSet<Ability> abilities = getMatching(predicate, ability);
        return abilities.isEmpty() ? null : abilities.getFirst();
    }

    /**
     * Checks if the provided ability or any sub ability match the provided predicate.
     * 
     * @param predicate The predicate to check abilities
     * @param ability   The ability to test and test sub abilities from
     * @return Whether the ability or any sub abilities match the provided predicate
     */
    static boolean hasMatching(Predicate<Ability> predicate, Ability ability) {
        return !getMatching(predicate, ability).isEmpty();
    }

    // Overloads for active and passive abilities
    static SortedSet<Ability> getAll(Optional<Holder<Ability>> activeAbility, HolderSet<Ability> passiveAbilities) {
        SortedSet<Ability> abilities = new ObjectLinkedOpenHashSet<>();
        activeAbility.ifPresent(ability -> abilities.addAll(getAll(ability.value())));
        for (Holder<Ability> ability : passiveAbilities) {
            abilities.addAll(getAll(ability.value()));
        }
        return abilities;
    }

    static SortedSet<Ability> getMatching(Predicate<Ability> predicate, Optional<Holder<Ability>> activeAbility, HolderSet<Ability> passiveAbilities) {
        SortedSet<Ability> abilities = new ObjectLinkedOpenHashSet<>();
        activeAbility.ifPresent(ability -> abilities.addAll(getMatching(predicate, ability.value())));
        for (Holder<Ability> ability : passiveAbilities) {
            abilities.addAll(getMatching(predicate, ability.value()));
        }
        return abilities;
    }

    static @Nullable Ability getFirstMatching(Predicate<Ability> predicate, Optional<Holder<Ability>> activeAbility, HolderSet<Ability> passiveAbilities) {
        SortedSet<Ability> abilities = getMatching(predicate, activeAbility, passiveAbilities);
        return abilities.isEmpty() ? null : abilities.getFirst();
    }

    static boolean hasMatching(Predicate<Ability> predicate, Optional<Holder<Ability>> activeAbility, HolderSet<Ability> passiveAbilities) {
        return !getMatching(predicate, activeAbility, passiveAbilities).isEmpty();
    }

    // Overloads for Miraculous abilities
    static SortedSet<Ability> getAll(Quirk miraculous, boolean includeActive) {
        return getAll(Optional.ofNullable(includeActive ? miraculous.activeAbility() : null), miraculous.passiveAbilities());
    }

    static SortedSet<Ability> getMatching(Predicate<Ability> predicate, Quirk miraculous, boolean includeActive) {
        return getMatching(predicate, Optional.ofNullable(includeActive ? miraculous.activeAbility() : null), miraculous.passiveAbilities());
    }

    static @Nullable Ability getFirstMatching(Predicate<Ability> predicate, Quirk miraculous, boolean includeActive) {
        return getFirstMatching(predicate, Optional.ofNullable(includeActive ? miraculous.activeAbility() : null), miraculous.passiveAbilities());
    }

    static boolean hasMatching(Predicate<Ability> predicate, Quirk miraculous, boolean includeActive) {
        return hasMatching(predicate, Optional.ofNullable(includeActive ? miraculous.activeAbility() : null), miraculous.passiveAbilities());
    }

    enum State {
        CONSUME,
        PASS,
        CANCEL;

        public boolean shouldStop() {
            return this == CANCEL || this == CONSUME;
        }

        public boolean isSuccess() {
            return this == CONSUME;
        }
    }
}
