package com.vagdedes.spartan.support;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TabCompletion implements TabCompleter {

    private static final Map<String, String> commands = new LinkedHashMap<>(18); // Attention

    static {
        commands.put("command", "perm"); // todo
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String arg, String[] args) {
        List<String> list = new ArrayList<>();
        int length = args.length;

        if (length == 1) {
            boolean isPlayer = sender instanceof Player;
            Player p = isPlayer ? (Player) sender : null;
            String argAbstract = args[0].toLowerCase();

            for (Map.Entry<String, String> entry : commands.entrySet()) {
                boolean add;

                if (!isPlayer) {
                    add = true;
                } else {
                    add = p.hasPermission(entry.getValue())
                            || p.hasPermission(Permissions.ADMIN);
                }

                if (add) {
                    String key = entry.getKey();

                    if (key.contains(argAbstract)) {
                        list.add(key);
                    }
                }
            }
        } else if (length > 1) {
            String argAbstract = args[length - 1].toLowerCase();
            boolean isPlayer = sender instanceof Player;
            Player p = isPlayer ? (Player) sender : null;

            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!isPlayer || p.canSee(online)) {
                    String name = online.getName();

                    if (name.toLowerCase().contains(argAbstract)) {
                        list.add(name);
                    }
                }
            }
        }
        return list;
    }
}
