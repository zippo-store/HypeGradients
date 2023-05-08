package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import me.doublenico.hypegradients.api.MessagePacket;
import me.doublenico.hypegradients.wrappers.WrapperBossBar;
import org.bukkit.plugin.java.JavaPlugin;

public class BossBarPacket extends MessagePacket {

    public BossBarPacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    @Override
    public boolean register() {
        return true;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperBossBar wrapper = new WrapperBossBar(event.getPacket());
        wrapper.debugBossbar();
    }
}
