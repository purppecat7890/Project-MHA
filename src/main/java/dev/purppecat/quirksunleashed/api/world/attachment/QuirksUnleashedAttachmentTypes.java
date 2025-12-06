package dev.purppecat.quirksunleashed.api.world.attachment;

import com.mojang.serialization.Codec;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyleData;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStylesData;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;

public class QuirksUnleashedAttachmentTypes {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, QuirksUnleashed.MOD_ID);

    // Fighting Styles
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<FightingStyleData>> FIGHTING_STYLE = ATTACHMENT_TYPES.register("fighting_style", () -> AttachmentType.builder(FightingStyleData::new).sync(FightingStyleData.STREAM_CODEC).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<FightingStylesData>> FIGHTING_STYLES = ATTACHMENT_TYPES.register("fighting_styles", () -> AttachmentType.builder(() -> new FightingStylesData()).serialize(FightingStylesData.CODEC).sync(FightingStylesData.STREAM_CODEC).copyOnDeath().build());

    private static <T> Codec<Optional<T>> optionalCodec(Codec<T> codec) {
        return codec.optionalFieldOf("data").codec();
    }

    private static <B extends ByteBuf, T> StreamCodec<B, Optional<T>> optionalStreamCodec(StreamCodec<B, T> codec) {
        return ByteBufCodecs.optional(codec);
    }

    @ApiStatus.Internal
    public static void init() {}
}
