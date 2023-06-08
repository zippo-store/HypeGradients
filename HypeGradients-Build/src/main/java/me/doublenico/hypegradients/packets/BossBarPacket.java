package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacket;
import me.doublenico.hypegradients.chat.ChatGradient;
import me.doublenico.hypegradients.chat.ChatJson;
import me.doublenico.hypegradients.wrappers.WrapperBossBar;
import org.bukkit.plugin.java.JavaPlugin;

public class BossBarPacket extends MessagePacket {

    public BossBarPacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    @Override
    public boolean register() {
        HypeGradients plugin = (HypeGradients) getPlugin();
        return plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.bossbar", true);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperBossBar wrapper = new WrapperBossBar(event.getPacket());
        WrappedChatComponent component = wrapper.getTitle();
        if (component == null) return;
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        ChatGradient gradient = new ChatGradient(string);
        if (gradient.isGradient((HypeGradients) getPlugin())) {
            component.setJson((new ChatJson(gradient.translateGradient((HypeGradients) getPlugin()))).convertToJson());
            wrapper.setTitle(component);
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Bossbar", "Title");
        }
    }
}
