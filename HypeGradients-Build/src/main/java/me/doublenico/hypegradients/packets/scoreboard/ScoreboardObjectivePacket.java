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
import me.doublenico.hypegradients.wrappers.scoreboard.WrapperScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardObjectivePacket extends MessagePacket {

    public ScoreboardObjectivePacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().scoreboardTitle();
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperScoreboardObjective wrapper = new WrapperScoreboardObjective(packet);
        WrappedChatComponent component = wrapper.getDisplayName();
        if (component == null)
            return;
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        MessagePacketEvent messagePacketEvent = new MessagePacketEvent(getMessageType(), string, message, component);
        Bukkit.getPluginManager().callEvent(messagePacketEvent);
        message = messagePacketEvent.getJsonMessage();
        string = messagePacketEvent.getPlainMessage();
        component = messagePacketEvent.getChatComponent();
        ChatGradient gradient = new ChatGradient(string);
        if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().scoreboardTitle()) {
            GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(string, message, gradient.getMessage(), getMessageType());
            Bukkit.getPluginManager().callEvent(gradientModifyEvent);
            gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
            component.setJson((new ChatJson(gradient.translateGradient())).convertToJson());
            wrapper.setDisplayName(component);
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Scoreboard", "Objective");
        } else {
            for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                if (!messageDetection.isEnabled(event.getPlayer(), string, message, component)) continue;
                HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                if (!chatDetectionConfiguration.getChatDetectionValues().scoreboardTitle()) continue;
                string = messageDetection.getPlainMessage(event.getPlayer(), string);
                component.setJson(new ChatJson(string).convertToJson());
                wrapper.setDisplayName(component);
            }
            component.setJson(new ChatJson(string).convertToJson());
            wrapper.setDisplayName(component);
        }
    }
}
