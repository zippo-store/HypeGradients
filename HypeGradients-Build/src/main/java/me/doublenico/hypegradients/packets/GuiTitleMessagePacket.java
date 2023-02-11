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
import me.doublenico.hypegradients.wrappers.WrapperGuiTitleMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class GuiTitleMessagePacket extends MessagePacket {

    public GuiTitleMessagePacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    @Override
    public boolean register() {
        HypeGradients plugin = (HypeGradients) getPlugin();
        return plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.gui.title", true);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperGuiTitleMessage wrapper = new WrapperGuiTitleMessage(packet);
        WrappedChatComponent component = wrapper.getWindowTitle();
        if (component == null) return;
        String message = component.getJson();
        String string = new ChatJson(message).convertToString();
        ChatGradient gradient = new ChatGradient(string);
        if (gradient.isGradient(JavaPlugin.getPlugin(HypeGradients.class))) {
            component.setJson(new ChatJson(gradient.translateGradient(JavaPlugin.getPlugin(HypeGradients.class))).convertToJson());
            wrapper.setWindowTitle(component);
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Gui Title", "Gui");
        }
    }
}
