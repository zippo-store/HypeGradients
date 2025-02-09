package me.doublenico.hypegradients.api.chat;

import io.dynamicstudios.configurations.IDynamicConfiguration;
import io.dynamicstudios.json.data.util.CColor;
import me.doublenico.hypegradients.api.log.GradientLogger;
import me.doublenico.hypegradients.api.configuration.ConfigurationManager;

public class ChatTranslators {
    public ChatTranslators() {
        IDynamicConfiguration configuration = ConfigurationManager.getInstance().getConfiguration("settings").getConfig();
        if (configuration == null) {
            new GradientLogger("Cannot use ChatTranslators because settings are null").warn();
            return;
        }
        CColor.registerColorTranslator(string -> string.replace("[warn]", getHexColor(configuration, "warn")));
        CColor.registerColorTranslator(string -> string.replace("[error]", getHexColor(configuration, "error")));
        CColor.registerColorTranslator(string -> string.replace("[info]", getHexColor(configuration, "info")));
        CColor.registerColorTranslator(string -> string.replace("[important]", getHexColor(configuration, "important")));
        CColor.registerColorTranslator(string -> string.replace("[argument]", getHexColor(configuration, "argument")));
        CColor.registerColorTranslator(string -> string.replace("[optional]", getHexColor(configuration, "optional")));
        CColor.registerColorTranslator(string -> string.replace("[required]", getHexColor(configuration, "required")));
        CColor.registerColorTranslator(string -> string.replace("[description]", getHexColor(configuration, "description")));
    }

    private String getHexColor(IDynamicConfiguration config, String path) {
        String color = config.getString("translators." + path);
        if (color == null) return new GradientLogger("The color" + path + " cannot be found").warn("000000");
        if (!color.matches("[a-fA-F\\d]{6}"))
            return new GradientLogger("The color for ChatTranslators is not a valid hex color").warn("000000");
        return "#" + color;
    }
}
