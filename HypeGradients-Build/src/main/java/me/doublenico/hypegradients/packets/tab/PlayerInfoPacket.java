package me.doublenico.hypegradients.packets.tab;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.chat.ChatGradient;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.event.GradientModifyEvent;
import me.doublenico.hypegradients.api.event.MessagePacketEvent;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.wrappers.tab.WrapperPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfoPacket extends MessagePacket {


    public PlayerInfoPacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    @Override
    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().playerInfo();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperPlayerInfo playerInfo = new WrapperPlayerInfo(event.getPacket());
        List<PlayerInfoData> players = new ArrayList<>();
        List<PlayerInfoData> playerInfoData = playerInfo.getData();
        for (PlayerInfoData data : playerInfo.getData()) {
            if (data.getDisplayName() == null) continue;
            WrappedChatComponent component = data.getDisplayName();
            if (component == null)
                return;
            String message = component.getJson();
            String string = (new ChatJson(message)).convertToString();
            ChatGradient gradient = new ChatGradient(string);
            MessagePacketEvent messagePacketEvent = new MessagePacketEvent(getMessageType(), string, message, component);
            Bukkit.getPluginManager().callEvent(messagePacketEvent);
            message = messagePacketEvent.getJsonMessage();
            string = messagePacketEvent.getPlainMessage();
            component = messagePacketEvent.getChatComponent();
            if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().playerInfo()) {
                playerInfoData.remove(data);
                GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(string, message, gradient.getMessage(), getMessageType());
                Bukkit.getPluginManager().callEvent(gradientModifyEvent);
                gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
                data.getDisplayName().setJson((new ChatJson(gradient.translateGradient())).convertToJson());
                players.add(data);
                if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
                ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
                ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Tab", "Footer");
            }
        }
        if (!players.isEmpty()) {
            playerInfoData.addAll(players);
            playerInfo.setData(playerInfoData);
        }
    }
}
