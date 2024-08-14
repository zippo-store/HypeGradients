package me.doublenico.hypegradients.api.event;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MessagePacketEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final MessageType messageType;
    private final Player player;
    private WrappedChatComponent chatComponent;
    private String plainMessage;
    private String jsonMessage;
    private boolean cancelled;

    /**
     * This event is called first when a message packet is sent.
     * @param player The player who sent the message.
     * @param messageType See {@link MessageType}.
     * @param plainMessage The plain message.
     * @param jsonMessage The json message.
     * @param chatComponent The chat component.
     */
    public MessagePacketEvent(Player player, MessageType messageType, String plainMessage, String jsonMessage, WrappedChatComponent chatComponent) {
        this.player = player;
        this.messageType = messageType;
        this.plainMessage = plainMessage;
        this.jsonMessage = jsonMessage;
        this.chatComponent = chatComponent;
        this.cancelled = false;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Check if the event is cancelled, if true the gradient will not be modified.
     * @return True if the event is cancelled.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Get the chat component of the message packet, see {@link WrappedChatComponent}.
     * @return The chat component.
     */
    public WrappedChatComponent getChatComponent() {
        return chatComponent;
    }


    /**
     * Get the player who sent the message.
     *
     * @return The player who sent the message.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return The {@link MessageType} of the message packet.
     */
    public MessageType getMessageType() {
        return messageType;
    }


    public String getPlainMessage() {
        return plainMessage;
    }


    public String getJsonMessage() {
        return jsonMessage;
    }

    public void setPlainMessage(String plainMessage) {
        this.plainMessage = plainMessage;
    }

    public void setJsonMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public void setChatComponent(WrappedChatComponent chatComponent) {
        this.chatComponent = chatComponent;
    }

    /**
     * Set the event to cancelled, if true the gradient will not be modified.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }


}
