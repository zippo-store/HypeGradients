package me.doublenico.hypegradients.config;


import io.dynamicstudios.configurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.configuration.Configuration;
import me.doublenico.hypegradients.api.detection.ChatDetectionValues;

public class MessageDetectionConfig extends Configuration {

    private final ChatDetectionValues chatDetectionValues;

    public MessageDetectionConfig(DynamicConfigurationDirectory configDirectory, String configName) {
        super(configDirectory, configName, true);
        addDefault("message-detection.enabled", false);
        getConfig().comment("This will handle all the incoming HypeGradients API chat packets, if you disable this, everything that connects to the API and create a message detection will be disabled");
        addInlineDefault("message-detection.chat", false, "Enable chat message detection");
        addInlineDefault("message-detection.title", false, "Enable title message detection");
        addInlineDefault("message-detection.subtitle", false, "Enable subtitle message detection");
        addInlineDefault("message-detection.gui.item", false, "Enable message detection for items in a gui");
        addInlineDefault("message-detection.gui.title", false, "Enable message detection  the gui title");
        addInlineDefault("message-detection.scoreboard.title", false, "Enable message detection for the scoreboard title");
        addInlineDefault("message-detection.scoreboard.lines", false, "Enable message detection for the scoreboard lines");
        addInlineDefault("message-detection.bossbar", false, "Enable message detection for the bossbar");
        addInlineDefault("message-detection.entity", false, "Enable message detection for entities ");
        addInlineDefault("message-detection.entity-metadata", false, "Enable message detection for entities metadata");
        addInlineDefault("message-detection.motd", false, "Enable message detection for motd");
        addInlineDefault("message-detection.tab.footer", false, "Enable message detection for tab footer");
        addInlineDefault("message-detection.tab.header", false, "Enable message detection for tab header");
        addInlineDefault("message-detection.tab.player-info", false, "Enable message detection for tab player info");
        addInlineDefault("message-detection.sign", false, "Enable message detection for sign");
        chatDetectionValues = new ChatDetectionValues(
            getConfig().getBoolean("message-detection.enabled", false),
            getConfig().getBoolean("message-detection.chat", false),
            getConfig().getBoolean("message-detection.title", false),
            getConfig().getBoolean("message-detection.subtitle", false),
            getConfig().getBoolean("message-detection.gui.item", false),
            getConfig().getBoolean("message-detection.gui.title", false),
            getConfig().getBoolean("message-detection.scoreboard.title", false),
            getConfig().getBoolean("message-detection.scoreboard.lines", false),
            getConfig().getBoolean("message-detection.bossbar", false),
            getConfig().getBoolean("message-detection.entity", false),
            getConfig().getBoolean("message-detection.entity-metadata", false),
            getConfig().getBoolean("message-detection.motd", false),
            getConfig().getBoolean("message-detection.tab.footer", false),
            getConfig().getBoolean("message-detection.tab.header", false),
            getConfig().getBoolean("message-detection.tab.player-info", false),
            getConfig().getBoolean("message-detection.sign", false)
        );
    }

    public ChatDetectionValues getChatDetectionValues() {
        return chatDetectionValues;
    }
}
