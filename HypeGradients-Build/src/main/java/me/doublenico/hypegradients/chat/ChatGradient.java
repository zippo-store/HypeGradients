package me.doublenico.hypegradients.chat;

import dev.dynamicstudios.json.data.util.CColor;
import me.doublenico.hypegradients.HypeGradients;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatGradient {
    private final String gradientRegex = "<gradient:((?:(?=#[a-fA-F\\d]{6}[;>])#[a-fA-F\\d]{6}[^>]?)+)>(.+?)<.gradient>";
    private String message;

    public ChatGradient(String message) {
        this.message = message;
    }

    public String translateGradient(HypeGradients plugin) {
        if (this.message == null)
            return null;
        if (plugin.getSettingsConfig().getConfig().getBoolean("colors", true))
            this.message = (new ColorChat(this.message)).replaceColors(plugin);
        this.message = ChatColor.translateAlternateColorCodes('&', this.message);
        Pattern pattern = Pattern.compile(gradientRegex);
        Matcher matcher = pattern.matcher(this.message);
        while (matcher.find()) {
            String gradient = matcher.group(1);
            String text = matcher.group(2);
            if (gradient.isEmpty()) {
                Bukkit.getLogger().warning("Gradient is empty!");
                continue;
            }
            if (text.isEmpty()) {
                Bukkit.getLogger().warning("Text is empty!");
                continue;
            }
            List<CColor> gradients = new ArrayList<>();
            for (String color : gradient.split(";"))
                gradients.add(CColor.fromHex(color));
            if (gradients.size() < 2) {
                Bukkit.getLogger().warning("Gradient is not valid!");
                continue;
            }
            this.message = this.message.replace(matcher.group(), CColor.translateGradient(text, gradients.toArray(new CColor[0])) + ChatColor.RESET);
        }
        return this.message;
    }

    public boolean isGradient(HypeGradients plugin) {
        if (this.message == null)
            return false;
        this.message = (new ColorChat(this.message)).replaceColors(plugin);
        Pattern pattern = Pattern.compile(gradientRegex);
        Matcher matcher = pattern.matcher(this.message);
        return matcher.find();
    }

    public boolean isGradientTeam(HypeGradients plugin) {
        if (this.message == null)
            return false;
        this.message = (new ColorChat(this.message)).replaceColors(plugin);
        Pattern pattern = Pattern.compile(gradientRegex);
        Matcher matcher = pattern.matcher(ChatColor.stripColor(this.message));
        return matcher.find();
    }


}
