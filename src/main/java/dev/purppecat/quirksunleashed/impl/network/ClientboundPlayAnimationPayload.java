package dev.purppecat.quirksunleashed.impl.network;

import static com.zigythebird.playeranim.PlayerAnimLibMod.ANIMATION_LAYER_ID;

import com.zigythebird.playeranim.PlayerAnimLibMod;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.animation.layered.IAnimation;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record ClientboundPlayAnimationPayload(ResourceLocation location, int targetId) implements ExtendedPacketPayload {
    public static final Type<ClientboundPlayAnimationPayload> TYPE = new Type<>(QuirksUnleashed.modLoc("clientbound_play_animation_payload"));
    public static final StreamCodec<ByteBuf, ClientboundPlayAnimationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC),
            ClientboundPlayAnimationPayload::location,
            ByteBufCodecs.VAR_INT,
            ClientboundPlayAnimationPayload::targetId,
            ClientboundPlayAnimationPayload::new);

    @Override
    public void handle(Player player) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (!(level == null)) {
            AbstractClientPlayer clientPlayer = (AbstractClientPlayer) level.getEntity(this.targetId);
            PlayerAnimationController controller = null;
            if (clientPlayer != null) {
                controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                        clientPlayer, PlayerAnimLibMod.ANIMATION_LAYER_ID);
                if (controller != null) {
                    controller.triggerAnimation(location);
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
