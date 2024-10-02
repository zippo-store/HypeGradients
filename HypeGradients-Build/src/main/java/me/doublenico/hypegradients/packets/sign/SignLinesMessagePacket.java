package me.doublenico.hypegradients.packets.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import org.bukkit.plugin.java.JavaPlugin;

public class SignLinesMessagePacket extends MessagePacket {

    public SignLinesMessagePacket(JavaPlugin plugin, MessagePacketConfigurations configurations, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, configurations, priority, type, messageType);
    }

    @Override
    public boolean register() {
        ((HypeGradients) getPlugin()).getDebugLogger().sendRegisterMessage(getMessagePacketConfigurations().settings().sign());
        return getMessagePacketConfigurations().settings().sign();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        HypeGradients plugin = (HypeGradients) getPlugin();
        plugin.getDebugLogger().sendMessagePacketMessage(getType(), getMessageType(), getPriority());
        PacketContainer packet = event.getPacket();
        plugin.getDebugLogger().sendWrapperMessage();
        if (event.getPacket().getNbtModifier().readSafely(0) == null) return;
        MetricsComponents metrics = new MetricsComponents(plugin.getMetricsWrapper(), "Sign", "Line");
        new SignPacketUtil().editSign(NbtFactory.asCompound(packet.getNbtModifier().read(0)), this, event, plugin.getDebugLogger(), metrics, "front_text");
        new SignPacketUtil().editSign(NbtFactory.asCompound(packet.getNbtModifier().read(0)), this, event, plugin.getDebugLogger(), metrics, "back_text");
    }

}
