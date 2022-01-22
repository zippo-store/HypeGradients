package me.doublenico.hypegradients;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.doublenico.hypegradients.utils.Gradient;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GradientPAPI extends PlaceholderExpansion {

    private final HypeGradients plugin;

    public GradientPAPI(HypeGradients plugin){
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "hypegradients";
    }

    @Override
    public @NotNull String getAuthor() {
        return "DoubleNico";
    }

    @Override
    public @Nullable String getRequiredPlugin() {
        return "HypeGradients";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String args) {
        String[] arrayOfString;
        String[] hex;
        List<Gradient> gradients = null;
        String startHex = "";
        String message = "";
        args = PlaceholderAPI.setBracketPlaceholders(player, args);
        int j = (arrayOfString = args.split("(?<!\\\\),")).length;
        for (int i = 0; i < j; i++) {
            String part = arrayOfString[i].replaceAll("\\\\,", ",");
            if(part.startsWith("start:")){
                startHex = part.replace("start:", "");
                continue;
            }
            if (part.startsWith("colors:")) {
                part = part.replace("colors:", "");
                hex = part.split(";");
                gradients = new ArrayList<>();
                for (String s : hex) {
                    gradients.add(Gradient.fromHex(s));
                }
                continue;
            }
            if (part.startsWith("text:")) {
                message = part.replace("text:", "");
                continue;
            }
        }
        return plugin.addGradient(message, Gradient.fromHex(startHex), gradients);
    }
}

