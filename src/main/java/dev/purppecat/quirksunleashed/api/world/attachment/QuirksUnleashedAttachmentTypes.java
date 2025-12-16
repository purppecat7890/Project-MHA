package dev.purppecat.quirksunleashed.api.world.attachment;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyle;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyleData;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStylesData;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;

public class QuirksUnleashedAttachmentTypes {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, QuirksUnleashed.MOD_ID);

    // Fighting Styles
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<FightingStyleData>> FIGHTING_STYLE = ATTACHMENT_TYPES.register("fighting_style", () -> AttachmentType.builder(() -> new FightingStyleData()).sync(FightingStyleData.STREAM_CODEC).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<FightingStylesData>> FIGHTING_STYLES = ATTACHMENT_TYPES.register("fighting_styles", () -> AttachmentType.builder(() -> new FightingStylesData()).serialize(FightingStylesData.CODEC).sync(FightingStylesData.STREAM_CODEC).copyOnDeath().build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> COMBAT_MODE = ATTACHMENT_TYPES.register("combat_mode", () -> AttachmentType.builder(() -> false).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> PUNCH_COUNTER = ATTACHMENT_TYPES.register("punch_counter", () -> AttachmentType.builder(() -> 1).build());


    @ApiStatus.Internal
    public static void init() {}
}
