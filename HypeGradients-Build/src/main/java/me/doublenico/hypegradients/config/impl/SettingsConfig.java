package me.doublenico.hypegradients.config.impl;


import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.config.ConfigManager;

public class SettingsConfig extends ConfigManager {
    public SettingsConfig(DynamicConfigurationDirectory configDirectory, String configName) {
        super(configDirectory, configName, true);
        addInlineDefault("no-found-packet", "disable", "If the packet is not found, stop the server or disable the plugin, values disable or stop");
        addInlineDefault("colors", true, "Enable colors");
        addInlineDefault("chat-detection.enabled", true, "Enable gradient detection.");
        addInlineDefault("chat-detection.chat", true, "Enable chat gradient detection.");
        addInlineDefault("chat-detection.title", true, "Enable title gradient detection.");
        addInlineDefault("chat-detection.subtitle", true, "Enable subtitle gradient detection.");
        addInlineDefault("chat-detection.gui.item", true, "Enable gradient detection for items in a gui.");
        addInlineDefault("chat-detection.gui.title", true, "Enable gradient detection for the gui title.");
        addInlineDefault("chat-detection.scoreboard.title", true, "Enable gradient detection for the scoreboard title.");
        addInlineDefault("chat-detection.scoreboard.lines", true, "Enable gradient detection for the scoreboard lines.");
    }
}
