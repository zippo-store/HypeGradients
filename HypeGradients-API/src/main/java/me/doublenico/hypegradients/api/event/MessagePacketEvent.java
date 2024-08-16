package me.doublenico.hypegradients.api.event;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.MessagePacketComponents;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called first when a message packet is sent.
 */
public class MessagePacketEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final MessageType messageType;
    private final Player player;
    private MessagePacketComponents components;
    private boolean cancelled;

    public MessagePacketEvent(Player player, MessageType messageType, MessagePacketComponents components) {
        this.player = player;
        this.messageType = messageType;
        this.components = components;
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

    /**
     * Get the components of the message packet, see {@link MessagePacketComponents}.
     * @return The components of the message packet.
     */
    public MessagePacketComponents getComponents() {
        return components;
    }

    /**
     * Set the components of the message packet, see {@link MessagePacketComponents}.
     * @param components The components of the message packet.
     */
    public void setComponents(MessagePacketComponents components) {
        this.components = components;
    }

    /**
     * Set the event to cancelled, if true the gradient will not be modified.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }


}
