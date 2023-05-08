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
import me.doublenico.hypegradients.wrappers.WrapperScoreboardTeam;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardTeamPacket extends MessagePacket {
    public ScoreboardTeamPacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    public boolean register() {
        HypeGradients plugin = (HypeGradients) getPlugin();
        return plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.scoreboard.lines", true);
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperScoreboardTeam wrapper = new WrapperScoreboardTeam(packet);
        if (!wrapper.getPrefix().isPresent())
            return;
        if (!wrapper.getSuffix().isPresent())
            return;
        WrappedChatComponent prefix = wrapper.getPrefix().get();
        WrappedChatComponent suffix = wrapper.getSuffix().get();
        String prefixText = new ChatJson(prefix.getJson()).convertToString();
        String suffixText = new ChatJson(suffix.getJson()).convertToString();
        if (prefixText == null || suffixText == null)
            return;
        String text = prefixText + suffixText;
        text = text.replace("§f", "");
        ChatGradient gradient = new ChatGradient(text);
        if (gradient.isGradientTeam((HypeGradients) getPlugin())) {
            prefix.setJson((new ChatJson(gradient.translateGradient((HypeGradients) getPlugin()))).convertToJson());
            wrapper.setPrefix(prefix);
            wrapper.setSuffix(WrappedChatComponent.fromText(""));
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Scoreboard", "Team");
        }

    }
}
