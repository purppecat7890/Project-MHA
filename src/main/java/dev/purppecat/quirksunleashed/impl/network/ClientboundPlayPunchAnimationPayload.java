package dev.purppecat.quirksunleashed.impl.network;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyleData;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import static com.zigythebird.playeranim.PlayerAnimLibMod.ANIMATION_LAYER_ID;

public class ClientboundPlayPunchAnimationPayload implements ExtendedPacketPayload {
//  public static final ClientboundPlayPunchAnimationPayload INSTANCE = new ClientboundPlayPunchAnimationPayload();
//  public static final Type<ClientboundPlayPunchAnimationPayload> TYPE =
//          new Type<>(QuirksUnleashed.modLoc("clientbound_play_punch_animation_payload"));
//  public static final StreamCodec<ByteBuf, ClientboundPlayPunchAnimationPayload> CODEC = StreamCodec.unit(INSTANCE);
    public static final ClientboundPlayPunchAnimationPayload INSTANCE = new ClientboundPlayPunchAnimationPayload();
    public static final Type<ClientboundPlayPunchAnimationPayload> TYPE =
            new Type<>(QuirksUnleashed.modLoc("clientbound_play_punch_animation_payload"));
    public static final StreamCodec<ByteBuf, ClientboundPlayPunchAnimationPayload> CODEC = StreamCodec.unit(INSTANCE);

    private ClientboundPlayPunchAnimationPayload() {}


    @Override
    public void handle(Player player) {
        if (player instanceof AbstractClientPlayer clientPlayer) {
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ANIMATION_LAYER_ID, 1502,
                    clientPlayer1 -> new PlayerAnimationController(clientPlayer,
                            (controller, state, animSetter) -> PlayState.STOP));
            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                    clientPlayer, ANIMATION_LAYER_ID);
            FightingStyleData abilityEffectData = clientPlayer.getData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLE);
            controller.triggerAnimation(abilityEffectData.punch1());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
