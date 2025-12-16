package dev.purppecat.quirksunleashed.impl.network;

import com.google.common.collect.ImmutableList;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.network.NeoForgeNetworkUtils;
import dev.thomasglasser.tommylib.api.network.PayloadInfo;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;

public class QuirksUnleashedPayloads {
    public static List<PayloadInfo<?>> PAYLOADS = ImmutableList.of(
            new PayloadInfo<>(ServerboundToggleCombatMode.TYPE, ExtendedPacketPayload.Direction.CLIENT_TO_SERVER, ServerboundToggleCombatMode.CODEC),
            new PayloadInfo<>(ClientboundPunchCounterHandler.TYPE, ExtendedPacketPayload.Direction.SERVER_TO_CLIENT, ClientboundPunchCounterHandler.CODEC),
            new PayloadInfo<>(ClientboundPlayAnimationPayload.TYPE, ExtendedPacketPayload.Direction.SERVER_TO_CLIENT, ClientboundPlayAnimationPayload.STREAM_CODEC));

    public static void onRegisterPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(QuirksUnleashed.MOD_ID);
        PAYLOADS.forEach((info) -> NeoForgeNetworkUtils.register(registrar, info));
    }
}
