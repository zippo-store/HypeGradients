package me.doublenico.hypegradients.api.chat;

import io.dynamicstudios.json.DynamicJText;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * Used to convert a message to JSON format or vice versa
 */
public class ChatJson {

    private final String message;

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
        return DynamicJText.parseText(this.message).json();
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

    public String toString() {
        return "ChatJson{jsonMessage=" + convertToJson()
            + ", stringMessage=" + convertToString()
            + "}";
    }
}
