package me.doublenico.hypegradients.packets.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedLevelChunkData;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import org.bukkit.plugin.java.JavaPlugin;

public class SignUpdateMessagePacket extends MessagePacket {

    public SignUpdateMessagePacket(JavaPlugin plugin, MessagePacketConfigurations configurations, ListenerPriority priority, PacketType type, MessageType messageType) {
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
        MetricsComponents metrics = new MetricsComponents(plugin.getMetricsWrapper(), "Sign", "Line");
        if (event.getPacket().getNbtModifier().readSafely(0) == null) return;
        for (WrappedLevelChunkData.BlockEntityInfo info : event.getPacket().getLevelChunkData().read(0).getBlockEntityInfo()) {
            if (info.getAdditionalData() == null) continue;
            if (info.getAdditionalData().containsKey("front_text"))
                new SignPacketUtil().editSign(NbtFactory.asCompound(packet.getNbtModifier().read(0)), this, event, plugin.getDebugLogger(), metrics, "front_text");
            else if(info.getAdditionalData().containsKey("back_text"))
                new SignPacketUtil().editSign(NbtFactory.asCompound(packet.getNbtModifier().read(0)), this, event, plugin.getDebugLogger(), metrics, "back_text");

        }
    }

}
