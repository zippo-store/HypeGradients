package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacket;
import me.doublenico.hypegradients.chat.ChatGradient;
import me.doublenico.hypegradients.chat.ChatJson;
import me.doublenico.hypegradients.wrappers.WrapperServerInfo;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerInfoPacket extends MessagePacket {
    public ServerInfoPacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    @Override
    public boolean register() {
        return true;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperServerInfo wrapper = new WrapperServerInfo(event.getPacket());
        WrappedChatComponent component = wrapper.getServerPing().getMotD();
        WrappedServerPing ping = wrapper.getServerPing();
        if (component == null)
            return;
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        ChatGradient gradient = new ChatGradient(string);
        if (gradient.isGradient((HypeGradients) getPlugin())) {
            ping.setMotD(gradient.translateGradient((HypeGradients) getPlugin()));
            wrapper.setServerPing(ping);
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("MOTD", "motd");
        }
    }
}
