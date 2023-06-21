package me.doublenico.hypegradients.packets.chat;

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
import me.doublenico.hypegradients.dev.packets.NewSignatureChatMessagePacket;
import me.doublenico.hypegradients.dev.wrappers.NewWrapperSignatureChat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NewSignaturePacket extends MessagePacket {

    public NewSignaturePacket(JavaPlugin plugin, MessageType messageType, ChatDetectionConfiguration chatDetectionConfiguration) {
        super(new NewSignatureChatMessagePacket(plugin).getPlugin(), chatDetectionConfiguration, new NewSignatureChatMessagePacket(plugin).getPriority(), new NewSignatureChatMessagePacket(plugin).getType(), messageType);
    }

    @Override
    public boolean register() {
        if (((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().chat())
            return isNewSignature();
        return false;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        NewWrapperSignatureChat wrapper = new NewWrapperSignatureChat(packet);
        WrappedChatComponent component = wrapper.getMessage();
        if (component == null) return;
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
            wrapper.setMessage(new ChatJson(gradient.translateGradient()).convertToJson());
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Signature", "Chat");
        }
    }
}
