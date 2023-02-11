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
import me.doublenico.hypegradients.wrappers.LegacyWrapperPlayServerTitle;
import org.bukkit.plugin.java.JavaPlugin;

public class LegacyTitleMessagePacket extends MessagePacket {

    public LegacyTitleMessagePacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    @Override
    public boolean register() {
        HypeGradients plugin = (HypeGradients) getPlugin();
        return plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.title", true);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        LegacyWrapperPlayServerTitle wrapper = new LegacyWrapperPlayServerTitle(packet);
        WrappedChatComponent component = wrapper.getTitle();
        if (component == null)
            return;
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        ChatGradient gradient = new ChatGradient(string);
        if (gradient.isGradient((HypeGradients) getPlugin())) {
            component.setJson((new ChatJson(gradient.translateGradient((HypeGradients) getPlugin()))).convertToJson());
            wrapper.setTitle(component);
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Legacy", "Title");
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Legacy", "Subtitle");
        }
    }
}
