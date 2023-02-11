package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacket;
import me.doublenico.hypegradients.chat.ChatGradient;
import me.doublenico.hypegradients.wrappers.WrapperGuiMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GuiMessagePacket extends MessagePacket {

    public GuiMessagePacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    public boolean register() {
        HypeGradients plugin = (HypeGradients) getPlugin();
        return plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.gui.item", true);
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperGuiMessage wrapper = new WrapperGuiMessage(packet);
        for (ItemStack item : wrapper.getSlotData()) {
            if (item == null || item.getType() == Material.AIR || item.getItemMeta() == null) continue;
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                ChatGradient gradient = new ChatGradient(meta.getDisplayName());
                if (gradient.isGradient(JavaPlugin.getPlugin(HypeGradients.class)))
                    meta.setDisplayName(gradient.translateGradient(JavaPlugin.getPlugin(HypeGradients.class)));

            }
            if (meta.getLore() != null && !meta.getLore().isEmpty()) {
                List<String> lore = new ArrayList<>();
                for (String s : meta.getLore()) {
                    ChatGradient gradient = new ChatGradient(s);
                    if (gradient.isGradient(JavaPlugin.getPlugin(HypeGradients.class)))
                        s = gradient.translateGradient(JavaPlugin.getPlugin(HypeGradients.class));
                    lore.add(s);
                }
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
        ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Gui Message", "Gui");
    }
}
