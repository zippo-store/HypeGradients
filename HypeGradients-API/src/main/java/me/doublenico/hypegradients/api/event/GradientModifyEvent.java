package me.doublenico.hypegradients.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GradientModifyEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String initialMessage;
    private final Player player;
    private final String initialMessageJSON;
    private final MessageType messageType;
    private String gradientMessage;

    public GradientModifyEvent(Player player, String initialMessage, String initialMessageJSON, String gradientMessage, MessageType messageType) {
        this.player = player;
        this.initialMessage = initialMessage;
        this.initialMessageJSON = initialMessageJSON;
        this.gradientMessage = gradientMessage;
        this.messageType = messageType;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getInitialMessage() {
        return initialMessage;
    }

    public String getInitialMessageJSON() {
        return initialMessageJSON;
    }

    public String getGradientMessage() {
        return gradientMessage;
    }

    public void setGradientMessage(String gradientMessage) {
        this.gradientMessage = gradientMessage;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
