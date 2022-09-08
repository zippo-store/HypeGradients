package me.doublenico.hypegradients.config.impl;


import dev.perryplaysmc.dynamicconfigurations.IDynamicConfiguration;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.config.ConfigManager;

public class SettingsConfig extends ConfigManager {
    public SettingsConfig(DynamicConfigurationDirectory configDirectory, IDynamicConfiguration config) {
        super(configDirectory, config);
        addInlineDefault("no-found-packet", "disable", "If the packet is not found, stop the server or disable the plugin, values 'disable' or 'stop'");
        addInlineDefault("colors", true, "Enable colors");
        addInlineDefault("chat-detection.enabled", true, "Enable gradient detection.");
        addInlineDefault("chat-detection.chat", true, "Enable chat gradient detection.");
        addInlineDefault("chat-detection.title", true, "Enable title gradient detection.");
        addInlineDefault("chat-detection.subtitle", true, "Enable subtitle gradient detection.");
    }
}
