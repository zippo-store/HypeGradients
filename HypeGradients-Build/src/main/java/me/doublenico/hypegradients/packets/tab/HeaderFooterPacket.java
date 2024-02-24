package me.doublenico.hypegradients.packets.tab;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
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
import me.doublenico.hypegradients.wrappers.tab.WrapperHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HeaderFooterPacket extends MessagePacket {


    public HeaderFooterPacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    @Override
    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().header() || ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().footer();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        setFooter(event);
        setHeader(event);
    }

    private void setFooter(PacketEvent event) {
        if (!((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().footer()) return;
        WrapperHeaderFooter wrapper = new WrapperHeaderFooter(event.getPacket());
        WrappedChatComponent component = wrapper.getFooter();
        if (component == null) {
            return;
        }
        Player player = event.getPlayer();
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        MessagePacketEvent messagePacketEvent = new MessagePacketEvent(player, getMessageType(), string, message, component);
        Bukkit.getPluginManager().callEvent(messagePacketEvent);
        message = messagePacketEvent.getJsonMessage();
        string = messagePacketEvent.getPlainMessage();
        component = messagePacketEvent.getChatComponent();
        ChatGradient gradient = new ChatGradient(string);
        if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().footer()) {
            GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, string, message, gradient.getMessage(), getMessageType());
            Bukkit.getPluginManager().callEvent(gradientModifyEvent);
            gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
            component.setJson((new ChatJson(gradient.translateGradient())).convertToJson());
            wrapper.setFooter(component);
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Tab", "Footer");
        }
        if (((HypeGradients) getPlugin()).getMessageDetectionConfig().getChatDetectionValues().footer()){
            for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                if (!messageDetection.isEnabled(event.getPlayer(), string, message, component)) continue;
                HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                if (!chatDetectionConfiguration.getChatDetectionValues().footer()) continue;
                string = messageDetection.getPlainMessage(event.getPlayer(), string);
                component.setJson(new ChatJson(string).convertToJson());
                wrapper.setFooter(component);
            }
            component.setJson(new ChatJson(string).convertToJson());
            wrapper.setFooter(component);
        }
    }

    private void setHeader(PacketEvent event) {
        if (!((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().header()) return;
        WrapperHeaderFooter wrapper = new WrapperHeaderFooter(event.getPacket());
        WrappedChatComponent component = wrapper.getHeader();
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
        if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().header()) {
            Bukkit.getPluginManager().callEvent(new GradientModifyEvent(player, string, message, gradient.getMessage(), getMessageType()));
            component.setJson((new ChatJson(gradient.translateGradient())).convertToJson());
            wrapper.setHeader(component);
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Tab", "Header");
        }
        if (((HypeGradients) getPlugin()).getMessageDetectionConfig().getChatDetectionValues().header()){
            for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                if (!messageDetection.isEnabled(event.getPlayer(), string, message, component)) continue;
                HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                if (!chatDetectionConfiguration.getChatDetectionValues().header()) continue;
                string = messageDetection.getPlainMessage(event.getPlayer(), string);
                component.setJson(new ChatJson(string).convertToJson());
                wrapper.setHeader(component);
            }
            component.setJson(new ChatJson(string).convertToJson());
            wrapper.setHeader(component);
        }
    }
}
