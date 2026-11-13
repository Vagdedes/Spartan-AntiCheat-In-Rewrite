package com.vagdedes.spartan.base;

import com.vagdedes.spartan.Register;
import com.vagdedes.spartan.files.ConfigManagement;
import com.vagdedes.spartan.math.NumberConversions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Detection {

    public static final int maxCommands = 10;
    private static final File
            file = new File(
            Register.plugin.getDataFolder() + "/detections.yml"
    );

    private static final List<String> defaultPunishments = new ArrayList<>(Detection.maxCommands);

    static {
        defaultPunishments.add(Register.getCommand() + " kick {player} {detection:name}");

        for (int position = (defaultPunishments.size() + 1); position <= Detection.maxCommands; position++) {
            defaultPunishments.add("");
        }
    }

    // Object

    public final String realName;
    private String name;
    private final Map<String, Object> options;
    public final boolean handleCancelledEvents;
    private boolean enabled, punish;
    private final Set<String> disabledWorlds;
    private Collection<String> punishments;

    // Config values may be booleans, numbers or text, and numbers must not be cast directly as the exact type is unknown
    private static boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue() > 0.0;
        } else {
            return Boolean.parseBoolean(value.toString().toLowerCase());
        }
    }

    // Object Methods

    public Detection(String name) {
        this.options = new ConcurrentHashMap<>();
        this.realName = name;
        this.name = getOption("name", name, false).toString();
        this.enabled = getOption(
                "enabled",
                true,
                false
        ) == Boolean.TRUE;
        this.punish = getOption(
                "punish",
                true,
                false
        ) == Boolean.TRUE;
        this.handleCancelledEvents = getOption(
                "handle_cancelled_events",
                true,
                false
        ) == Boolean.TRUE;

        // Separator

        if (this.name != null) {
            if (this.name.length() > 32) {
                this.name = this.name.substring(0, 32);
            }
        } else {
            this.name = name;
        }
        this.setName(this.name);

        // Separator

        String worlds_config = getOption("disabled_worlds", "exampleDisabledWorld1, exampleDisabledWorld2", false).toString();

        if (worlds_config != null) {
            String[] worldsSplit = worlds_config.split(",");
            int size = worldsSplit.length;

            if (size > 0) {
                Set<String> set = new HashSet<>(size);

                for (String world : worldsSplit) {
                    world = world.replace(" ", "");

                    if (!world.isEmpty()) {
                        set.add(world.toLowerCase());
                    }
                }
                if (!set.isEmpty()) {
                    this.disabledWorlds = set;
                } else {
                    this.disabledWorlds = new HashSet<>(0);
                }
            } else {
                this.disabledWorlds = new HashSet<>(0);
            }
        } else {
            this.disabledWorlds = new HashSet<>(0);
        }


        // Separator

        calculatePunishmentCommands();
    }

    // Separator

    public String getName() {
        return name;
    }

    // Separator

    public boolean isEnabled(String world) {
        return this.enabled
                && (world == null
                || !disabledWorlds.contains(world.toLowerCase()));
    }

    public boolean setEnabled(boolean b) {
        if (this.enabled != b) {
            this.enabled = b;
            setOption("enabled", b);
            return true;
        } else {
            return false;
        }
    }

    // Separator

    public boolean setName(String name) {
        if (name.length() > 32) {
            name = name.substring(0, 32);
        }
        for (Detection detection : Detections.LIST) {
            if (detection.getName().equalsIgnoreCase(this.name)
                    || detection.realName.equalsIgnoreCase(this.name)) {
                this.name = realName;
                return false;
            }
        }
        this.name = name;
        setOption("name", name);
        return true;
    }

    // Separator

    private void setOptionRaw(YamlConfiguration configuration, String option, Object value) {
        try {
            option = this.realName + "." + option;
            configuration.set(option, value);

            try {
                configuration.save(file);
                options.put(option, value);
            } catch (Exception ex) {
                Register.notify("Could not save option '" + option + "' in file '" + file.getName() + "': " + ex.getMessage());
            }
            return;
        } catch (Exception ex) {
            Register.notify("Failed to find/create the '" + file.getName() + "' file.");
        }
        Register.notify("Failed to find/create the '" + file.getName() + "' file.");
    }

    private void setOption(String option, Object value) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        setOptionRaw(configuration, option, value);
    }

    private Object getOption(String option, Object def, boolean cache) {
        if (cache && options.containsKey(option)) {
            return options.get(option);
        }
        try {
            String key = this.realName + "." + option;
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

            if (configuration.contains(key)) {
                Object value = configuration.get(key, def);
                options.put(option, value);
                return value;
            }
            setOptionRaw(configuration, option, def);
        } catch (Exception ex) {
            Register.notify("Failed to find/create the '" + file.getName() + "' file.");
        }
        return def;
    }

    // Separator

    public boolean canPunish() {
        return !Register.getPanic() && this.punish;
    }

    public Collection<String> getPunishmentCommands(boolean clone) {
        return clone ? new ArrayList<>(this.punishments) : this.punishments;
    }

    private void calculatePunishmentCommands() {
        String sectionString = realName + ".punishments.commands";
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        if (!configuration.contains(sectionString)) {
            for (int position = 0; position < defaultPunishments.size(); position++) {
                ConfigManagement.add(file, sectionString + "." + (position + 1), defaultPunishments.get(position));
            }
        }
        ConfigurationSection section = configuration.getConfigurationSection(sectionString);

        if (section != null) {
            Collection<String> list = new ArrayList<>();
            Set<String> keys = section.getKeys(true);

            if (!keys.isEmpty()) {
                sectionString += ".";

                for (String key : keys) {
                    if (NumberConversions.validInteger(key)) {
                        int number = Integer.parseInt(key);

                        if (number >= 1 && number <= Detection.maxCommands) {
                            String command = configuration.getString(sectionString + number);

                            if (command != null && !command.isEmpty()) {
                                list.add(command);
                            }
                        }
                    }
                }
            }
            this.punishments = list;
        } else {
            this.punishments = new ArrayList<>(0);
        }
    }

    public boolean setPunish(boolean b) {
        if (this.punish != b) {
            this.punish = b;
            setOption("punishments.enabled", b);
            return true;
        } else {
            return false;
        }
    }

}
