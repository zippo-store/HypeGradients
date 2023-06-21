package me.doublenico.hypegradients.packets.chat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
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
import me.doublenico.hypegradients.wrappers.chat.WrapperPlayServerChat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatMessagePacket extends MessagePacket {


    public ChatMessagePacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    public boolean register() {
        if (((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().chat())
            return !supportsSignature();
        return false;
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperPlayServerChat wrapper = new WrapperPlayServerChat(packet);
        WrappedChatComponent component = wrapper.getMessage();
        if (component == null)
            return;
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        ChatGradient gradient = new ChatGradient(string);
        MessagePacketEvent messagePacketEvent = new MessagePacketEvent(getMessageType(), string, message, component);
        Bukkit.getPluginManager().callEvent(messagePacketEvent);
        message = messagePacketEvent.getJsonMessage();
        string = messagePacketEvent.getPlainMessage();
        component = messagePacketEvent.getChatComponent();
        if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().chat()) {
            GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(string, message, gradient.getMessage(), getMessageType());
            Bukkit.getPluginManager().callEvent(gradientModifyEvent);
            gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
            component.setJson((new ChatJson(gradient.translateGradient())).convertToJson());
            wrapper.setMessage(component);
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Chat", "Chat");
        }
    }
}
