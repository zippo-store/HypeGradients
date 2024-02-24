package me.doublenico.hypegradients.packets.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import org.bukkit.plugin.java.JavaPlugin;

public class SignLinesPacket extends MessagePacket {
    public SignLinesPacket(JavaPlugin plugin, ChatDetectionConfiguration chatDetectionConfiguration, ListenerPriority priority, PacketType type, MessageType messageType) {
        super(plugin, chatDetectionConfiguration, priority, type, messageType);
    }

    @Override
    public boolean register() {
        return ((HypeGradients) getPlugin()).getSettingsConfig().getChatDetectionValues().sign();
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacket().getNbtModifier().readSafely(0) == null) return;
        new SignPacketUtil().editSign(NbtFactory.asCompound(event.getPacket().getNbtModifier().read(0)), "front_text", event, getMessageType(), getPlugin(), getChatDetectionConfiguration());
        new SignPacketUtil().editSign(NbtFactory.asCompound(event.getPacket().getNbtModifier().read(0)), "back_text", event, getMessageType(), getPlugin(), getChatDetectionConfiguration());
    }


}
