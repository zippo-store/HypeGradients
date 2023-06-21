package me.doublenico.hypegradients.api.detection;

import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.configuration.Configuration;

public class ChatDetectionConfiguration extends Configuration {

    private final ChatDetectionValues chatDetectionValues;

    public ChatDetectionConfiguration(DynamicConfigurationDirectory configDirectory, String configName, boolean appendMissingKeys) {
        super(configDirectory, configName, appendMissingKeys);
        addInlineDefault("chat-detection.enabled", true, "Enable detection for " + configName);
        addInlineDefault("chat-detection.chat", true, "Enable chat detection for " + configName);
        addInlineDefault("chat-detection.title", true, "Enable title detection for " + configName);
        addInlineDefault("chat-detection.subtitle", true, "Enable subtitle detection for " + configName);
        addInlineDefault("chat-detection.gui.item", true, "Enable detection for items in a gui for " + configName);
        addInlineDefault("chat-detection.gui.title", true, "Enable detection for the gui title for " + configName);
        addInlineDefault("chat-detection.scoreboard.title", true, "Enable detection for the scoreboard title for " + configName);
        addInlineDefault("chat-detection.scoreboard.lines", true, "Enable detection for the scoreboard lines for " + configName);
        addInlineDefault("chat-detection.bossbar", true, "Enable detection for the bossbar for " + configName);
        addInlineDefault("chat-detection.entity", true, "Enable detection for entities for " + configName);
        addInlineDefault("chat-detection.entity-metadata", true, "Enable detection for entities metadata for " + configName);
        addInlineDefault("chat-detection.motd", true, "Enable detection for motd for " + configName);
        addInlineDefault("chat-detection.tab.footer", true, "Enable detection for tab footer for " + configName);
        addInlineDefault("chat-detection.tab.header", true, "Enable detection for tab header for " + configName);
        addInlineDefault("chat-detection.tab.player-info", true, "Enable detection for tab player info for" + configName);

        chatDetectionValues = new ChatDetectionValues(
                getConfig().getBoolean("chat-detection.enabled", true),
                getConfig().getBoolean("chat-detection.chat", true),
                getConfig().getBoolean("chat-detection.title", true),
                getConfig().getBoolean("chat-detection.subtitle", true),
                getConfig().getBoolean("chat-detection.gui.item", true),
                getConfig().getBoolean("chat-detection.gui.title", true),
                getConfig().getBoolean("chat-detection.scoreboard.title", true),
                getConfig().getBoolean("chat-detection.scoreboard.lines", true),
                getConfig().getBoolean("chat-detection.bossbar", true),
                getConfig().getBoolean("chat-detection.entity", true),
                getConfig().getBoolean("chat-detection.entity-metadata", true),
                getConfig().getBoolean("chat-detection.motd", true),
                getConfig().getBoolean("chat-detection.tab.footer", true),
                getConfig().getBoolean("chat-detection.tab.header", true),
                getConfig().getBoolean("chat-detection.tab.player-info", true)

        );
        ChatDetectionManager.getInstance().addConfiguration(configName, this);

    }

    public ChatDetectionValues getChatDetectionValues() {
        return chatDetectionValues;
    }
}
