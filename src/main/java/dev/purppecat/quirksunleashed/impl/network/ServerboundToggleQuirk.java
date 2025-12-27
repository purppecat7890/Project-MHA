package dev.purppecat.quirksunleashed.impl.network;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.world.ability.Ability;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.api.world.quirk.Quirk;
import dev.purppecat.quirksunleashed.api.world.quirk.QuirksData;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class ServerboundToggleQuirk implements ExtendedPacketPayload {
    public static final ServerboundToggleQuirk INSTANCE = new ServerboundToggleQuirk();
    public static final Type<ServerboundToggleQuirk> TYPE = new Type<>(QuirksUnleashed.modLoc("serverbound_toggle_quirk"));
    public static final StreamCodec<ByteBuf, ServerboundToggleQuirk> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public void handle(Player player) {
        player.setData(QuirksUnleashedAttachmentTypes.QUIRK_ACTIVATED, !player.getData(QuirksUnleashedAttachmentTypes.QUIRK_ACTIVATED));
        QuirksUnleashed.LOGGER.info(player.getGameProfile().getName() + " - Quirk: " + player.getData(QuirksUnleashedAttachmentTypes.QUIRK_ACTIVATED));
        player.displayClientMessage(Component.literal("Quirk: " + player.getData(QuirksUnleashedAttachmentTypes.QUIRK_ACTIVATED)), true);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
