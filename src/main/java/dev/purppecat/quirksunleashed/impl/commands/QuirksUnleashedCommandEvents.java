package dev.purppecat.quirksunleashed.impl.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.purppecat.quirksunleashed.impl.server.commands.FightingStyleCommand;
import dev.purppecat.quirksunleashed.impl.server.commands.QuirkCommand;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;

public class QuirksUnleashedCommandEvents {
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        FightingStyleCommand.register(dispatcher);
        QuirkCommand.register(dispatcher);

        ConfigCommand.register(dispatcher);
    }
}
