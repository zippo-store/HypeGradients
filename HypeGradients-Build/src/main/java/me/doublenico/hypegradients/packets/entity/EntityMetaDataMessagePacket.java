package me.doublenico.hypegradients.packets.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import me.doublenico.hypegradients.wrappers.entity.WrapperMetaData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EntityMetaDataMessagePacket extends MessagePacket {

    public EntityMetaDataMessagePacket(JavaPlugin plugin, MessagePacketConfigurations configurations, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, configurations, priority, type, messageType);
    }

    @Override
    public boolean register() {
        ((HypeGradients) getPlugin()).getDebugLogger().sendRegisterMessage(getMessagePacketConfigurations().settings().entityMetadata());
        return getMessagePacketConfigurations().settings().entityMetadata();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        HypeGradients plugin = (HypeGradients) getPlugin();
        plugin.getDebugLogger().sendMessagePacketMessage(getType(), getMessageType(), getPriority());
        PacketContainer packet = event.getPacket();
        plugin.getDebugLogger().sendWrapperMessage();
        WrapperMetaData wrapper = new WrapperMetaData(packet);
        MetricsComponents metrics = new MetricsComponents(plugin.getMetricsWrapper(), "Entity", "Metadata");
        List<WrappedChatComponent> components = new ArrayList<>();
        List<WrappedChatComponent> metaComponents = wrapper.getMessages();
        if (metaComponents == null || metaComponents.isEmpty()) return;
        metaComponents.forEach(component -> {
            MessagePacketComponents packetComponents = new MessagePacketComponents(component, component.getJson(), new ChatJson(component.getJson()).convertToString());
            packetComponents = processPacket(event, plugin.getDebugLogger(), metrics, packetComponents);
            if (packetComponents == null) return;
            components.add(packetComponents.getWrappedChatComponent());
        });
        for(WrappedChatComponent component : components) {
            wrapper.setMessages(component);
        }
    }

}
