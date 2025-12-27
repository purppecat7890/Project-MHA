package dev.purppecat.quirksunleashed.impl.network;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class ServerboundToggleCombatMode implements ExtendedPacketPayload {
    public static final ServerboundToggleCombatMode INSTANCE = new ServerboundToggleCombatMode();
    public static final Type<ServerboundToggleCombatMode> TYPE = new Type<>(QuirksUnleashed.modLoc("serverbound_toggle_combat_mode"));
    public static final StreamCodec<ByteBuf, ServerboundToggleCombatMode> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public void handle(Player player) {
        player.setData(QuirksUnleashedAttachmentTypes.COMBAT_MODE, !player.getData(QuirksUnleashedAttachmentTypes.COMBAT_MODE));
        QuirksUnleashed.LOGGER.info(player.getGameProfile().getName() + " - Combat Mode: " + player.getData(QuirksUnleashedAttachmentTypes.COMBAT_MODE));
        player.displayClientMessage(Component.literal("Combat Mode: " + player.getData(QuirksUnleashedAttachmentTypes.COMBAT_MODE)), true);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
