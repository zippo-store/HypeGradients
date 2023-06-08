package me.doublenico.hypegradients.dev;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.ChatColor;

public record AdventureChatComponent(String message) {

    public String getFormattedComponent() {
        try {
            Component component = MiniMessage.miniMessage().deserializeOrNull(ChatColor.stripColor(message));
            if (component == null) return null;
            return GsonComponentSerializer.gson().serialize(component);
        } catch (Throwable e) {
            return null;
        }
    }

}
