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
import me.doublenico.hypegradients.wrappers.entity.WrapperArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityMessagePacket extends MessagePacket {

    public EntityMessagePacket(JavaPlugin plugin, MessagePacketConfigurations configurations, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, configurations, priority, type, messageType);
    }

    @Override
    public boolean register() {
        ((HypeGradients) getPlugin()).getDebugLogger().sendRegisterMessage(getMessagePacketConfigurations().settings().entity());
        return getMessagePacketConfigurations().settings().entity();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        HypeGradients plugin = (HypeGradients) getPlugin();
        plugin.getDebugLogger().sendMessagePacketMessage(getType(), getMessageType(), getPriority());
        PacketContainer packet = event.getPacket();
        plugin.getDebugLogger().sendWrapperMessage();
        WrapperArmorStand wrapper = new WrapperArmorStand(packet);
        Entity entity = wrapper.getEntity(event);
        if (entity == null)
            return;
        String name = entity.getCustomName();
        if (name == null) return;
        MessagePacketComponents components = new MessagePacketComponents(WrappedChatComponent.fromLegacyText(name), new ChatJson(name).convertToJson(), name);
        MetricsComponents metrics = new MetricsComponents(plugin.getMetricsWrapper(), "Entity", "Name");
        components = processPacket(event, plugin.getDebugLogger(), metrics, components);
        if (components == null) return;
        wrapper.setWrappedChatComponent(components.getWrappedChatComponent());
    }

}
