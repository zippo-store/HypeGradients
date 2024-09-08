package me.doublenico.hypegradients.api.packet.components;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.chat.ChatJson;

/**
 * Represents the components of a message packet that can be modified
 * Contains the {@link WrappedChatComponent}, the json message, and the plain message of the packet
 * <p>
 * The {@link WrappedChatComponent} should be modified to change the message
 */
public class MessagePacketComponents {

    private WrappedChatComponent wrappedChatComponent;
    private String jsonMessage;
    private String plainMessage;

    public MessagePacketComponents(WrappedChatComponent component, String jsonMessage, String plainMessage) {
        this.wrappedChatComponent = component;
        this.jsonMessage = jsonMessage;
        this.plainMessage = plainMessage;
    }

    public WrappedChatComponent getWrappedChatComponent() {
        return wrappedChatComponent;
    }

    public String getPlainMessage() {
        return plainMessage;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }

    public void setWrappedChatComponent(WrappedChatComponent wrappedChatComponent) {
        this.wrappedChatComponent = wrappedChatComponent;
    }

    public void setPlainWrappedChatComponent(String plainMessage) {
        this.wrappedChatComponent = WrappedChatComponent.fromText(plainMessage);
    }

    public void setJsonWrappedChatComponent(String jsonMessage) {
        this.wrappedChatComponent = WrappedChatComponent.fromJson(jsonMessage);
    }

    public void setLegacyWrappedChatComponent(String legacyMessage) {
        this.wrappedChatComponent = WrappedChatComponent.fromLegacyText(legacyMessage);
    }

    public void setJsonMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public void setJsonMessage(WrappedChatComponent wrappedChatComponent) {
        this.jsonMessage = wrappedChatComponent.getJson();
    }

    public void setPlainMessage(String plainMessage) {
        this.plainMessage = plainMessage;
    }

    public void setPlainMessage(WrappedChatComponent wrappedChatComponent) {
        this.plainMessage = new ChatJson(wrappedChatComponent.getJson()).convertToString();
    }
}
