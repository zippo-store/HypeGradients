package me.doublenico.hypegradients.packets.scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedTeamParameters;
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
import me.doublenico.hypegradients.wrappers.scoreboard.WrapperScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class ScoreboardTeamPacket extends MessagePacket {
    public ScoreboardTeamPacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().scoreboardLines();
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperScoreboardTeam wrapper = new WrapperScoreboardTeam(packet);
        Optional<WrappedTeamParameters> teamParameters = wrapper.getTeamParameters();
        if(teamParameters.isEmpty()) return;
        WrappedTeamParameters wrappedTeamParameters = teamParameters.get();
        WrappedChatComponent displayName = wrapper.getDisplayName(wrappedTeamParameters);
        WrappedChatComponent prefix = wrapper.getPrefix(wrappedTeamParameters);
        WrappedChatComponent suffix = wrapper.getSuffix(wrappedTeamParameters);
        if(displayName == null || prefix == null || suffix == null) return;
        String displayNameText = new ChatJson(displayName.getJson()).convertToString();
        String prefixText = new ChatJson(prefix.getJson()).convertToString();
        String suffixText = new ChatJson(suffix.getJson()).convertToString();
        WrapperScoreboardTeam newWrapperScoreboardTeam = new WrapperScoreboardTeam().builder(wrappedTeamParameters);
        changeText(event, newWrapperScoreboardTeam, 0, displayName, displayNameText);
        changeText(event, newWrapperScoreboardTeam, 1, prefix, prefixText);
        changeText(event, newWrapperScoreboardTeam, 2, suffix, suffixText);
        wrappedTeamParameters = newWrapperScoreboardTeam.build();
        wrapper.setTeamParameters(wrappedTeamParameters);
    }

    // computer science breaks me ;(, priority 0, is for display name, 1 is for prefix, 2 is for suffix
    public void changeText(PacketEvent event, WrapperScoreboardTeam scoreboardTeam, int priority, WrappedChatComponent initial, String text){
        MessagePacketEvent messagePacketEvent = new MessagePacketEvent(event.getPlayer(), MessageType.SCOREBOARD_TEAM, text, initial.getJson(), initial);
        Bukkit.getPluginManager().callEvent(messagePacketEvent);
        initial = messagePacketEvent.getChatComponent();
        String json = messagePacketEvent.getJsonMessage();
        text = messagePacketEvent.getPlainMessage();
        ChatGradient gradient = new ChatGradient(text);
        if(gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().scoreboardLines()){
            GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(event.getPlayer(), text, json, gradient.getMessage(), getMessageType());
            Bukkit.getPluginManager().callEvent(gradientModifyEvent);
            gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
            initial.setJson(new ChatJson(gradient.translateGradient()).convertToJson());
            if(priority == 0) scoreboardTeam.setDisplayName(initial);
            if(priority == 1) scoreboardTeam.setPrefix(initial);
            if(priority == 2) scoreboardTeam.setSuffix(initial);
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Scoreboard", "Team");
        }
        if (((HypeGradients) getPlugin()).getMessageDetectionConfig().getChatDetectionValues().scoreboardTitle()) {
            for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                if (!messageDetection.isEnabled(event.getPlayer(), text, json, initial)) continue;
                HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                if (!chatDetectionConfiguration.getChatDetectionValues().scoreboardLines()) continue;
                text = messageDetection.getPlainMessage(event.getPlayer(), text);
                initial.setJson(new ChatJson(text).convertToJson());
                if (priority == 0) scoreboardTeam.setDisplayName(initial);
                if (priority == 1) scoreboardTeam.setPrefix(initial);
                if (priority == 2) scoreboardTeam.setSuffix(initial);
            }
        }

    }
}