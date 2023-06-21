package me.doublenico.hypegradients.packets.gui;

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
import me.doublenico.hypegradients.wrappers.gui.WrapperGuiSlotMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GuiSlotMessage extends MessagePacket {


    public GuiSlotMessage(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    @Override
    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().guiItem();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperGuiSlotMessage wrapper = new WrapperGuiSlotMessage(packet);
        ItemStack item = wrapper.getSlotData();
        if (item == null || item.getType() == Material.AIR || item.getItemMeta() == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
            String string = meta.getDisplayName();
            String message = new ChatJson(string).convertToJson();
            WrappedChatComponent component = WrappedChatComponent.fromText(string);
            ChatGradient gradient = new ChatGradient(string);
            MessagePacketEvent messagePacketEvent = new MessagePacketEvent(getMessageType(), string, message, component);
            Bukkit.getPluginManager().callEvent(messagePacketEvent);
            message = messagePacketEvent.getJsonMessage();
            string = messagePacketEvent.getPlainMessage();
            component = messagePacketEvent.getChatComponent();
            if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().guiItem()) {
                GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(string, message, gradient.getMessage(), getMessageType());
                Bukkit.getPluginManager().callEvent(gradientModifyEvent);
                gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
                meta.setDisplayName(gradient.translateGradient());
            }
        }
        if (meta.getLore() != null && !meta.getLore().isEmpty()) {
            List<String> lore = new ArrayList<>();
            for (String s : meta.getLore()) {
                ChatGradient gradient = new ChatGradient(s);
                String message = new ChatJson(s).convertToJson();
                WrappedChatComponent component = WrappedChatComponent.fromText(s);
                MessagePacketEvent messagePacketEvent = new MessagePacketEvent(getMessageType(), s, message, component);
                Bukkit.getPluginManager().callEvent(messagePacketEvent);
                message = messagePacketEvent.getJsonMessage();
                s = messagePacketEvent.getPlainMessage();
                component = messagePacketEvent.getChatComponent();
                if (gradient.isGradient() && getChatDetectionConfiguration().getChatDetectionValues().guiItem()) {
                    GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(s, message, gradient.getMessage(), getMessageType());
                    Bukkit.getPluginManager().callEvent(gradientModifyEvent);
                    gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
                    s = gradient.translateGradient();
                }
                lore.add(s);
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
        ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
        ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Gui Slot", "Gui");
    }
}
