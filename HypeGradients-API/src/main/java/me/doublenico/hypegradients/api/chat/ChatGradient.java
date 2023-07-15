package me.doublenico.hypegradients.api.chat;

import dev.dynamicstudios.json.data.util.CColor;
import dev.perryplaysmc.dynamicconfigurations.IDynamicConfiguration;
import me.doublenico.hypegradients.api.GradientLogger;
import me.doublenico.hypegradients.api.configuration.ConfigurationManager;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatGradient {
    // regex given by @PerryPlaysMC and edited by me Thanks!
    private final String gradientRegex = "<gradient:((?:(?=#[a-fA-F\\d]{6}[;>])#[a-fA-F\\d]{6}[^>]?)+)>(.+?)<\\/gradient>";
    private String message;

    public ChatGradient(String message) {
        this.message = message;
    }

    public String translateGradient() {
        if (this.message == null) return null;
        IDynamicConfiguration settings = ConfigurationManager.getInstance().getConfiguration("settings").getConfig();
        if (settings == null) return new GradientLogger("Settings configuration is null").warn(message);
        if (settings.getBoolean("colors", true)) this.message = (new ColorChat(this.message)).replaceColors();
        this.message = ChatColor.translateAlternateColorCodes('&', this.message);
        Pattern pattern = Pattern.compile(getGradientRegex());
        Matcher matcher = pattern.matcher(this.message);
        while (matcher.find()) {
            String gradient = matcher.group(1);
            String text = matcher.group(2);
            if (gradient.isEmpty()) {
                new GradientLogger("Gradient is empty!").warn();
                continue;
            }
            if (text.isEmpty()) {
                new GradientLogger("Text is empty!").warn();
                continue;
            }
            List<CColor> gradients = new ArrayList<>();
            IDynamicConfiguration tags = ConfigurationManager.getInstance().getConfiguration("tags").getConfig();
            String separator;
            if (tags == null) {
                new GradientLogger("Tags configuration is null, we will use default separator").warn();
                separator = ";";
            } else if (tags.getBoolean("gradient.useDefault", false)) separator = ";";
            else separator = tags.getString("gradient.separator", ";");
            for (String color : gradient.split(separator))
                gradients.add(CColor.fromHex(color));
            if (gradients.size() < 2) {
                new GradientLogger("Gradient is not valid!").warn();
                continue;
            }
            this.message = this.message.replace(matcher.group(), CColor.translateGradient(text, gradients.toArray(new CColor[0])) + ChatColor.RESET);
        }
        return this.message;
    }

    public boolean isGradient() {
        if (this.message == null)
            return false;
        String check = (new ColorChat(this.message)).replaceColors();
        Pattern pattern = Pattern.compile(getGradientRegex());
        Matcher matcher = pattern.matcher(check);
        return matcher.find();
    }

    public boolean isGradientTeam() {
        if (this.message == null)
            return false;
        this.message = (new ColorChat(this.message)).replaceColors();
        Pattern pattern = Pattern.compile(getGradientRegex());
        Matcher matcher = pattern.matcher(ChatColor.stripColor(this.message));
        return matcher.find();
    }

    private String getGradientRegex() {
        IDynamicConfiguration tags = ConfigurationManager.getInstance().getConfiguration("tags").getConfig();
        if (tags == null)
            return new GradientLogger("Tags configuration is null, we will use default regex").warn(gradientRegex);
        if (tags.getBoolean("gradient.useDefault", false)) return gradientRegex;
        String prefix = tags.getString("gradient.prefix", "<gradient:");
        String prefixEnd = tags.getString("gradient.prefixEnd", ">");
        String separator = tags.getString("gradient.separator", ";");
        String suffix = tags.getString("gradient.suffix", "</gradient>");
        char[] regexSymbols = {'.', '*', '+', '?', '^', '$', '[', ']', '-', '(', ')', '|', '/', '{', '}'};
        for (char symbol : regexSymbols) {
            prefix = prefix.replace(String.valueOf(symbol), "\\" + symbol);
            prefixEnd = prefixEnd.replace(String.valueOf(symbol), "\\" + symbol);
            separator = separator.replace(String.valueOf(symbol), "\\" + symbol);
            suffix = suffix.replace(String.valueOf(symbol), "\\" + symbol);
        }
        return prefix + "((?:(?=#[a-fA-F\\d]{6}[" + separator + prefixEnd + "].)#[a-fA-F\\d]{6}[^" + prefixEnd + "]?)+)" + prefixEnd + "(.+?)" + suffix;
    }

    public String getMessage() {
        return message;
    }
}
