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
        addInlineDefault("chat-detection.bossbar", true, "Enable gradient detection for the bossbar.");
        addInlineDefault("bstats.enabled", true, "Enable bStats");
        addInlineDefault("animations.enabled", true, "Enable animations");
        addInlineDefault("chat-detection-minimessage.enabled", false, "Enable minimessage chat detection.");
        addInlineDefault("chat-detection-minimessage.chat", true, "Enable chat minimessage detection.");
        addInlineDefault("chat-detection-minimessage.title", true, "Enable title minimessage detection.");
        addInlineDefault("chat-detection-minimessage.subtitle", true, "Enable subtitle minimessage detection.");
        addInlineDefault("chat-detection-minimessage.gui.item", true, "Enable minimessage detection for items in a gui.");
        addInlineDefault("chat-detection-minimessage.gui.title", true, "Enable minimessage detection for the gui title.");
        addInlineDefault("chat-detection-minimessage.scoreboard.title", true, "Enable minimessage detection for the scoreboard title.");
        addInlineDefault("chat-detection-minimessage.scoreboard.lines", true, "Enable minimessage detection for the scoreboard lines.");
        addInlineDefault("chat-detection-uwu.enabled", true, "Enable uwu chat detection.");
        addInlineDefault("chat-detection-uwu.chat", true, "Enable chat uwu detection.");
        addInlineDefault("chat-detection-uwu.title", true, "Enable title uwu detection.");
        addInlineDefault("chat-detection-uwu.subtitle", true, "Enable subtitle uwu detection.");
        addInlineDefault("chat-detection-uwu.gui.item", true, "Enable uwu detection for items in a gui.");
        addInlineDefault("chat-detection-uwu.gui.title", true, "Enable uwu detection for the gui title.");
        addInlineDefault("chat-detection-uwu.scoreboard.title", true, "Enable uwu detection for the scoreboard title.");
        addInlineDefault("chat-detection-uwu.scoreboard.lines", true, "Enable uwu detection for the scoreboard lines.");
    }
}
