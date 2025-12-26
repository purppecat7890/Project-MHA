package dev.purppecat.quirksunleashed.impl.data.lang;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyleData;
import dev.purppecat.quirksunleashed.impl.server.commands.FightingStyleCommand;
import dev.thomasglasser.tommylib.api.data.lang.ExtendedEnUsLanguageProvider;
import net.minecraft.data.PackOutput;

public class QuirksUnleashedEnUsLanguageProvider extends ExtendedEnUsLanguageProvider {
    public QuirksUnleashedEnUsLanguageProvider(PackOutput output) {
        super(output, QuirksUnleashed.MOD_ID);
    }

    @Override
    protected void addTranslations() {
        addCommands();
    }

    private void addCommands() {
        // Power Level
        add(FightingStyleCommand.FIGHTING_STYLE_QUERY_SUCCESS_SELF, "Your fighting style is %s.");
        add(FightingStyleCommand.FIGHTING_STYLE_QUERY_SUCCESS_OTHER, "%s's fighting style is %s.");
        add(FightingStyleCommand.FIGHTING_STYLE_SET_SUCCESS_SELF, "Your fighting style has been set to %s.");
        add(FightingStyleCommand.FIGHTING_STYLE_SET_SUCCESS_OTHER, "Set %s's fighting style to %s.");
        // Exceptions
        add(FightingStyleCommand.EXCEPTION_INVALID_FIGHTING_STYLE, "Invalid fighting style type %s");
    }
}
