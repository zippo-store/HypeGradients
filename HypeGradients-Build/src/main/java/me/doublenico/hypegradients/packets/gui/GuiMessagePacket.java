package me.doublenico.hypegradients.packets.gui;

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
import me.doublenico.hypegradients.wrappers.gui.WrapperGuiMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GuiMessagePacket extends MessagePacket {


    public GuiMessagePacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().guiItem();
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperGuiMessage wrapper = new WrapperGuiMessage(packet);
        Player player = event.getPlayer();
        for (ItemStack item : wrapper.getSlotData()) {
            if (item == null || item.getType() == Material.AIR || item.getItemMeta() == null) continue;
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                String string = meta.getDisplayName();
                String message = new ChatJson(string).convertToJson();
                WrappedChatComponent component = WrappedChatComponent.fromText(string);
                MessagePacketEvent messagePacketEvent = new MessagePacketEvent(player, getMessageType(), string, message, component);
                Bukkit.getPluginManager().callEvent(messagePacketEvent);
                message = messagePacketEvent.getJsonMessage();
                string = messagePacketEvent.getPlainMessage();
                component = messagePacketEvent.getChatComponent();
                ChatGradient gradient = new ChatGradient(string);
                if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().guiItem()) {
                    GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, string, message, gradient.getMessage(), getMessageType());
                    Bukkit.getPluginManager().callEvent(gradientModifyEvent);
                    gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
                    meta.setDisplayName(gradient.translateGradient());
                }
            }
            if (meta.getLore() != null && !meta.getLore().isEmpty()) {
                List<String> lore = new ArrayList<>();
                for (String s : meta.getLore()) {
                    String message = new ChatJson(s).convertToJson();
                    WrappedChatComponent component = WrappedChatComponent.fromText(s);
                    MessagePacketEvent messagePacketEvent = new MessagePacketEvent(player, getMessageType(), s, message, component);
                    Bukkit.getPluginManager().callEvent(messagePacketEvent);
                    message = messagePacketEvent.getJsonMessage();
                    s = messagePacketEvent.getPlainMessage();
                    component = messagePacketEvent.getChatComponent();
                    ChatGradient gradient = new ChatGradient(s);
                    if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().guiItem()) {
                        GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, s, message, gradient.getMessage(), getMessageType());
                        Bukkit.getPluginManager().callEvent(gradientModifyEvent);
                        gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
                        s = gradient.translateGradient();
                    }
                    if (((HypeGradients) getPlugin()).getMessageDetectionConfig().getChatDetectionValues().guiItem()){
                        for (MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()) {
                            if (!messageDetection.isEnabled(event.getPlayer(), s, message, component)) continue;
                            HypeGradients plugin = JavaPlugin.getPlugin(HypeGradients.class);
                            ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(event.getPlayer(), new DynamicConfigurationDirectory(plugin, plugin.getDataFolder()));
                            if (!chatDetectionConfiguration.getChatDetectionValues().guiItem()) continue;
                            s = messageDetection.getPlainMessage(event.getPlayer(), s);
                        }
                        lore.add(s);
                    } else lore.add(s);
                }
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
        ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
        ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Gui Message", "Gui");
    }
}
