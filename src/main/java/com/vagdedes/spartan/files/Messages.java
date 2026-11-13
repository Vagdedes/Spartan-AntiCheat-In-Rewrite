package com.vagdedes.spartan.files;

import com.vagdedes.spartan.Register;

public class Messages extends ConfigBuilder {

    public Messages() {
        super("messages");
    }

    @Override
    public void create() {
        addOption("console_name", "Console");
        addOption("no_permission", "&8[&2" + prefix + "&8]&c You don't have permission to interact with this.");

        // Config
        addOption("config_reload", "&8[&2" + prefix + "&8]&e Config successfully reloaded.");

        // Panic
        addOption("panic_mode_enable", "&8[&2" + prefix + "&8]&a Panic mode enabled, no checks will punish players.");
        addOption("panic_mode_disable", "&8[&2" + prefix + "&8]&c Panic mode disabled, checks may punish players again if configured so.");

        // Kick
        addOption("kick_reason", "&c {reason}");
        addOption("kick_broadcast_message", "&8[&2" + prefix + "&8]&c {player}&7 was kicked for&4 {reason}");

        // Violations
        addOption("player_violation_reset_message", "&8[&2" + prefix + "&8]&a Violations successfully reset for player&8: &2{player}");
        addOption("player_stored_data_delete_message", "&8[&2" + prefix + "&8]&a Stored data successfully deleted for player&8: &2{player}");
        addOption("detection_notification", "&8[&2" + prefix + "&8] "
                + "&e{player} &7>> &c{detection:real}§8[{detection:level}§8] &7(§f{info}§7)");

        // Checks
        addOption("check_stored_data_delete_message", "&8[&2" + prefix + "&8]&a Stored data successfully deleted for check&8: &2{check}");
        addOption("check_enable_message", "&8[&2" + prefix + "&8] &aYou enabled the check&8:&7 {detection}");
        addOption("check_disable_message", "&8[&2" + prefix + "&8] &cYou disabled the check&8:&7 {detection}");
        addOption("check_silent_disable_message", "&8[&2" + prefix + "&8] &aYou enabled preventions for the check&8:&7 {detection}");
        addOption("check_silent_enable_message", "&8[&2" + prefix + "&8] &cYou disabled preventions for the check&8:&7 {detection}");
        addOption("check_punishment_enable_message", "&8[&2" + prefix + "&8] &aYou enabled punishments for the check&8:&7 {detection}");
        addOption("check_punishment_disable_message", "&8[&2" + prefix + "&8] &cYou disabled punishments for the check&8:&7 {detection}");
        addOption("non_existing_check", "&8[&2" + prefix + "&8] &cThis check doesn't exist.");

        // Notifications
        addOption("verbose_enabled", "&8[&2" + prefix + "&8] &aYou enabled verbose.");
        addOption("verbose_disabled", "&8[&2" + prefix + "&8] &cYou disabled verbose.");
        addOption("notifications_enabled", "&8[&2" + prefix + "&8] &aYou enabled notifications.");
        addOption("notifications_disabled", "&8[&2" + prefix + "&8] &cYou disabled notifications.");

        // Commands
        addOption("unknown_command", "&fUnknown command. Please type \"/help\" for help.");
        addOption("failed_command", "&8[&2" + prefix + "&8]&c Command failed ({command}). Please check your arguments and try again.");
    }

    @Override
    public final String getString(String path) {
        return super.getString(path);
    }

    @Override
    public final String getColorfulString(String path) {
        String string = super.getColorfulString(path);

        if (Register.isFreeEdition()
                && !string.contains(prefix)) {
            string = "§4§l(" + prefix + ")§r " + string;
        }
        string = string.replace(prefix, Register.getBukkitName());
        return string;
    }

}
