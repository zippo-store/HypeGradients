package me.doublenico.hypegradients.api.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.chat.ChatGradient;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.event.GradientModifyEvent;
import me.doublenico.hypegradients.api.event.MessagePacketEvent;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Base class for all message packets
 */
public abstract class MessagePacket {

    private final JavaPlugin plugin;
    private final ListenerPriority priority;
    private final PacketType type;
    private final MessageType messageType;
    private final MessagePacketConfigurations messagePacketConfigurations;

    public MessagePacket(JavaPlugin plugin, MessagePacketConfigurations messagePacketConfigurations, ListenerPriority priority, PacketType type, MessageType messageType) {
        this.plugin = plugin;
        this.priority = priority;
        this.type = type;
        this.messageType = messageType;
        this.messagePacketConfigurations = messagePacketConfigurations;
        MessagePacketHandler.getPackets().add(this);
        if (new MessagePacketHandler().registerPacketListener(this))
            plugin.getLogger().finest("Registered packet listener for " + type.name());
        else {
            if (register())
                plugin.getLogger().warning("Failed to register packet listener for " + type.name());
        }
    }

    /**
     * Checks for conditions, and if true, it will register the packet listener. {@link MessagePacketHandler#registerPacketListener()}
     *
     * @return if the packet should run or not
     */
    public abstract boolean register();

    /**
     * Called when the packet is sent.
     *
     * @param event the packet event
     */
    public abstract void onPacketSending(PacketEvent event);

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public ListenerPriority getPriority() {
        return priority;
    }

    public PacketType getType() {
        return type;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public MessagePacketConfigurations getMessagePacketConfigurations() {
        return messagePacketConfigurations;
    }

    public String getNMSVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        return v.substring(v.lastIndexOf('.') + 1);
    }

    /**
     * This method checks if the server version supports the signature chat, the signature chat appeared in 1.19
     * @return if the server version supports the signature chat
     */
    public boolean supportsSignature() {
        return switch (getNMSVersion()) {
            case "v1_16_R1", "v1_16_R2", "v1_16_R3", "v1_17_R1", "v1_18_R1", "v1_18_R2" -> false;
            default -> true;
        };
    }

    /**
     * This method checks if the server version supports the new signature chat which is a modified version of the first one, the new signature chat appeared in 1.20
     * @return if the server version supports the new signature chat
     */
    public boolean isNewSignature() {
        if (supportsSignature()) {
            switch (getNMSVersion()) {
                case "v1_19_R1", "v1_19_R2" -> {
                    return false;
                }
                default -> {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sets the gradient for the component
     * @param player the player
     * @param gradient the gradient message, see {@link ChatGradient}
     * @param component the chat component where the gradient was found, see {@link WrappedChatComponent}
     * @param jsonMessage the json message of the component
     * @param plainMessage the plain message of the component
     * @return the component with the gradient
     */
    public WrappedChatComponent setGradient(Player player, ChatGradient gradient, WrappedChatComponent component, String jsonMessage, String plainMessage){
        GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, plainMessage, jsonMessage, gradient.getMessage(), getMessageType());
        Bukkit.getPluginManager().callEvent(gradientModifyEvent);
        if (gradientModifyEvent.isCancelled()) return component;
        gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
        component.setJson((new ChatJson(gradient.translateGradient())).convertToJson());

        return component;
    }

    /**
     * Checks if the component is a gradient and if the chat detection for the packet is enabled
     * @param gradient the gradient message, see {@link ChatGradient}
     * @param chatDetection if the chat detection is enabled
     * @return if the component is a gradient
     */
    public boolean isGradient(ChatGradient gradient, boolean chatDetection){
        return gradient.isGradient() && chatDetection;
    }

    /**
     * Gets the initial components of the message packet and modify them in the {@link MessagePacketEvent} event, see {@link MessagePacketComponents}
     * @param player The player
     * @param components The initial components of the message packet
     * @return the modified components after the event or null if the event is cancelled
     */
    public MessagePacketComponents getMessageEvent(Player player, MessagePacketComponents components){
        MessagePacketEvent messagePacketEvent = new MessagePacketEvent(player, getMessageType(), components);
        Bukkit.getPluginManager().callEvent(messagePacketEvent);
        if (messagePacketEvent.isCancelled()) return components;
        return new MessagePacketComponents(messagePacketEvent.getComponents().getWrappedChatComponent(), messagePacketEvent.getComponents().getJsonMessage(), messagePacketEvent.getComponents().getPlainMessage());
    }

}
