package me.doublenico.hypegradients.redesign;

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
import me.doublenico.hypegradients.wrappers.title.WrapperPlayServerTitle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TitleMessagePacketRedesign extends MessagePacket {

    public TitleMessagePacketRedesign(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    @Override
    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().title();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperPlayServerTitle wrapper = new WrapperPlayServerTitle(packet);
        WrappedChatComponent component = wrapper.getTitle();
        if (component == null)
            return;
        Player player = event.getPlayer();
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        MessagePacketEvent messagePacketEvent = new MessagePacketEvent(player, getMessageType(), string, message, component);
        Bukkit.getPluginManager().callEvent(messagePacketEvent);
        if (messagePacketEvent.isCancelled()) return;
        message = messagePacketEvent.getJsonMessage();
        string = messagePacketEvent.getPlainMessage();
        component = messagePacketEvent.getChatComponent();
        ChatGradient gradient = new ChatGradient(string);
        if (isGradient(gradient, getChatDetectionConfiguration().getChatDetectionValues().title())) {
            component = setGradient(player, gradient, component, message, string);
        }
        wrapper.setTitle(component);
    }

    public WrappedChatComponent setGradient(Player player, ChatGradient gradient, WrappedChatComponent component, String jsonMessage, String plainMessage){
        GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, plainMessage, jsonMessage, gradient.getMessage(), getMessageType());
        Bukkit.getPluginManager().callEvent(gradientModifyEvent);
        if (gradientModifyEvent.isCancelled()) return component;
        gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
        component.setJson((new ChatJson(gradient.translateGradient())).convertToJson());
        setMetrics((HypeGradients) getPlugin(), "Title", "Title");

        return component;
    }

    public boolean isGradient(ChatGradient gradient, boolean title){
        return gradient.isGradient() && title;
    }

    public void setMetrics(HypeGradients plugin, String event, String detectionType){
        if (plugin.getMetricsWrapper() == null) return;
        plugin.getMetricsWrapper().gradientChart();
        plugin.getMetricsWrapper().gradientDetectionChart(event, detectionType);
    }
}
