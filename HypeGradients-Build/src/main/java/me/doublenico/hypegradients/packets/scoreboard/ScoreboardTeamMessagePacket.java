package me.doublenico.hypegradients.packets.scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedTeamParameters;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import me.doublenico.hypegradients.wrappers.scoreboard.WrapperScoreboardTeam;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class ScoreboardTeamMessagePacket extends MessagePacket {

    public ScoreboardTeamMessagePacket(JavaPlugin plugin, MessagePacketConfigurations configurations, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, configurations, priority, type, messageType);
    }

    @Override
    public boolean register() {
        ((HypeGradients) getPlugin()).getDebugLogger().sendRegisterMessage(getMessagePacketConfigurations().settings().scoreboardLines());
        return getMessagePacketConfigurations().settings().scoreboardLines();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        HypeGradients plugin = (HypeGradients) getPlugin();
        plugin.getDebugLogger().sendMessagePacketMessage(getType(), getMessageType(), getPriority());
        PacketContainer packet = event.getPacket();
        plugin.getDebugLogger().sendWrapperMessage();
        WrapperScoreboardTeam wrapper = new WrapperScoreboardTeam(packet);
        Optional<WrappedTeamParameters> teamParameters = wrapper.getTeamParameters();
        if(teamParameters.isEmpty()) return;
        WrappedTeamParameters wrappedTeamParameters = teamParameters.get();
        WrappedChatComponent displayName = wrapper.getDisplayName(wrappedTeamParameters);
        WrappedChatComponent prefix = wrapper.getPrefix(wrappedTeamParameters);
        WrappedChatComponent suffix = wrapper.getSuffix(wrappedTeamParameters);
        if(displayName == null || prefix == null || suffix == null) return;
        MessagePacketComponents displayComponent = new MessagePacketComponents(displayName, displayName.getJson(), new ChatJson(displayName.getJson()).convertToString());
        MessagePacketComponents prefixComponent = new MessagePacketComponents(prefix, prefix.getJson(), new ChatJson(prefix.getJson()).convertToString());
        MessagePacketComponents suffixComponent = new MessagePacketComponents(suffix, suffix.getJson(), new ChatJson(suffix.getJson()).convertToString());
        MetricsComponents metrics = new MetricsComponents(plugin.getMetricsWrapper(), "Scoreboard", "Lines");
        WrapperScoreboardTeam newWrapperScoreboardTeam = new WrapperScoreboardTeam().builder(wrappedTeamParameters);
        displayComponent = processPacket(event, plugin.getDebugLogger(), metrics, displayComponent);
        prefixComponent = processPacket(event, plugin.getDebugLogger(), metrics, prefixComponent);
        suffixComponent = processPacket(event, plugin.getDebugLogger(), metrics, suffixComponent);
        newWrapperScoreboardTeam.setDisplayName(displayComponent.getWrappedChatComponent())
            .setPrefix(prefixComponent.getWrappedChatComponent())
            .setSuffix(suffixComponent.getWrappedChatComponent());
        wrappedTeamParameters = newWrapperScoreboardTeam.build();
        wrapper.setTeamParameters(wrappedTeamParameters);
    }

}
