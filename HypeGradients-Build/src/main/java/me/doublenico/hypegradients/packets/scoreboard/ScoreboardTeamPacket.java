package me.doublenico.hypegradients.packets.scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
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
import me.doublenico.hypegradients.wrappers.scoreboard.WrapperScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
        if (wrapper.getPrefix().isEmpty())
            return;
        if (wrapper.getSuffix().isEmpty())
            return;
        WrappedChatComponent prefix = wrapper.getPrefix().get();
        WrappedChatComponent suffix = wrapper.getSuffix().get();
        String prefixText = new ChatJson(prefix.getJson()).convertToString();
        String suffixText = new ChatJson(suffix.getJson()).convertToString();
        if (prefixText == null || suffixText == null)
            return;
        Player player = event.getPlayer();
        String text = prefixText + suffixText;
        text = text.replace("§f", "");
        String message = new ChatJson(text).convertToJson();
        WrappedChatComponent component = WrappedChatComponent.fromText(text);
        MessagePacketEvent messagePacketEvent = new MessagePacketEvent(player, getMessageType(), text, message, component);
        Bukkit.getPluginManager().callEvent(messagePacketEvent);
        message = messagePacketEvent.getJsonMessage();
        text = messagePacketEvent.getPlainMessage();
        component = messagePacketEvent.getChatComponent();
        ChatGradient gradient = new ChatGradient(text);
        if (gradient.isGradientTeam() && getChatDetectionConfiguration().getChatDetectionValues().scoreboardTitle()) {
            GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, text, message, gradient.getMessage(), getMessageType());
            Bukkit.getPluginManager().callEvent(gradientModifyEvent);
            gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
            prefix.setJson((new ChatJson(gradient.translateGradient()).convertToJson()));
            wrapper.setPrefix(prefix);
            wrapper.setSuffix(WrappedChatComponent.fromText(""));
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Scoreboard", "Team");
        } else {
            for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                if (!messageDetection.isEnabled(event.getPlayer(), text, message, component)) continue;
                HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                if (!chatDetectionConfiguration.getChatDetectionValues().scoreboardLines()) continue;
                text = messageDetection.getPlainMessage(event.getPlayer(), text);
                prefix.setJson((new ChatJson(text).convertToJson()));
                wrapper.setPrefix(prefix);
                wrapper.setSuffix(WrappedChatComponent.fromText(""));
            }
            prefix.setJson((new ChatJson(text).convertToJson()));
            wrapper.setPrefix(prefix);
            wrapper.setSuffix(WrappedChatComponent.fromText(""));
        }
    }
}