package dev.purppecat.quirksunleashed.impl.world.entity;

import static com.zigythebird.playeranim.PlayerAnimLibMod.ANIMATION_LAYER_ID;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyleData;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStylesData;
import dev.purppecat.quirksunleashed.impl.network.ClientboundPlayAnimationPayload;
import dev.purppecat.quirksunleashed.impl.network.ClientboundPunchCounterHandler;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyle;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyles;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
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

public class QuirksUnleashedEntityEvents {

    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        Level level = player.level();
        if (level instanceof ServerLevel serverLevel) {
            if ((player.getOffhandItem().isEmpty())) {
                if (player.getData(QuirksUnleashedAttachmentTypes.COMBAT_MODE)) {
                    FightingStyleData abilityEffectData = serverLevel.getEntity(player.getId()).getData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLE);
                    TommyLibServices.NETWORK.sendToTrackingClients(new ClientboundPlayAnimationPayload(abilityEffectData.heavy(), player.getId()), player);
                    DamageSource source = serverLevel.damageSources().playerAttack(player);
                    target.hurt(source, 2f);
                }
            }
        }
    }

    public static void onEntityJoinedLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof AbstractClientPlayer clientPlayer) {
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ANIMATION_LAYER_ID, 1502,
                    clientPlayer1 -> new PlayerAnimationController(clientPlayer,
                            (controller, state, animSetter) -> PlayState.STOP));
        }
        if (entity.level() instanceof ServerLevel serverLevel) {
            Holder<FightingStyle> defaultStyleHolder = serverLevel.registryAccess()
                    .registryOrThrow(QuirksUnleashedRegistries.FIGHTING_STYLE)
                    .getHolder(FightingStyles.DEFAULT)
                    .orElseThrow();

            FightingStyleData defaultData = new FightingStyleData(defaultStyleHolder.value());
            entity.setData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLE.get(), defaultData);

        }
    }

    public static void OnHitEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level instanceof ServerLevel serverLevel) {
            if ((player.getMainHandItem().isEmpty())) {
                if (player.getData(QuirksUnleashedAttachmentTypes.COMBAT_MODE)) {
                    Entity player1 = serverLevel.getEntity(player.getId());
                    if (player1 != null) {
                        FightingStyleData currentData = player1.getData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLE.get());
                        int punchCount = player.getData(QuirksUnleashedAttachmentTypes.PUNCH_COUNTER.get());
                        if (punchCount == 1) {
                            TommyLibServices.NETWORK.sendToTrackingClients(new ClientboundPlayAnimationPayload(currentData.punch1(), player.getId()), player);
                        } else if (punchCount == 2) {
                            TommyLibServices.NETWORK.sendToTrackingClients(new ClientboundPlayAnimationPayload(currentData.punch2(), player.getId()), player);
                        } else if (punchCount == 3) {
                            TommyLibServices.NETWORK.sendToTrackingClients(new ClientboundPlayAnimationPayload(currentData.punch3(), player.getId()), player);
                        } else if (punchCount == 4) {
                            TommyLibServices.NETWORK.sendToTrackingClients(new ClientboundPlayAnimationPayload(currentData.heavy(), player.getId()), player);
                        }
                        TommyLibServices.NETWORK.sendToClient(new ClientboundPunchCounterHandler(player.getId()), ((ServerPlayer) player));
                    }
                }
            }
        }
    }
}
