package me.doublenico.hypegradients.config.impl;

import dev.perryplaysmc.dynamicconfigurations.IDynamicConfiguration;
import dev.perryplaysmc.dynamicconfigurations.IDynamicConfigurationSection;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.config.ConfigManager;

import java.util.LinkedList;

public class ColorConfig extends ConfigManager {
    public ColorConfig(DynamicConfigurationDirectory configDirectory, IDynamicConfiguration config, boolean firstTime) {
        super(configDirectory, config);
        if (firstTime) {
            addDefault("colors.red", "840000");
            addDefault("colors.orange", "ff7f00");
            addDefault("colors.yellow", "ffff00");
            addDefault("colors.green", "00ff00");
        }
    }


    public LinkedList<String> getColors() {
        IDynamicConfigurationSection section = getConfig().getSection("colors");
        if (section == null)
            return null;
        return new LinkedList<>(section.getKeys(false));
    }
}
