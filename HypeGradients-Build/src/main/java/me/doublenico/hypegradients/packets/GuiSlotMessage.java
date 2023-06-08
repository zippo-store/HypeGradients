package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacket;
import me.doublenico.hypegradients.chat.ChatGradient;
import me.doublenico.hypegradients.dev.AdventureChatComponent;
import me.doublenico.hypegradients.wrappers.WrapperGuiSlotMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GuiSlotMessage extends MessagePacket {
    public GuiSlotMessage(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    @Override
    public boolean register() {
        HypeGradients plugin = (HypeGradients) getPlugin();
        return plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.gui.item", true);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperGuiSlotMessage wrapper = new WrapperGuiSlotMessage(packet);
        ItemStack item = wrapper.getSlotData();
        if (item == null || item.getType() == Material.AIR || item.getItemMeta() == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
            ChatGradient gradient = new ChatGradient(meta.getDisplayName());
            if (gradient.isGradient(JavaPlugin.getPlugin(HypeGradients.class)))
                meta.setDisplayName(gradient.translateGradient(JavaPlugin.getPlugin(HypeGradients.class)));
            if (((HypeGradients) getPlugin()).getSettingsConfig().getConfig().getBoolean("chat-detection-minimessage.enabled", true) || ((HypeGradients) getPlugin()).getSettingsConfig().getConfig().getBoolean("chat-detection-minimessage.gui.item", true)) {
                AdventureChatComponent component = new AdventureChatComponent(meta.getDisplayName());
                if (component.getFormattedComponent() != null)
                    meta.setDisplayName(component.getFormattedComponent());
            }
        }
        if (meta.getLore() != null && !meta.getLore().isEmpty()) {
            List<String> lore = new ArrayList<>();
            for (String s : meta.getLore()) {
                ChatGradient gradient = new ChatGradient(s);
                if (gradient.isGradient(JavaPlugin.getPlugin(HypeGradients.class)))
                    s = gradient.translateGradient(JavaPlugin.getPlugin(HypeGradients.class));
                if (((HypeGradients) getPlugin()).getSettingsConfig().getConfig().getBoolean("chat-detection-minimessage.enabled", true) || ((HypeGradients) getPlugin()).getSettingsConfig().getConfig().getBoolean("chat-detection-minimessage.gui.item", true)) {
                    AdventureChatComponent component = new AdventureChatComponent(s);
                    if (component.getFormattedComponent() != null)
                        s = component.getFormattedComponent();
                    lore.add(s);
                }
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
        ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
        ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Gui Slot", "Gui");
    }
}
