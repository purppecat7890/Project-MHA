package dev.purppecat.quirksunleashed.impl.network;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record ClientboundPunchCounterHandler(int playerId) implements ExtendedPacketPayload {
    public static final Type<ClientboundPunchCounterHandler> TYPE = new Type<>(QuirksUnleashed.modLoc("serverbound_punch_counter_handler"));
    public static final StreamCodec<ByteBuf, ClientboundPunchCounterHandler> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ClientboundPunchCounterHandler::playerId,
            ClientboundPunchCounterHandler::new
    );
    @Override
    public void handle(Player player) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (!(level == null)) {
            Player targetPlayer = (Player) level.getEntity(this.playerId);
            targetPlayer.setData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get(), (targetPlayer.getData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get()) + 1));
            if (targetPlayer.getData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get()) == 4) {
                System.out.println(targetPlayer.getGameProfile().getName() + " - Heavy Attack");
                targetPlayer.displayClientMessage(Component.literal("Heavy Attack"), false);
            } else {
                if (targetPlayer.getData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get()) >= 5) {
                    targetPlayer.setData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get(), 1);
                }
                System.out.println(targetPlayer.getGameProfile().getName() + " - Punch Counter: " + targetPlayer.getData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get()));
                targetPlayer.displayClientMessage(Component.literal("Punch Counter: " + targetPlayer.getData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get())), false);
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
