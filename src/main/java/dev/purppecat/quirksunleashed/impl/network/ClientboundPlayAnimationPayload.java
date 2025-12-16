package dev.purppecat.quirksunleashed.impl.network;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.enums.PlayState;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.NoteBlockEvent;

import static com.zigythebird.playeranim.PlayerAnimLibMod.ANIMATION_LAYER_ID;

public record ClientboundPlayAnimationPayload(ResourceLocation location, int targetId) implements ExtendedPacketPayload {
    public static final Type<ClientboundPlayAnimationPayload> TYPE =
            new Type<>(QuirksUnleashed.modLoc("clientbound_play_animation_payload"));
    public static final StreamCodec<ByteBuf, ClientboundPlayAnimationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(ResourceLocation.CODEC),
            ClientboundPlayAnimationPayload::location,
            ByteBufCodecs.VAR_INT,
            ClientboundPlayAnimationPayload::targetId,
            ClientboundPlayAnimationPayload::new
    );

    @Override
    public void handle(Player player) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (!(level == null)) {
            AbstractClientPlayer clientPlayer = (AbstractClientPlayer) level.getEntity(this.targetId);
            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                    clientPlayer, ANIMATION_LAYER_ID);
            if (controller != null) {

                FirstPersonConfiguration firstPersonConfiguration = controller.getFirstPersonConfiguration();
                firstPersonConfiguration.setShowRightArm(true);
                firstPersonConfiguration.setShowLeftArm(true);
                firstPersonConfiguration.setShowArmor(true);
                controller.setFirstPersonConfiguration(firstPersonConfiguration);
                controller.triggerAnimation(location);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
