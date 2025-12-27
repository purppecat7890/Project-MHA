package dev.purppecat.quirksunleashed.impl.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.core.registries.QuirksUnleashedRegistries;
import dev.purppecat.quirksunleashed.api.world.attachment.QuirksUnleashedAttachmentTypes;
import dev.purppecat.quirksunleashed.api.world.quirk.Quirk;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class QuirkCommand {
    public static final int COMMANDS_ENABLED_PERMISSION_LEVEL = 2;

    // Messages
    public static final String QUIRK_QUERY_SUCCESS_SELF = "commands.quirk.query.success.self";
    public static final String QUIRK_QUERY_SUCCESS_OTHER = "commands.quirk.query.success.other";
    public static final String QUIRK_SET_SUCCESS_SELF = "commands.quirk.set.success.self";
    public static final String QUIRK_SET_SUCCESS_OTHER = "commands.quirk.set.success.other";

    // Exceptions
    public static final String EXCEPTION_INVALID_QUIRK = "commands.quirk.invalid";
    private static final DynamicCommandExceptionType ERROR_INVALID_QUIRK = new DynamicCommandExceptionType(
            arg -> Component.translatableEscape(EXCEPTION_INVALID_QUIRK, arg));
    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(
            entity -> Component.translatableEscape("commands.attribute.failed.entity", entity));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("quirk")
                        .requires(source -> source.hasPermission(COMMANDS_ENABLED_PERMISSION_LEVEL))

                        // Query command
                        .then(Commands.literal("get")
                                .executes(ctx -> getQuirk(ctx.getSource().getEntityOrException(), ctx, true))
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .executes(ctx -> {
                                            Entity target = EntityArgument.getEntity(ctx, "target");
                                            return getQuirk(target, ctx, target == ctx.getSource().getEntity());
                                        })))

                        // Set command
                        .then(Commands.literal("set")
                                .then(Commands.argument("quirk", ResourceKeyArgument.key(QuirksUnleashedRegistries.QUIRK))
                                        .executes(ctx -> setQuirk(ctx.getSource().getEntityOrException(), ctx, true))
                                        .then(Commands.argument("target", EntityArgument.entity())
                                                .executes(ctx -> {
                                                    Entity target = EntityArgument.getEntity(ctx, "target");
                                                    return setQuirk(target, ctx, target == ctx.getSource().getEntity());
                                                })))));
    }

    private static int getQuirk(Entity entity, CommandContext<CommandSourceStack> context, boolean self) throws CommandSyntaxException {
        Holder<Quirk> key = entity.getData(QuirksUnleashedAttachmentTypes.QUIRKS).getQuirk().getFirst();
        context.getSource().sendSuccess(() -> self ? Component.translatable(QUIRK_QUERY_SUCCESS_SELF, Component.translatable(QuirksUnleashed.toLanguageKey(key.getKey())), key.toString()) : Component.translatable(QUIRK_QUERY_SUCCESS_OTHER, entity.getDisplayName(), Component.translatable(QuirksUnleashed.toLanguageKey(key.getKey())), key.toString()), true);
        return 1;
    }

    private static int setQuirk(Entity entity, CommandContext<CommandSourceStack> context, boolean self) throws CommandSyntaxException {
        Holder.Reference<Quirk> style = resolveQuirk(context, "quirk");
        entity.getData(QuirksUnleashedAttachmentTypes.QUIRKS).get(style).save(style, entity);
        context.getSource().sendSuccess(() -> self ? Component.translatable(QUIRK_SET_SUCCESS_SELF, Component.translatable(QuirksUnleashed.toLanguageKey(style.key())), style.toString()) : Component.translatable(QUIRK_SET_SUCCESS_OTHER, entity.getDisplayName(), Component.translatable(QuirksUnleashed.toLanguageKey(style.key())), style.toString()), true);
        return 1;
    }

    public static Holder.Reference<Quirk> resolveQuirk(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return ResourceKeyArgument.resolveKey(context, name, QuirksUnleashedRegistries.QUIRK, ERROR_INVALID_QUIRK);
    }
}
