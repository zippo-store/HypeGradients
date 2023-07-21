package me.doublenico.hypegradients.packets.sign;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtList;
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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SignPacketUtil {

    public void editSign(NbtCompound compound, String side, PacketEvent event, MessageType messageType, Plugin gradients, ChatDetectionConfiguration mainChatDetectionConfiguration) {
        NbtList<Object> lines = compound.getCompound(side).getList("messages");
        List<String> list = new ArrayList<>();
        Player player = event.getPlayer();
        for (int i = 0; i < lines.size(); i++) {
            String message = lines.getValue(i).toString();
            list.add(message);
            String string = (new ChatJson(message)).convertToString();
            WrappedChatComponent component = WrappedChatComponent.fromJson(message);
            MessagePacketEvent messagePacketEvent = new MessagePacketEvent(player, messageType, string, message, component);
            Bukkit.getPluginManager().callEvent(messagePacketEvent);
            message = messagePacketEvent.getJsonMessage();
            string = messagePacketEvent.getPlainMessage();
            ChatGradient gradient = new ChatGradient(string);
            component = messagePacketEvent.getChatComponent();
            if (gradient.isGradient() && mainChatDetectionConfiguration.getChatDetectionValues().chat()) {
                GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, string, message, gradient.getMessage(), messageType);
                Bukkit.getPluginManager().callEvent(gradientModifyEvent);
                gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
                component.setJson((new ChatJson(gradient.translateGradient())).convertToJson());
                list.set(i, component.getJson());
                if (((HypeGradients) gradients).getMetricsWrapper() == null) return;
                ((HypeGradients) gradients).getMetricsWrapper().gradientChart();
                ((HypeGradients) gradients).getMetricsWrapper().gradientDetectionChart("Sign", "Line");
            } else {
                for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                    if (!messageDetection.isEnabled(event.getPlayer(), string, message, component)) continue;
                    HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                    ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                    if (!chatDetectionConfiguration.getChatDetectionValues().chat()) continue;
                    string = messageDetection.getPlainMessage(event.getPlayer(), string);
                    component.setJson(new ChatJson(string).convertToJson());
                    list.set(i, component.getJson());
                }
                component.setJson(new ChatJson(string).convertToJson());
                list.set(i, component.getJson());
            }
        }
        if (list.isEmpty()) return;
        lines.asCollection().clear();
        list.forEach(lines::add);
    }
}
