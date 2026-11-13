package com.vagdedes.spartan.support;

import com.vagdedes.spartan.Register;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;

        if (label.equalsIgnoreCase(Register.getCommand())
                && (isPlayer || sender instanceof ConsoleCommandSender)) {
            Player player = isPlayer ? (Player) sender : null;

            if (args.length == 0) {
                // todo
            }
        }
        return false;
    }
}
