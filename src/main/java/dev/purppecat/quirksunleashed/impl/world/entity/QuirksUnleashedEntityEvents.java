package dev.purppecat.quirksunleashed.impl.world.entity;

import static com.zigythebird.playeranim.PlayerAnimLibMod.ANIMATION_LAYER_ID;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyleData;
import dev.purppecat.quirksunleashed.impl.network.ClientboundPlayPunchAnimationPayload;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class QuirksUnleashedEntityEvents {
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        if ((player.getMainHandItem().isEmpty())) {
            if (!(event.getLevel().isClientSide())) {
                if (player instanceof AbstractClientPlayer clientPlayer) {
                    PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ANIMATION_LAYER_ID, 1502,
                            clientPlayer1 -> new PlayerAnimationController(clientPlayer,
                                    (controller, state, animSetter) -> PlayState.STOP));
                    PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                            clientPlayer, ANIMATION_LAYER_ID);
                    FightingStyleData abilityEffectData = controller.getPlayer().getData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLE);
                    controller.registerBones();
                    controller.triggerAnimation(abilityEffectData.punch2());
                    controller.getFirstPersonConfiguration().setShowRightArm(true);
                    controller.getFirstPersonConfiguration().setShowArmor(true);
                    controller.getFirstPersonConfiguration().setShowLeftArm(true);
                    int damage = 5;
                    if (!event.getLevel().isClientSide()) {
                        player.attack(entity);
                        entity.hurt(player.damageSources().source(DamageTypes.PLAYER_ATTACK), damage);
                    }
                }
            }
        }
    }

    public static void OnHitEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if ((player.getMainHandItem().isEmpty())) {
            if (player instanceof AbstractClientPlayer clientPlayer) {
                PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ANIMATION_LAYER_ID, 1502,
                        clientPlayer1 -> new PlayerAnimationController(clientPlayer,
                                (controller, state, animSetter) -> PlayState.STOP));
                PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                        clientPlayer, ANIMATION_LAYER_ID);
                TommyLibServices.NETWORK.sendToClient(ClientboundPlayPunchAnimationPayload.INSTANCE, ((ServerPlayer) player));
            }
            player.sendSystemMessage(Component.literal("yo"));
        }
    }
}
