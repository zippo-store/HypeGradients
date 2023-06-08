package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacket;
import me.doublenico.hypegradients.chat.ChatGradient;
import me.doublenico.hypegradients.chat.ChatJson;
import me.doublenico.hypegradients.dev.AdventureChatComponent;
import me.doublenico.hypegradients.wrappers.WrapperPlayServerSubtitle;
import org.bukkit.plugin.java.JavaPlugin;

public class SubtitleMessagePacket extends MessagePacket {
    public SubtitleMessagePacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    public boolean register() {
        HypeGradients plugin = (HypeGradients) getPlugin();
        return plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.subtitle", true);
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperPlayServerSubtitle wrapper = new WrapperPlayServerSubtitle(packet);
        WrappedChatComponent component = wrapper.getSubtitle();
        if (component == null)
            return;
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        ChatGradient gradient = new ChatGradient(string);
        if (gradient.isGradient((HypeGradients) getPlugin())) {
            component.setJson((new ChatJson(gradient.translateGradient((HypeGradients) getPlugin()))).convertToJson());
            wrapper.setSubtitle(component);
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Subtitle", "Subtitle");
        } else if (((HypeGradients) getPlugin()).getSettingsConfig().getConfig().getBoolean("chat-detection-minimessage.enabled", true) || ((HypeGradients) getPlugin()).getSettingsConfig().getConfig().getBoolean("chat-detection-minimessage.subtitle", true)) {
            AdventureChatComponent adventureChatComponent = new AdventureChatComponent(string);
            if (adventureChatComponent.getFormattedComponent() == null)
                return;
            component.setJson(adventureChatComponent.getFormattedComponent());
            wrapper.setSubtitle(component);
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Subtitle", "Subtitle");
        }

    }
}
