package me.doublenico.hypegradients.packets.tab;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import me.doublenico.hypegradients.wrappers.tab.WrapperPlayerInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfoMessagePacket extends MessagePacket {

    public PlayerInfoMessagePacket(JavaPlugin plugin, MessagePacketConfigurations configurations, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, configurations, priority, type, messageType);
    }

    @Override
    public boolean register() {
        ((HypeGradients) getPlugin()).getDebugLogger().sendRegisterMessage(getMessagePacketConfigurations().settings().playerInfo());
        return getMessagePacketConfigurations().settings().playerInfo();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        HypeGradients plugin = (HypeGradients) getPlugin();
        plugin.getDebugLogger().sendMessagePacketMessage(getType(), getMessageType(), getPriority());
        PacketContainer packet = event.getPacket();
        plugin.getDebugLogger().sendWrapperMessage();
        WrapperPlayerInfo wrapper = new WrapperPlayerInfo(packet);
        MetricsComponents metrics = new MetricsComponents(plugin.getMetricsWrapper(), "Tab", "Players");
        List<PlayerInfoData> players = new ArrayList<>();
        List<PlayerInfoData> playerInfoData = wrapper.getData();
        for (PlayerInfoData player : playerInfoData) {
            if (player.getDisplayName() == null) continue;
            WrappedChatComponent displayName = player.getDisplayName();
            MessagePacketComponents components = new MessagePacketComponents(displayName, displayName.getJson(), new ChatJson(displayName.getJson()).convertToString());
            components = processPacket(event, plugin.getDebugLogger(), metrics, components);
            player.getDisplayName().setJson(components.getWrappedChatComponent().getJson());
            playerInfoData.remove(player);
            players.add(player);
        }
        if (!players.isEmpty()) {
            playerInfoData.addAll(players);
            wrapper.setData(playerInfoData);
        }
    }

}
