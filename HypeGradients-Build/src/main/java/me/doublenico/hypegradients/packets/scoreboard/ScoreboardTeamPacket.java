package me.doublenico.hypegradients.packets.scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.HypeGradients;
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
        String text = prefixText + suffixText;
        text = text.replace("§f", "");
        String message = new ChatJson(text).convertToJson();
        WrappedChatComponent component = WrappedChatComponent.fromText(text);
        ChatGradient gradient = new ChatGradient(text);
        MessagePacketEvent messagePacketEvent = new MessagePacketEvent(getMessageType(), text, message, component);
        Bukkit.getPluginManager().callEvent(messagePacketEvent);
        message = messagePacketEvent.getJsonMessage();
        text = messagePacketEvent.getPlainMessage();
        component = messagePacketEvent.getChatComponent();
        if (gradient.isGradientTeam() && getChatDetectionConfiguration().getChatDetectionValues().scoreboardTitle()) {
            GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(text, message, gradient.getMessage(), getMessageType());
            Bukkit.getPluginManager().callEvent(gradientModifyEvent);
            gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
            prefix.setJson((new ChatJson(gradient.translateGradient()).convertToJson()));
            wrapper.setPrefix(prefix);
            wrapper.setSuffix(WrappedChatComponent.fromText(""));
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Scoreboard", "Team");
        }
    }
}
