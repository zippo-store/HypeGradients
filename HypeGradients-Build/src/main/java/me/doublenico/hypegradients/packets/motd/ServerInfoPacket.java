package me.doublenico.hypegradients.packets.motd;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessageDetection;
import me.doublenico.hypegradients.api.MessageDetectionManager;
import me.doublenico.hypegradients.api.chat.ChatGradient;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.wrappers.motd.WrapperServerInfo;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerInfoPacket extends MessagePacket {

    public ServerInfoPacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    @Override
    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().motd();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperServerInfo wrapper = new WrapperServerInfo(event.getPacket());
        WrappedChatComponent component = wrapper.getServerPing().getMotD();
        WrappedServerPing ping = wrapper.getServerPing();
        if (component == null)
            return;
        Player player = event.getPlayer();
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        ChatGradient gradient = new ChatGradient(string);
        if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().motd()) {
            ping.setMotD(gradient.translateGradient());
            wrapper.setServerPing(ping);
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("MOTD", "motd");
        }
        if (((HypeGradients) getPlugin()).getMessageDetectionConfig().getChatDetectionValues().motd()){
            for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                if (!messageDetection.isEnabled(event.getPlayer(), string, message, component)) continue;
                HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                if (!chatDetectionConfiguration.getChatDetectionValues().motd()) continue;
                string = messageDetection.getPlainMessage(event.getPlayer(), string);
                ping.setMotD(string);
                wrapper.setServerPing(ping);
            }
            ping.setMotD(string);
            wrapper.setServerPing(ping);
        }
    }
}
