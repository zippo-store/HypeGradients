package me.doublenico.hypegradients.config;

import dev.perryplaysmc.dynamicconfigurations.IDynamicConfigurationSection;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.configuration.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ColorConfig extends Configuration {
    public ColorConfig(DynamicConfigurationDirectory configDirectory, String configName, boolean firstTime) {
        super(configDirectory, configName, false);
        if (firstTime) {
            addDefault("colors.red", "840000");
            getConfig().comment(
                    "Here is the configuration for colors",
                    "You can use the colors using the <color-name> tag in a placeholder/gradient/message",
                    "Example: <red> -> #840000",
                    "If you wanna add a color, add in the colors list",
                    "How a color should look like in the configuration",
                    "name: 000000",
                    "The color needs to be a correct hex color (need to have 6 total of letters from A-F or 0-9 number) without the #",
                    "Don't worry if the colors are not good the plugin will check for the problematic color and put it in a colors section"
            );
            addDefault("colors.orange", "ff7f00");
            addDefault("colors.yellow", "ffff00");
            addDefault("colors.green", "00ff00");
        }

    }


    public List<String> getColors() {
        IDynamicConfigurationSection section = getConfig().getSection("colors");
        if (section == null)
            return Collections.emptyList();
        return new LinkedList<>(section.getKeys(false));
    }

    public void checkConfig(HypeGradients plugin) {
        HashMap<String, String> colors = new HashMap<>();
        for (String key : getConfig().getSection("colors").getKeys(false)) {
            String hex = getConfig().getString("colors." + key);
            if (hex == null) {
                getConfig().getSection("colors").set(key, null);
                continue;
            }
            if (!hex.matches("[a-fA-F\\d]{6}")) {
                plugin.getLogger().warning("The hex " + hex + " is not a valid hex color");
                plugin.getLogger().warning("The color will be put into error mode");

                getConfig().createSection("errors");
                colors.put(key, hex);
                getConfig().getSection("errors").set(key, hex);
            }
        }
        for (String key : colors.keySet()) {
            getConfig().getSection("colors").set(key, null);
        }
    }
}
