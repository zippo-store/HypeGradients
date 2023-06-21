package me.doublenico.hypegradients.api.event;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessagePacketEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final MessageType messageType;
    private String plainMessage;
    private String jsonMessage;
    private WrappedChatComponent chatComponent;

    public MessagePacketEvent(MessageType messageType, String plainMessage, String jsonMessage, WrappedChatComponent chatComponent) {
        this.messageType = messageType;
        this.plainMessage = plainMessage;
        this.jsonMessage = jsonMessage;
        this.chatComponent = chatComponent;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getPlainMessage() {
        return plainMessage;
    }

    public void setPlainMessage(String plainMessage) {
        this.plainMessage = plainMessage;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }

    public void setJsonMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public WrappedChatComponent getChatComponent() {
        return chatComponent;
    }

    public void setChatComponent(WrappedChatComponent chatComponent) {
        this.chatComponent = chatComponent;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
