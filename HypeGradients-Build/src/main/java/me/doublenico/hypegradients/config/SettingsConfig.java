package me.doublenico.hypegradients.config;


import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.configuration.Configuration;
import me.doublenico.hypegradients.api.detection.ChatDetectionValues;

public class SettingsConfig extends Configuration {

    private final ChatDetectionValues chatDetectionValues;

    public SettingsConfig(DynamicConfigurationDirectory configDirectory, String configName) {
        super(configDirectory, configName, true);
        addInlineDefault("no-found-packet", "disable", "If the packet is not found, stop the server or disable the plugin, values disable or stop");
        addInlineDefault("debug", false, "Enable debug mode, this will log everything that the plugin does");
        addInlineDefault("colors", true, "Enable colors");;
        addInlineDefault("placeholders", true, "Enable placeholders, PlaceholderAPI is required");
        addDefault("chat-packet.enabled", true);
        getConfig().comment("This will handle all the incoming chat packets, if you disable this, everything that the plugin checks(gradient, color, message detection) will be disabled");
        addInlineDefault("chat-packet.chat", true, "Enable chat packet");
        addInlineDefault("chat-packet.title", true, "Enable title packet");
        addInlineDefault("chat-packet.subtitle", true, "Enable subtitle packet");
        addInlineDefault("chat-packet.gui.item", true, "Enable packet for items in a gui");
        addInlineDefault("chat-packet.gui.title", true, "Enable packet  the gui title");
        addInlineDefault("chat-packet.scoreboard.title", true, "Enable packet for the scoreboard title");
        addInlineDefault("chat-packet.scoreboard.lines", true, "Enable packet for the scoreboard lines");
        addInlineDefault("chat-packet.bossbar", true, "Enable packet for the bossbar");
        addInlineDefault("chat-packet.entity", true, "Enable packet for entities ");
        addInlineDefault("chat-packet.entity-metadata", true, "Enable packet for entities metadata");
        addInlineDefault("chat-packet.motd", true, "Enable packet for motd");
        addInlineDefault("chat-packet.tab.footer", true, "Enable packet for tab footer");
        addInlineDefault("chat-packet.tab.header", true, "Enable packet for tab header");
        addInlineDefault("chat-packet.tab.player-info", true, "Enable packet for tab player info");
        addInlineDefault("chat-packet.sign", true, "Enable packet for sign");
        addInlineDefault("bstats.enabled", true, "Enable bStats");
        addInlineDefault("animations.enabled", true, "Enable animations");
        addInlineDefault("translators.warn", "AF1212", "This color will show when a warn message is sent");
        addInlineDefault("translators.error", "FF0000", "This color will show when an error message is sent");
        addInlineDefault("translators.info", "00FF00", "This color will show when an info message is sent");
        addInlineDefault("translators.important", "511DFF", "This color will show when an important message is sent");
        addInlineDefault("translators.argument", "7F54E7", "This color will show when an argument message is sent");
        addInlineDefault("translators.optional", "5560AF", "This color will show when a optional message is sent");
        addInlineDefault("translators.required", "231274", "This color will show when a required message is sent");
        addInlineDefault("translators.description", "323232", "This color will show when a description message is sent");
        chatDetectionValues = new ChatDetectionValues(
                getConfig().getBoolean("chat-packet.enabled", true),
                getConfig().getBoolean("chat-packet.chat", true),
                getConfig().getBoolean("chat-packet.title", true),
                getConfig().getBoolean("chat-packet.subtitle", true),
                getConfig().getBoolean("chat-packet.gui.item", true),
                getConfig().getBoolean("chat-packet.gui.title", true),
                getConfig().getBoolean("chat-packet.scoreboard.title", true),
                getConfig().getBoolean("chat-packet.scoreboard.lines", true),
                getConfig().getBoolean("chat-packet.bossbar", true),
                getConfig().getBoolean("chat-packet.entity", true),
                getConfig().getBoolean("chat-packet.entity-metadata", true),
                getConfig().getBoolean("chat-packet.motd", true),
                getConfig().getBoolean("chat-packet.tab.footer", true),
                getConfig().getBoolean("chat-packet.tab.header", true),
                getConfig().getBoolean("chat-packet.tab.player-info", true),
                getConfig().getBoolean("chat-packet.sign", true)
        );
    }

    public ChatDetectionValues getChatDetectionValues() {
        return chatDetectionValues;
    }
}
