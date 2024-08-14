package me.doublenico.hypegradients.api.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.event.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MessagePacket {

    private final JavaPlugin plugin;
    private final ListenerPriority priority;
    private final PacketType type;
    private final MessageType messageType;
    private final ChatDetectionConfiguration chatDetectionConfiguration;

    public MessagePacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        this.plugin = plugin;
        this.priority = priority;
        this.type = type;
        this.messageType = messageType;
        this.chatDetectionConfiguration = chatDetectionConfiguration;
        MessagePacketHandler.getPackets().add(this);
        if (new MessagePacketHandler().registerPacketListener(this))
            plugin.getLogger().finest("Registered packet listener for " + type.name());
        else {
            if (register())
                plugin.getLogger().warning("Failed to register packet listener for " + type.name());
        }
    }

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

    public ChatDetectionConfiguration getChatDetectionConfiguration() {
        return chatDetectionConfiguration;
    }

    public String getNMSVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        return v.substring(v.lastIndexOf('.') + 1);
    }

    public boolean supportsSignature() {
        return switch (getNMSVersion()) {
            case "v1_16_R1", "v1_16_R2", "v1_16_R3", "v1_17_R1", "v1_18_R1", "v1_18_R2" -> false;
            default -> true;
        };
    }

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


}
