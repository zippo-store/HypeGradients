package me.doublenico.hypegradients.dev;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import org.bukkit.plugin.java.JavaPlugin;

public class SignatureChatMessagePacket {

    private final JavaPlugin plugin;
    private final ListenerPriority priority;
    private final PacketType type;

    public SignatureChatMessagePacket(JavaPlugin plugin) {
        this.plugin = plugin;
        this.priority = ListenerPriority.MONITOR;
        this.type = PacketType.Play.Server.SYSTEM_CHAT;
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

}
