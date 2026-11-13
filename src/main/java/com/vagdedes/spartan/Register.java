package com.vagdedes.spartan;

import com.vagdedes.spartan.base.Detection;
import com.vagdedes.spartan.base.Notifications;
import com.vagdedes.spartan.files.ConfigManagement;
import com.vagdedes.spartan.math.NumberConversions;
import com.vagdedes.spartan.support.Commands;
import com.vagdedes.spartan.support.TabCompletion;
import com.vagdedes.spartan.support.Versions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class Register extends JavaPlugin {

    public static Plugin plugin = null;
    public static final String
            user = "%%__USER__%%",
            resource = "%%__RESOURCE__%%";

    private static final boolean
            hasResourceId = NumberConversions.validInteger(resource),
            builtByBit = "%%__BUILTBYBIT__%%".length() != 18,
            spigot = !builtByBit && hasResourceId;
    private static boolean panic = false;

    private static final Set<Class<?>> listeners = new LinkedHashSet<>(2);

    public static boolean isFreeEdition() {
        return !builtByBit && !spigot;
    }

    public void onEnable() {
        plugin = this;

        if (Versions.serverVersion == Versions.MCVersion.OTHER) {
            Bukkit.getConsoleSender().sendMessage(
                    "[" + plugin.getName() + "] The server's version or type is not supported. "
                            + "Please contact the plugin's developer if you think this is an error."
            );
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        ConfigManagement.create();
        //enable(new EventClass());
        this.getCommand(getCommand()).setExecutor(new Commands());
        this.getCommand(getCommand()).setTabCompleter(new TabCompletion());
    }

    public void onDisable() {
        plugin = this;
        listeners.clear();
    }

    // Utilities

    private static void enable(Listener l) {
        if (isPluginEnabled() && listeners.add(l.getClass())) {
            Bukkit.getPluginManager().registerEvents(l, plugin);
        }
    }

    public static boolean isPluginEnabled() {
        return plugin != null && plugin.isEnabled();
    }

    public static String getVersion() {
        return plugin == null ? "" : plugin.getDescription().getVersion();
    }

    public static String getBukkitName() {
        return plugin == null ? "" : plugin.getName();
    }

    public static String getCommand() {
        return getBukkitName().toLowerCase();
    }

    public static void notify(String message) {
        message = ChatColor.YELLOW + "[" + Register.getBukkitName() + "] " + message;
        Bukkit.getConsoleSender().sendMessage(message);
        Collection<Player> players = Notifications.getNotificationPlayers();

        for (Player p : players) {
            p.sendMessage(message);
        }
    }

    public static boolean setPanic(boolean b) {
        return panic = b;
    }

    public static boolean getPanic() {
        return panic;
    }

}

