package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacket;
import me.doublenico.hypegradients.chat.ChatGradient;
import me.doublenico.hypegradients.wrappers.WrapperArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityPacket extends MessagePacket {

    public EntityPacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    @Override
    public boolean register() {
        return true;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperArmorStand armorStand = new WrapperArmorStand(event.getPacket());
        Entity entity = armorStand.getEntity(event);
        if (entity == null) return;
        String name = entity.getCustomName();
        if (name == null) return;
        ChatGradient gradient = new ChatGradient(name);
        if (gradient.isGradient((HypeGradients) getPlugin())) {
            entity.setCustomName(gradient.translateGradient((HypeGradients) getPlugin()));
        }

    }
}
