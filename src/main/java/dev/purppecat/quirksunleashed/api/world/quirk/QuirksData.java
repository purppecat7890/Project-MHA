package dev.purppecat.quirksunleashed.api.world.quirk;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;

/// Holds all {@link QuirkData}s for an entity
public class QuirksData {
    public static final UnboundedMapCodec<Holder<Quirk>, QuirkData> MAP_CODEC = Codec.unboundedMap(Quirk.CODEC, QuirkData.CODEC);
    public static final Codec<QuirksData> CODEC = MAP_CODEC.xmap(QuirksData::new, data -> data.map);
    public static final StreamCodec<RegistryFriendlyByteBuf, QuirksData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    Object2ObjectOpenHashMap::new,
                    Quirk.STREAM_CODEC,
                    QuirkData.STREAM_CODEC),
            set -> set.map,
            QuirksData::new);

    private final Map<Holder<Quirk>, QuirkData> map;

    public QuirksData() {
        this.map = new Object2ObjectOpenHashMap<>();
    }

    public QuirksData(Map<Holder<Quirk>, QuirkData> map) {
        this.map = new Object2ObjectOpenHashMap<>(map);
    }

    /**
     * Gets the {@link QuirkData} for the provided {@link Quirk}
     *
     * @param key The quirk to get the data for
     * @return The quirk data
     */
    public QuirkData get(Holder<Quirk> key) {
        return map.getOrDefault(key, new QuirkData());
    }

    @ApiStatus.Internal
    public void tick(LivingEntity entity, ServerLevel level) {
        forEach((miraculous, data) -> data.tick(entity, level, miraculous));
    }

    public List<Holder<Quirk>> getQuirk() {
        ImmutableList.Builder<Holder<Quirk>> keys = new ImmutableList.Builder<>();
        for (Holder<Quirk> key : map.keySet()) {
            keys.add(key);
        }
        return keys.build();
    }

    @ApiStatus.Internal
    /// @see QuirkData#save(Holder, Entity)
    public QuirkData put(Entity entity, Holder<Quirk> key, QuirkData value) {
        QuirkData data = map.put(key, value);
        save(entity);
        return data;
    }

    /**
     * Returns an immutable set of quirk keys.
     *
     * @return An immutable set of quirk keys
     */
    public Set<Holder<Quirk>> keySet() {
        return ImmutableSet.copyOf(map.keySet());
    }

    /**
     * Returns an immutable set of quirk data.
     *
     * @return An immutable set of quirk data
     */
    public Set<QuirkData> values() {
        return ImmutableSet.copyOf(map.values());
    }

    /**
     * Executes the provided consumer for each quirk data entry.
     *
     * @param consumer The consumer to execute for each quirk data entry
     */
    public void forEach(BiConsumer<Holder<Quirk>, QuirkData> consumer) {
        map.forEach(consumer);
    }

    /**
     * Collects all quirk keys that are currently activated.
     *
     * @return An immutable list of quirk keys that are currently activated
     */
    public List<Holder<Quirk>> getQuirks() {
        ImmutableList.Builder<Holder<Quirk>> keys = new ImmutableList.Builder<>();
        for (Holder<Quirk> key : map.keySet()) {
            if (get(key).activated()) {
                keys.add(key);
            }
        }
        return keys.build();
    }

    /**
     * Saves the quirks data to the provided entity and syncs it to clients.
     *
     * @param entity The entity to save the quirks data to
     */
    public void save(Entity entity) {
        entity.setData(QuirksUnleashedAttachmentTypes.QUIRKS, this);
    }
}
