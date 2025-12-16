package dev.purppecat.quirksunleashed.api.world.combat;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

public class FightingStylesData {
    public static final UnboundedMapCodec<Holder<FightingStyle>, FightingStyleData> MAP_CODEC = Codec.unboundedMap(FightingStyle.CODEC, FightingStyleData.CODEC);
    public static final Codec<FightingStylesData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MAP_CODEC.fieldOf("map").forGetter(set -> set.map)).apply(instance, FightingStylesData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FightingStylesData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    Maps::newHashMapWithExpectedSize,
                    FightingStyle.STREAM_CODEC,
                    FightingStyleData.STREAM_CODEC),
            set -> set.map,
            FightingStylesData::new);

    private final Map<Holder<FightingStyle>, FightingStyleData> map;

    public FightingStylesData() {
        this.map = new Reference2ObjectOpenHashMap<>();
    }

    public FightingStylesData(Map<Holder<FightingStyle>, FightingStyleData> map) {
        this.map = new Reference2ObjectOpenHashMap<>(map);
    }

    public FightingStyleData get(Holder<FightingStyleData> key) {
        return map.getOrDefault(key, new FightingStyleData());
    }

    public FightingStyleData put(Entity entity, Holder<FightingStyle> key, FightingStyleData value, boolean syncToClient) {
        FightingStyleData data = map.put(key, value);
        save(entity);
        return data;
    }

    public Set<Holder<FightingStyle>> keySet() {
        return Set.copyOf(map.keySet());
    }

    public List<FightingStyleData> values() {
        return List.copyOf(map.values());
    }

    public void forEach(BiConsumer<Holder<FightingStyle>, FightingStyleData> consumer) {
        map.forEach(consumer);
    }

    public void save(Entity entity) {
        entity.setData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLES, this);
    }
}
