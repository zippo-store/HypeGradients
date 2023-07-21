package me.doublenico.hypegradients.packets.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedLevelChunkData;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import org.bukkit.plugin.java.JavaPlugin;

public class SignUpdatePacket extends MessagePacket {
    public SignUpdatePacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    @Override
    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().sign();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        for (WrappedLevelChunkData.BlockEntityInfo info : event.getPacket().getLevelChunkData().read(0).getBlockEntityInfo()) {
            if (info.getAdditionalData() == null) continue;
            if (info.getAdditionalData().containsKey("front_text")) {
                new SignPacketUtil().editSign(info.getAdditionalData(), "front_text", event, getMessageType(), getPlugin(), getChatDetectionConfiguration());
                new SignPacketUtil().editSign(info.getAdditionalData(), "back_text", event, getMessageType(), getPlugin(), getChatDetectionConfiguration());
            }
        }
    }
}
