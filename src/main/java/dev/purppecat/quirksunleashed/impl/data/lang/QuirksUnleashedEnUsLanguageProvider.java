package dev.purppecat.quirksunleashed.impl.data.lang;

import dev.purppecat.quirksunleashed.QuirksUnleashed;
import dev.purppecat.quirksunleashed.api.world.ability.Abilities;
import dev.purppecat.quirksunleashed.api.world.combat.FightingStyles;
import dev.purppecat.quirksunleashed.api.world.quirk.Quirks;
import dev.purppecat.quirksunleashed.impl.client.QuirksUnleashedKeyMappings;
import dev.purppecat.quirksunleashed.impl.server.commands.FightingStyleCommand;
import dev.purppecat.quirksunleashed.impl.server.commands.QuirkCommand;
import dev.thomasglasser.tommylib.api.data.lang.ExtendedEnUsLanguageProvider;
import net.minecraft.data.PackOutput;

public class QuirksUnleashedEnUsLanguageProvider extends ExtendedEnUsLanguageProvider {
    public QuirksUnleashedEnUsLanguageProvider(PackOutput output) {
        super(output, QuirksUnleashed.MOD_ID);
    }

    @Override
    protected void addTranslations() {
        addCommands();
        addAbilities();
        addFightingStyles();
        addQuirks();
        addKeyMappings();
    }

    private void addCommands() {
        // Fighting Style
        add(FightingStyleCommand.FIGHTING_STYLE_QUERY_SUCCESS_SELF, "Your fighting style is %s.");
        add(FightingStyleCommand.FIGHTING_STYLE_QUERY_SUCCESS_OTHER, "%s's fighting style is %s.");
        add(FightingStyleCommand.FIGHTING_STYLE_SET_SUCCESS_SELF, "Your fighting style has been set to %s.");
        add(FightingStyleCommand.FIGHTING_STYLE_SET_SUCCESS_OTHER, "Set %s's fighting style to %s.");
        // Quirk
        add(QuirkCommand.QUIRK_QUERY_SUCCESS_SELF, "Your Quirk is %s.");
        add(QuirkCommand.QUIRK_QUERY_SUCCESS_OTHER, "%s's Quirk is %s.");
        add(QuirkCommand.QUIRK_SET_SUCCESS_SELF, "Your Quirk has been set to %s.");
        add(QuirkCommand.QUIRK_SET_SUCCESS_OTHER, "Set %s's Quirk to %s.");
        // Exceptions
        add(FightingStyleCommand.EXCEPTION_INVALID_FIGHTING_STYLE, "Invalid fighting style type %s");
        add(QuirkCommand.EXCEPTION_INVALID_QUIRK, "Invalid Quirk %s");
    }

    private void addQuirks() {
        add(Quirks.QUIRKLESS, "Quirkless");
        add(Quirks.LEAP, "Leap");
    }

    private void addFightingStyles() {
        add(FightingStyles.DEFAULT, "Default");
    }

    private void addAbilities() {
        addCapitalized(Abilities.NONE);
        addCapitalized(Abilities.HIGH_JUMP);
    }

    private void addKeyMappings() {
        add(QuirksUnleashedKeyMappings.QUIRKS_UNLEASHED_CATEGORY, "Quirks Unleashed");
        add(QuirksUnleashedKeyMappings.TOGGLE_COMBAT, "Toggle Combat");
        add(QuirksUnleashedKeyMappings.ACTIVATE_QUIRK, "Toggle Quirk");
        add(QuirksUnleashedKeyMappings.ACTIVATE_ABILITY, "Activate Ability");
    }
}
