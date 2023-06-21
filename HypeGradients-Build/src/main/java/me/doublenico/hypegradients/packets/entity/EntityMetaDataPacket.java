package me.doublenico.hypegradients.packets.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.chat.ChatGradient;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.event.GradientModifyEvent;
import me.doublenico.hypegradients.api.event.MessagePacketEvent;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.wrappers.entity.WrapperMetaData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EntityMetaDataPacket extends MessagePacket {

    public EntityMetaDataPacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    @Override
    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().entityMetadata();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperMetaData metaData = new WrapperMetaData(event.getPacket());
        List<WrappedChatComponent> components = new ArrayList<>();
        List<WrappedChatComponent> metaComponents = metaData.getMessages();
        metaComponents.forEach(wrappedChatComponent -> {
            String message = wrappedChatComponent.getJson();
            String string = (new ChatJson(message)).convertToString();
            MessagePacketEvent messagePacketEvent = new MessagePacketEvent(getMessageType(), string, message, wrappedChatComponent);
            Bukkit.getPluginManager().callEvent(messagePacketEvent);
            message = messagePacketEvent.getJsonMessage();
            string = messagePacketEvent.getPlainMessage();
            wrappedChatComponent = messagePacketEvent.getChatComponent();
            ChatGradient gradient = new ChatGradient(string);
            if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().entityMetadata()) {
                GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(string, message, gradient.getMessage(), getMessageType());
                Bukkit.getPluginManager().callEvent(gradientModifyEvent);
                gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
                wrappedChatComponent.setJson((new ChatJson(gradient.translateGradient())).convertToJson());
                components.add(wrappedChatComponent);
            } else {
                components.add(wrappedChatComponent);
            }
        });
        for (WrappedChatComponent component : components) {
            metaData.setMessages(component);
        }
    }
}
