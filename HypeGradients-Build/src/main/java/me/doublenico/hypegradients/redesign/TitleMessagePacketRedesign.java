package me.doublenico.hypegradients.redesign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.chat.ChatGradient;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.bstats.Metrics;
import me.doublenico.hypegradients.wrappers.title.WrapperPlayServerTitle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TitleMessagePacketRedesign extends MessagePacket {

    public TitleMessagePacketRedesign(JavaPlugin plugin, MessagePacketConfigurations configurations, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, configurations, priority, type, messageType);
    }

    @Override
    public boolean register() {
        ((HypeGradients) getPlugin()).getDebugLogger().sendRegisterMessage(true);
        return getMessagePacketConfigurations().settings().title();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        HypeGradients plugin = (HypeGradients) getPlugin();
        plugin.getDebugLogger().sendMessagePacketMessage(getType(), getMessageType(), getPriority());
        PacketContainer packet = event.getPacket();
        plugin.getDebugLogger().sendWrapperMessage();
        WrapperPlayServerTitle wrapper = new WrapperPlayServerTitle(packet);
        if (wrapper.getTitle() == null)
            return;
        MessagePacketComponents components = new MessagePacketComponents(wrapper.getTitle(), wrapper.getTitle().getJson(), (new ChatJson(wrapper.getTitle().getJson())).convertToString());
        Player player = event.getPlayer();
        plugin.getDebugLogger().sendComponentMessage(player, components);
        plugin.getDebugLogger().sendMessageEventMessage();
        components = getMessageEvent(player, components);
        if (components == null) return;
        plugin.getDebugLogger().sendComponentMessage(player, components);
        plugin.getDebugLogger().sendBeforeMessage();
        components = getMessageDetection(getPlugin(), player, components, true);
        if (components == null) return;
        plugin.getDebugLogger().sendComponentMessage(player, components);
        plugin.getDebugLogger().sendGradientMessage();
        ChatGradient gradient = new ChatGradient(components.getPlainMessage());
        plugin.getDebugLogger().sendGradientDetectedMessage(gradient, getMessagePacketConfigurations().gradient().getChatDetectionValues().title());
        if (isGradient(gradient, getMessagePacketConfigurations().gradient().getChatDetectionValues().title())) {
            components = setGradient(player, gradient, components);
            plugin.getDebugLogger().sendComponentMessage(player, components);
            new Metrics().setMetrics(plugin, "Title", "Title");
        }
        plugin.getDebugLogger().sendAfterMessage();
        components = getMessageDetection(getPlugin(), player, components, false);
        if (components == null) return;
        plugin.getDebugLogger().sendComponentMessage(player, components);
        wrapper.setTitle(components.getWrappedChatComponent());
    }

}
