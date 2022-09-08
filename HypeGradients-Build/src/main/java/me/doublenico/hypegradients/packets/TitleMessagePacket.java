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
import me.doublenico.hypegradients.wrappers.WrapperPlayServerTitle;
import org.bukkit.plugin.java.JavaPlugin;

public class TitleMessagePacket extends MessagePacket {
    public TitleMessagePacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    public boolean register() {
        HypeGradients plugin = (HypeGradients) getPlugin();
        return plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.title", true);
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperPlayServerTitle wrapper = new WrapperPlayServerTitle(packet);
        WrappedChatComponent component = wrapper.getTitle();
        if (component == null)
            return;
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        ChatGradient gradient = new ChatGradient(string);
        if (gradient.isGradient((HypeGradients) getPlugin())) {
            component.setJson((new ChatJson(gradient.translateGradient((HypeGradients) getPlugin()))).convertToJson());
            wrapper.setTitle(component);
        }
    }
}
