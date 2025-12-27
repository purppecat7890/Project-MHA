package dev.purppecat.quirksunleashed.impl.world.entity;

import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyle;
import dev.purppecat.quirksunleashed.impl.network.ClientboundPlayAnimationPayload;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

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

    public static void onPostEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity.level() instanceof ServerLevel level) {
            if (entity instanceof LivingEntity livingEntity) {
                entity.getData(QuirksUnleashedAttachmentTypes.QUIRKS).tick(livingEntity, level);
            }
        }

    }

    public static void onEntityJoinedLevel(EntityJoinLevelEvent event) {}

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
                }
            }
        }
    }
}
