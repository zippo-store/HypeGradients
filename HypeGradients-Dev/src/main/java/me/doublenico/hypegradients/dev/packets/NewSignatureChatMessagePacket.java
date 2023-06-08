package me.doublenico.hypegradients.dev.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import org.bukkit.plugin.java.JavaPlugin;

public class NewSignatureChatMessagePacket {

    private final JavaPlugin plugin;
    private final ListenerPriority priority;
    private final PacketType type;

    public NewSignatureChatMessagePacket(JavaPlugin plugin) {
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
