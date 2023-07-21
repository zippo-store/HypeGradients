package me.doublenico.hypegradients.packets.tab;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessageDetection;
import me.doublenico.hypegradients.api.MessageDetectionManager;
import me.doublenico.hypegradients.api.chat.ChatGradient;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.event.GradientModifyEvent;
import me.doublenico.hypegradients.api.event.MessagePacketEvent;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.wrappers.tab.WrapperPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
            Player player = event.getPlayer();
            String message = component.getJson();
            String string = (new ChatJson(message)).convertToString();
            MessagePacketEvent messagePacketEvent = new MessagePacketEvent(player, getMessageType(), string, message, component);
            Bukkit.getPluginManager().callEvent(messagePacketEvent);
            message = messagePacketEvent.getJsonMessage();
            string = messagePacketEvent.getPlainMessage();
            component = messagePacketEvent.getChatComponent();
            ChatGradient gradient = new ChatGradient(string);
            if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().playerInfo()) {
                playerInfoData.remove(data);
                GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, string, message, gradient.getMessage(), getMessageType());
                Bukkit.getPluginManager().callEvent(gradientModifyEvent);
                gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
                data.getDisplayName().setJson((new ChatJson(gradient.translateGradient())).convertToJson());
                players.add(data);
                if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
                ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
                ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Tab", "Players");
            } else {
                for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                    if (!messageDetection.isEnabled(event.getPlayer(), string, message, component)) continue;
                    HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                    ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                    if (!chatDetectionConfiguration.getChatDetectionValues().playerInfo()) continue;
                    string = messageDetection.getPlainMessage(event.getPlayer(), string);
                    data.getDisplayName().setJson(new ChatJson(string).convertToJson());
                    players.add(data);
                }
                data.getDisplayName().setJson(new ChatJson(string).convertToJson());
                players.add(data);
            }
        }
        if (!players.isEmpty()) {
            playerInfoData.addAll(players);
            playerInfo.setData(playerInfoData);
        }
    }
}
