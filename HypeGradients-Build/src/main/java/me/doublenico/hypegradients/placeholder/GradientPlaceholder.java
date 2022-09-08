package me.doublenico.hypegradients.placeholder;

import dev.dynamicstudios.json.data.util.CColor;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.chat.ColorChat;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GradientPlaceholder extends PlaceholderExpansion {
    private final HypeGradients plugin;

    public GradientPlaceholder(HypeGradients plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public String getIdentifier() {
        return "hypegradients";
    }

    @NotNull
    public String getAuthor() {
        return "DoubleNico";
    }

    @Nullable
    public String getRequiredPlugin() {
        return "HypeGradients";
    }

    @NotNull
    public String getVersion() {
        return "1.0.3";
    }

    @NotNull
    public List<String> getPlaceholders() {
        List<String> placeholders = new ArrayList<>();
        placeholders.add("%hypegradients_align:<align>,colors:#HEX;#HEX,text:<text>%");
        placeholders.add("%hypegradients_align:<align>,colors:#HEX;#HEX;<COLOR>,text:<text>%");
        placeholders.add("%hypegradients_colors:#HEX;#HEX;<COLOR>,text:<text>%");
        placeholders.add("%hypegradients_colors:#HEX;#HEX,text:<text>%");
        return placeholders;
    }

    public boolean persist() {
        return true;
    }

    @Nullable
    public String onRequest(OfflinePlayer player, @NotNull String args) {
        List<CColor> gradients = null;
        String message = "";
        CColor.GradientCenter align = CColor.GradientCenter.LEFT;
        args = PlaceholderAPI.setBracketPlaceholders(player, args);
        String[] arrayOfString;
        int j = (arrayOfString = args.split("(?<!\\\\),")).length;
        for (int i = 0; i < j; i++) {
            String part = arrayOfString[i].replaceAll("\\\\,", ",");
            if (part.startsWith("colors:")) {
                part = part.replace("colors:", "");
                String[] hex = part.split(";");
                gradients = new ArrayList<>();
                for (String s : hex) {
                    if (plugin.getSettingsConfig().getConfig().getBoolean("colors", true))
                        s = (new ColorChat(s)).replaceColors(this.plugin);
                    if (!s.matches("#[a-fA-F\\d]{6}")) {
                        this.plugin.getLogger().warning("Invalid hex color: " + s);
                        return message;
                    }
                    gradients.add(CColor.fromHex(s));
                }
            } else if (part.startsWith("text:")) {
                message = part.replace("text:", "");
                message = ChatColor.translateAlternateColorCodes('&', message);
            } else if (part.startsWith("align:")) {
                if (part.replace("align:", "").equalsIgnoreCase("left")) align = CColor.GradientCenter.LEFT;
                else if (part.replace("align:", "").equalsIgnoreCase("right")) align = CColor.GradientCenter.RIGHT;
                else if (part.replace("align:", "").equalsIgnoreCase("middle")) align = CColor.GradientCenter.MIDDLE;
            }
        }
        if (gradients == null) return message;
        if (gradients.size() < 2) {
            this.plugin.getLogger().warning("You must have more than 2 colors, yours: " + gradients.size());
            return message;
        }
        return CColor.translateGradient(message, align, gradients.toArray(new CColor[0]));
    }
}
