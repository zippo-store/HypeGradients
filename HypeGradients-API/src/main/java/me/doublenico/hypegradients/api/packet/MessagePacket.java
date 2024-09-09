package me.doublenico.hypegradients.api.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.MessageDetection;
import me.doublenico.hypegradients.api.MessageDetectionManager;
import me.doublenico.hypegradients.api.chat.ChatGradient;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.event.GradientModifyEvent;
import me.doublenico.hypegradients.api.event.MessagePacketEvent;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.annotations.Execute;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.api.packet.enums.DetectionExecution;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
     * Sets the gradient for the component
     * @param player the player
     * @param gradient the gradient message, see {@link ChatGradient}
     * @param components the initial components of the message packet, see {@link MessagePacketComponents}
     * @return the component with the gradient
     */
    public MessagePacketComponents setGradient(Player player, ChatGradient gradient, MessagePacketComponents components) {
        GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, components.getPlainMessage(), components.getJsonMessage(), gradient.getMessage(), getMessageType());
        Bukkit.getPluginManager().callEvent(gradientModifyEvent);
        if (gradientModifyEvent.isCancelled()) return components;
        gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
        components.setPlainMessage(gradient.translateGradient());
        components.setJsonMessage((new ChatJson(gradient.translateGradient())).convertToJson());
        components.setJsonWrappedChatComponent(components.getJsonMessage());

        return components;
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

    /**
     * Gets the message detection for the packet
     * @param plugin the plugin
     * @param player the player
     * @param components the initial components of the message packet
     * @param before if the message detection should be executed before or after the gradient, see {@link DetectionExecution}
     * @return the modified components after the message detection
     */
    public MessagePacketComponents getMessageDetection(JavaPlugin plugin, Player player, MessagePacketComponents components, boolean before){
        for(MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()){
            if (getExecution(messageDetection) != before) continue;
            if(!messageDetection.isEnabled(player, components)) continue;
            ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(player, new DynamicConfigurationDirectory(plugin, new File(plugin.getDataFolder() + "/detections")));
            if(chatDetectionConfiguration == null) continue;
            if(!chatDetectionConfiguration.getChatDetectionValues().title()) continue;
            if (messageDetection.getChatComponent(player, components) == null) return components;
            components = messageDetection.getChatComponent(player, components);
        }
        return components;
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
     * @param detection the message detection
     * @return if the message detection should be executed before or after the gradient, see {@link DetectionExecution}
     */
    public boolean getExecution(MessageDetection detection) {
        Execute priority = detection.getClass().getAnnotation(Execute.class);
        if (priority != null) return priority.value().isBefore();

        return DetectionExecution.AFTER.isBefore();
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


}
