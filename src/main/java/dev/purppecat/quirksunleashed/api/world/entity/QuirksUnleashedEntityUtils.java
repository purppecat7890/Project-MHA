package dev.purppecat.quirksunleashed.api.world.entity;

import dev.thomasglasser.tommylib.api.world.entity.EntityUtils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class QuirksUnleashedEntityUtils {
    /**
     * Applies the provided {@link MobEffect} to the provided {@link LivingEntity} at the provided amplifier invisibly for an infinite duration.
     *
     * @param entity    The entity to apply the effect to
     * @param effect    The effect to apply to the entity
     * @param amplifier The amplifier to use for the effect
     */
    public static void applyInfiniteHiddenEffect(LivingEntity entity, Holder<MobEffect> effect, int amplifier) {
        entity.addEffect(new MobEffectInstance(effect, -1, amplifier, false, false));
    }
    /**
     * Applies the provided {@link MobEffect} to the provided {@link LivingEntity} at the provided amplifier invisibly for an finite duration.
     *
     * @param entity    The entity to apply the effect to
     * @param effect    The effect to apply to the entity
     * @param amplifier The amplifier to use for the effect
     */
    public static void applyFiniteHiddenEffect(LivingEntity entity, Holder<MobEffect> effect, int duration, int amplifier) {
        entity.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false));
    }


    /**
     * Collects every item an entity has in a {@link Set}, including curios.
     *
     * @param entity The entity to check for items
     * @return The {@link Set} of any items the entity has
     */
    public static Set<ItemStack> getInventory(Entity entity) {
        Set<ItemStack> inventory = new ObjectOpenHashSet<>();
        inventory.addAll(EntityUtils.getInventory(entity));
        return inventory;
    }

    /**
     * Finds an entity in whatever level is currently loaded with the provided {@link UUID}.
     *
     * @param level The level to pull the level list from
     * @param uuid  The {@link UUID} of the entity to find
     * @return The found entity, or null if not found
     */
    public static @Nullable Entity findEntity(ServerLevel level, UUID uuid) {
        for (ServerLevel serverLevel : level.getServer().getAllLevels()) {
            Entity entity = serverLevel.getEntity(uuid);
            if (entity != null)
                return entity;
        }
        return null;
    }
}
