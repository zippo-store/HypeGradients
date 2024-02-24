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
import me.doublenico.hypegradients.wrappers.entity.WrapperMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        List<WrappedChatComponent> components = new ArrayList<>();
        List<WrappedChatComponent> metaComponents = metaData.getMessages();
        metaComponents.forEach(wrappedChatComponent -> {
            String message = wrappedChatComponent.getJson();
            String string = (new ChatJson(message)).convertToString();
            MessagePacketEvent messagePacketEvent = new MessagePacketEvent(player, getMessageType(), string, message, wrappedChatComponent);
            Bukkit.getPluginManager().callEvent(messagePacketEvent);
            message = messagePacketEvent.getJsonMessage();
            string = messagePacketEvent.getPlainMessage();
            wrappedChatComponent = messagePacketEvent.getChatComponent();
            ChatGradient gradient = new ChatGradient(string);
            if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().entityMetadata()) {
                GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, string, message, gradient.getMessage(), getMessageType());
                Bukkit.getPluginManager().callEvent(gradientModifyEvent);
                gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
                wrappedChatComponent.setJson((new ChatJson(gradient.translateGradient())).convertToJson());
                components.add(wrappedChatComponent);
            }
            if (((HypeGradients) getPlugin()).getMessageDetectionConfig().getChatDetectionValues().entityMetadata()) {
                for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                    if (!messageDetection.isEnabled(event.getPlayer(), string, message, wrappedChatComponent)) continue;
                    HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                    ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                    if (!chatDetectionConfiguration.getChatDetectionValues().entityMetadata()) continue;
                    string = messageDetection.getPlainMessage(event.getPlayer(), string);
                    wrappedChatComponent.setJson(new ChatJson(string).convertToJson());
                    components.add(wrappedChatComponent);
                }
                wrappedChatComponent.setJson(new ChatJson(string).convertToJson());
                components.add(wrappedChatComponent);
            }
        });
        for (WrappedChatComponent component : components) {
            metaData.setMessages(component);
        }

    }
}
