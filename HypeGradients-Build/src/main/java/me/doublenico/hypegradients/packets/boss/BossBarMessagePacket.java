package me.doublenico.hypegradients.packets.boss;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import me.doublenico.hypegradients.wrappers.boss.WrapperBossBar;
import org.bukkit.plugin.java.JavaPlugin;

public class BossBarMessagePacket extends MessagePacket {

    public BossBarMessagePacket(JavaPlugin plugin, MessagePacketConfigurations configurations, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, configurations, priority, type, messageType);
    }

    @Override
    public boolean register() {
        ((HypeGradients) getPlugin()).getDebugLogger().sendRegisterMessage(getMessagePacketConfigurations().settings().bossbar());
        return getMessagePacketConfigurations().settings().bossbar();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        HypeGradients plugin = (HypeGradients) getPlugin();
        plugin.getDebugLogger().sendMessagePacketMessage(getType(), getMessageType(), getPriority());
        PacketContainer packet = event.getPacket();
        plugin.getDebugLogger().sendWrapperMessage();
        WrapperBossBar wrapper = new WrapperBossBar(packet);
        if (wrapper.getWrappedChatComponent() == null)
            return;
        MetricsComponents metrics = new MetricsComponents(plugin.getMetricsWrapper(), "Bossbar", "Title");
        processPacket(event, wrapper, plugin.getDebugLogger(), metrics);
    }

}
