package me.doublenico.hypegradients.api.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ChatJson {
    /**
     * This variable represents a private final string message.
     * <p>
     * It is used to store a message that cannot be modified once assigned.
     */
    private final String message;

    /**
     * Creates a ChatJson object with the given message.
     *
     * @param message The message to include in the ChatJson object.
     */
    public ChatJson(String message) {
        this.message = message;
    }

    /**
     * Converts the message to JSON format.
     *
     * @return The message in JSON format. If the message is null, returns an empty string.
     */
    public String convertToJson() {
        if (this.message == null)
            return "";
        return ComponentSerializer.toString(TextComponent.fromLegacyText(this.message));
    }

    /**
     * Converts the given message to a legacy string representation.
     *
     * @return the converted string representation of the message, If the message is null, returns an empty string
     */
    public String convertToString() {
        if (this.message == null)
            return "";
        return BaseComponent.toLegacyText(ComponentSerializer.parse(this.message));
    }
}
