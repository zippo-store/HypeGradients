package me.doublenico.hypegradients.packets.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessageDetection;
import me.doublenico.hypegradients.api.MessageDetectionManager;
import me.doublenico.hypegradients.api.chat.ChatGradient;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.event.GradientModifyEvent;
import me.doublenico.hypegradients.api.event.MessagePacketEvent;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.wrappers.entity.WrapperArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityPacket extends MessagePacket {


    public EntityPacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    @Override
    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().entity();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperArmorStand armorStand = new WrapperArmorStand(event.getPacket());
        Entity entity = armorStand.getEntity(event);
        if (entity == null) return;
        String name = entity.getCustomName();
        if (name == null) return;
        String json = new ChatJson(name).convertToJson();
        WrappedChatComponent component = WrappedChatComponent.fromText(name);
        MessagePacketEvent messagePacketEvent = new MessagePacketEvent(getMessageType(), name, json, component);
        json = messagePacketEvent.getJsonMessage();
        name = messagePacketEvent.getPlainMessage();
        ChatGradient gradient = new ChatGradient(name);
        component = messagePacketEvent.getChatComponent();
        if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().entity()) {
            GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(name, json, gradient.getMessage(), getMessageType());
            Bukkit.getPluginManager().callEvent(gradientModifyEvent);
            gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
            entity.setCustomName(gradient.translateGradient());
        } else {
            for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                if (!messageDetection.isEnabled(event.getPlayer(), name, json, component)) continue;
                HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                if (!chatDetectionConfiguration.getChatDetectionValues().entity()) continue;
                name = messageDetection.getPlainMessage(event.getPlayer(), name);
                entity.setCustomName(name);
            }
            entity.setCustomName(name);
        }
    }
}
