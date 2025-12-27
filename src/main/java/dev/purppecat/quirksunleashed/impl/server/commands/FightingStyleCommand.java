package dev.purppecat.quirksunleashed.impl.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyle;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class FightingStyleCommand {
    public static final int COMMANDS_ENABLED_PERMISSION_LEVEL = 2;

    // Messages
    public static final String FIGHTING_STYLE_QUERY_SUCCESS_SELF = "commands.miraculous.fighting_style.query.success.self";
    public static final String FIGHTING_STYLE_QUERY_SUCCESS_OTHER = "commands.miraculous.fighting_style.query.success.other";
    public static final String FIGHTING_STYLE_SET_SUCCESS_SELF = "commands.miraculous.fighting_style.set.success.self";
    public static final String FIGHTING_STYLE_SET_SUCCESS_OTHER = "commands.miraculous.fighting_style.set.success.other";

    // Exceptions
    public static final String EXCEPTION_INVALID_FIGHTING_STYLE = "commands.miraculous.fighting_style.invalid";
    private static final DynamicCommandExceptionType ERROR_INVALID_FIGHTING_STYLE = new DynamicCommandExceptionType(
            arg -> Component.translatableEscape(EXCEPTION_INVALID_FIGHTING_STYLE, arg));
    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(
            entity -> Component.translatableEscape("commands.attribute.failed.entity", entity));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("fighting_style")
                        .requires(source -> source.hasPermission(COMMANDS_ENABLED_PERMISSION_LEVEL))

                        // Query command
                        .then(Commands.literal("get")
                                .executes(ctx -> getFightingStyle(ctx.getSource().getEntityOrException(), ctx, true))
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .executes(ctx -> {
                                            Entity target = EntityArgument.getEntity(ctx, "target");
                                            return getFightingStyle(target, ctx, target == ctx.getSource().getEntity());
                                        })))

                        // Set command
                        .then(Commands.literal("set")
                                .then(Commands.argument("fighting_style", ResourceKeyArgument.key(QuirksUnleashedRegistries.FIGHTING_STYLE))
                                        .executes(ctx -> setFightingStyle(ctx.getSource().getEntityOrException(), ctx, true))
                                        .then(Commands.argument("target", EntityArgument.entity())
                                                .executes(ctx -> {
                                                    Entity target = EntityArgument.getEntity(ctx, "target");
                                                    return setFightingStyle(target, ctx, target == ctx.getSource().getEntity());
                                                })))));
    }

    private static int getFightingStyle(Entity entity, CommandContext<CommandSourceStack> context, boolean self) throws CommandSyntaxException {
        Holder<FightingStyle> key = entity.getData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLES).getStyle().getFirst();
        context.getSource().sendSuccess(() -> self ? Component.translatable(FIGHTING_STYLE_QUERY_SUCCESS_SELF, Component.translatable(QuirksUnleashed.toLanguageKey(key.getKey())), key.toString()) : Component.translatable(FIGHTING_STYLE_QUERY_SUCCESS_OTHER, entity.getDisplayName(), Component.translatable(QuirksUnleashed.toLanguageKey(key.getKey())), key.toString()), true);
        return 1;
    }

    private static int setFightingStyle(Entity entity, CommandContext<CommandSourceStack> context, boolean self) throws CommandSyntaxException {
        Holder.Reference<FightingStyle> style = resolveMiraculous(context, "fighting_style");
        entity.getData(QuirksUnleashedAttachmentTypes.FIGHTING_STYLES).get(style).save(style, entity);
        context.getSource().sendSuccess(() -> self ? Component.translatable(FIGHTING_STYLE_SET_SUCCESS_SELF, Component.translatable(QuirksUnleashed.toLanguageKey(style.key())), style.toString()) : Component.translatable(FIGHTING_STYLE_SET_SUCCESS_OTHER, entity.getDisplayName(), Component.translatable(QuirksUnleashed.toLanguageKey(style.key())), style.toString()), true);
        return 1;
    }

    public static Holder.Reference<FightingStyle> resolveMiraculous(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return ResourceKeyArgument.resolveKey(context, name, QuirksUnleashedRegistries.FIGHTING_STYLE, ERROR_INVALID_FIGHTING_STYLE);
    }
}
