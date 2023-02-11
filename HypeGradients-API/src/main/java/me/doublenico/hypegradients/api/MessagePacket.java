package me.doublenico.hypegradients.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MessagePacket {

    private final JavaPlugin plugin;
    private final ListenerPriority priority;
    private final PacketType type;

    protected MessagePacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        this.plugin = plugin;
        this.priority = priority;
        this.type = type;
        MessagePacketHandler.packets.add(this);
        if (new MessagePacketHandler().registerPacketListener(this))
            plugin.getLogger().finest("Registered packet listener for " + type.name());
        else
            plugin.getLogger().warning("Failed to register packet listener for " + type.name());
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
