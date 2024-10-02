package me.doublenico.hypegradients.packets.chat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.wrappers.chat.WrapperPlayServerChat;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatMessagePacket extends MessagePacket {
    public ChatMessagePacket(JavaPlugin plugin, MessagePacketConfigurations messagePacketConfigurations, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, messagePacketConfigurations, priority, type, messageType);
    }

    @Override
    public boolean register() {
        ((HypeGradients) getPlugin()).getDebugLogger().sendRegisterMessage(getMessagePacketConfigurations().settings().chat());
        return getMessagePacketConfigurations().settings().chat();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        HypeGradients plugin = (HypeGradients) getPlugin();
        plugin.getDebugLogger().sendMessagePacketMessage(getType(), getMessageType(), getPriority());
        PacketContainer packet = event.getPacket();
        plugin.getDebugLogger().sendWrapperMessage();
        WrapperPlayServerChat wrapper = new WrapperPlayServerChat(packet);
        if (wrapper.getWrappedChatComponent() == null)
            return;
        MetricsComponents metrics = new MetricsComponents(plugin.getMetricsWrapper(), "Chat", "Chat");
        processPacket(event, wrapper, plugin.getDebugLogger(), metrics);
    }
}
