package me.doublenico.hypegradients.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ChatJson {
    private final String message;

    public ChatJson(String message) {
        this.message = message;
    }

    public String convertToJson() {
        if (this.message == null)
            return "";
        return ComponentSerializer.toString(TextComponent.fromLegacyText(this.message));
    }

    public String convertToString() {
        if (this.message == null)
            return "";
        return BaseComponent.toLegacyText(ComponentSerializer.parse(this.message));
    }
}
