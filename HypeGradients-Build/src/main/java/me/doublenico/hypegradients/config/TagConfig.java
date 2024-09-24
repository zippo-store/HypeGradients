package me.doublenico.hypegradients.config;

import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.configuration.Configuration;

public class TagConfig extends Configuration {
    public TagConfig(DynamicConfigurationDirectory configDirectory, String configName, boolean appendMissingKeys) {
        super(configDirectory, configName, appendMissingKeys);
        addDefault("gradient.useDefault", false);
        getConfig().comment("This configuration file is made to change how the plugin will detect the gradient or color",
                "This is how it works:",
                "The plugin will detect the gradient using the prefix and suffix",
                "The prefix is the start of the gradient and the suffix is the end of the gradient",
                "The plugin will detect the colors using the separator",
                "The separator is used to separate the colors in the gradient",
                "The prefixEnd is used to end the prefix and colors",
                "The suffix is used to end the gradient",
                "example: [prefix]color1[separator]color2[prefixEnd]text[suffix]");
        addInlineDefault("gradient.prefix", "<gradient:", "The prefix is the start of the gradient, example: <gradient: <---- the <gradient: is the prefix");
        addInlineDefault("gradient.separator", ";", "The separator is used to separate the colors in the gradient, example: color1;color2;color3 <---- the ; is the separator");
        addInlineDefault("gradient.prefixEnd", ">", "The prefixEnd is used to end the prefix and colors, example: <gradient:color1;color2;color3> <---- the > is the prefixEnd");
        addInlineDefault("gradient.suffix", "</gradient>", "The suffix is used to end the gradient, example: <gradient:color1;color2;color3>{text}</gradient> <---- the </gradient> is the suffix");
        addDefault("color.tag", "<%tag%>");
        getConfig().comment("This is how the colors will be checked, the %tag% will be replaced with color",
            "The system works like this, prefix%tag%suffix",
            "The prefix is the start of the color tag and it's required",
            "The suffix is the end of the color tag and it's not required so you can leave it empty",
            "example: <%tag%> <---- the %tag% will be replaced with red, so it will be <red>",
            "example: -%tag% <---- the %tag% will be replaced with red, so it will be -red");
        addInlineDefault("color.useDefault", false, "If the plugin should use the default color tag");
        getConfig().set("detection", getGradient(), "Check how the gradient will be detected");
    }

    private String getGradient(){
        return getConfig().getString("gradient.prefix", "<gradient:") +
                getConfig().getString("color.tag", "<%tag>").replace("%tag%", "red") +
                getConfig().getString("gradient.separator", ";") +
                "#123456" +
                getConfig().getString("gradient.prefixEnd", ">") +
                getConfig().getString("gradient.suffix", "</gradient>");
    }
}
