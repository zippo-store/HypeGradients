package me.doublenico.hypegradients;

import me.doublenico.hypegradients.utils.Gradient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.List;

public final class HypeGradients extends JavaPlugin {

    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        new GradientPAPI(this).register();
    }

    @Override
    public void onDisable() {

    }

    public String addGradient(String text, Color start, List<Gradient> transition) {
        String newText = "";
        List<Color> gradient = Gradient.createGradient(text.length(), start, transition);
        int index = 0;
        int increment = 1;
        for(char c : text.toCharArray()) {
            newText+=Gradient.of(gradient.get(index)).toString()+c;
            index+=increment;
            if(index >= gradient.size() || index < 0) {
                increment = index < 0 ? 1 : -1;
                index+=increment;
            }
        }
        return newText;
    }

    public String addGradient(String text, Gradient start, List<Gradient> transitions) {
        return addGradient(text, start.getColor(), transitions);
    }

}
