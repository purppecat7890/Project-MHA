package dev.purppecat.quirksunleashed.impl.world.entity;

import static com.zigythebird.playeranim.PlayerAnimLibMod.ANIMATION_LAYER_ID;

import com.zigythebird.playeranim.animation.PlayerAnimResources;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyle;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyleData;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyles;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStylesData;
import dev.purppecat.quirksunleashed.impl.network.ClientboundPlayAnimationPayload;
import dev.purppecat.quirksunleashed.impl.network.ClientboundPunchCounterHandler;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

public class QuirksUnleashedEntityEvents {
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        Level level = player.level();
        if (level instanceof ServerLevel serverLevel) {
            if ((player.getOffhandItem().isEmpty())) {
                if (player.getData(QuirksUnleashedAttachmentTypes.COMBAT_MODE)) {
                    Holder<FightingStyle> key = player.getData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLES).getStyle().getFirst();
                    TommyLibServices.NETWORK.sendToTrackingClients(new ClientboundPlayAnimationPayload(key.value().heavy(), player.getId()), player); // tracking
                    System.out.println(key.value().heavy());
                    DamageSource source = serverLevel.damageSources().playerAttack(player);
                    target.hurt(source, 2f);
                }
            }
        }
    }

    public static void onEntityJoinedLevel(EntityJoinLevelEvent event) {
    }
    


    public static void OnHitEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level instanceof ServerLevel serverLevel) {
            if ((player.getMainHandItem().isEmpty())) {
                if (player.getData(QuirksUnleashedAttachmentTypes.COMBAT_MODE)) {
                    Holder<FightingStyle> key = player.getData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLES).getStyle().getFirst();
                    int punchCount = player.getData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get());
                    if (punchCount == 4) {
                        player.displayClientMessage(Component.literal("Heavy Attack"), false);
                    } else {
                        if (punchCount == 5 || punchCount > 5) {
                            player.setData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get(), 1);
                        }
                        player.displayClientMessage(Component.literal("Punch Counter: " + player.getData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get())), false);
                    }
                    player.setData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get(), (player.getData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get()) + 1));
                    ResourceLocation animLocation = switch (punchCount) {
                        case 1 -> key.value().punch1();
                        case 2 -> key.value().punch2();
                        case 3 -> key.value().punch3();
                        case 4 -> key.value().heavy();
                        default -> key.value().punch1();
                    };
                    TommyLibServices.NETWORK.sendToTrackingClients(new ClientboundPlayAnimationPayload(animLocation, player.getId()), player); // tracking
//                        TommyLibServices.NETWORK.sendToClient(new ClientboundPunchCounterHandler(player1.getId(), punchCount), ((ServerPlayer) player1));;
                    System.out.println(animLocation);
                }
            }
        }
    }
}
