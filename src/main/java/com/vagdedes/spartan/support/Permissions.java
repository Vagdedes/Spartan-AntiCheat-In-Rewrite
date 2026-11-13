package com.vagdedes.spartan.support;

import com.vagdedes.spartan.Register;
import com.vagdedes.spartan.base.Detection;
import com.vagdedes.spartan.files.ConfigManagement;
import org.bukkit.entity.Player;

public class Permissions {

    public static final String
            KICK = Register.getCommand() + ".kick",
            ADMIN = Register.getCommand() + ".admin",
            NOTIFICATIONS = Register.getCommand() + ".notifications",
            VERBOSE = Register.getCommand() + ".verbose",
            BYPASS = Register.getCommand() + ".bypass",
            RELOAD = Register.getCommand() + ".reload",
            TOGGLE = Register.getCommand() + ".toggle",
            ENABLE = Register.getCommand() + ".enable",
            DISABLE = Register.getCommand() + ".disable",
            PANIC = Register.getCommand() + ".panic",
            PUNISH = Register.getCommand() + ".punish",
            DELETE = Register.getCommand() + ".delete";

    public static final String[] LIST = new String[]{
            KICK,
            ADMIN,
            NOTIFICATIONS,
            VERBOSE,
            BYPASS,
            RELOAD,
            TOGGLE,
            ENABLE,
            DISABLE,
            PANIC,
            PUNISH,
            DELETE
    };

    public static boolean hasAny(Player p) {
        for (String key : LIST) {
            if (p.hasPermission(key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBypassing(Player p, Detection detection) {
        if (p.isOp()) {
            return ConfigManagement.settings.getBoolean("Important.op_bypass");
        } else {
            if (p.hasPermission(BYPASS)) {
                return true;
            } else if (detection != null) {
                return p.hasPermission(BYPASS + "." + detection.realName.toLowerCase());
            } else {
                return false;
            }
        }
    }

}
