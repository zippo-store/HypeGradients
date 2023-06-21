package me.doublenico.hypegradients.api;

import org.bukkit.Bukkit;

public record GradientLogger(String message) {

    private static final String prefix = "[HypeGradients] ";

    public <T> T warn(T value) {
        Bukkit.getLogger().warning(prefix + message);
        return value;
    }

    public void warn() {
        Bukkit.getLogger().warning(prefix + message);
    }

}
