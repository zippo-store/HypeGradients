package me.doublenico.hypegradients.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a gradient is detected.
 */
public class GradientModifyEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final MessageType messageType;
    private final String plainMessage;
    private final String jsonMessage;
    private String gradientMessage;
    private boolean cancelled;

    /**
     * GradientModifyEvent is called when a gradient is detected.
     *
     * @param player          The player who sent the message.
     * @param plainMessage    The plain message.
     * @param jsonMessage     The json message.
     * @param gradientMessage The gradient message.
     * @param messageType     The message type.
     */
    public GradientModifyEvent(Player player, String plainMessage, String jsonMessage, String gradientMessage, MessageType messageType) {
        this.player = player;
        this.plainMessage = plainMessage;
        this.jsonMessage = jsonMessage;
        this.gradientMessage = gradientMessage;
        this.messageType = messageType;
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
     * Get the player who sent the message.
     *
     * @return The player who sent the message.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return The {@link MessageType} of the gradient.
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * This is used for checks, cannot be modified.
     * @return The plain message without colors.
     */
    public String getPlainMessage() {
        return plainMessage;
    }

    /**
     * This is used for checks, cannot be modified.
     * @return The json message.
     */
    public String getJsonMessage() {
        return jsonMessage;
    }

    /**
     * Get the gradient message.
     *
     * @return The gradient message.
     */
    public String getGradientMessage() {
        return gradientMessage;
    }

    /**
     * Set the gradient message.
     *
     * @param gradientMessage The gradient message.
     */
    public void setGradientMessage(String gradientMessage) {
        this.gradientMessage = gradientMessage;
    }

    /**
     * Set the event to cancelled, if true the gradient will not be modified.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}