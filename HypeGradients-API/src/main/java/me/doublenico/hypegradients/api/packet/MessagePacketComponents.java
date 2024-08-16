package me.doublenico.hypegradients.api.packet;

import com.comphenix.protocol.wrappers.WrappedChatComponent;

/**
 * Represents the components of a message packet that can be modified
 * Contains the {@link WrappedChatComponent}, the json message, and the plain message of the packet
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

    public void setWrappedChatComponent(WrappedChatComponent wrappedChatComponent) {
        this.wrappedChatComponent = wrappedChatComponent;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }

    public void setJsonMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public String getPlainMessage() {
        return plainMessage;
    }

    public void setPlainMessage(String plainMessage) {
        this.plainMessage = plainMessage;
    }
}
