package me.doublenico.hypegradients.packets.sign;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtList;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import me.doublenico.hypegradients.api.log.DebugLogger;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;

import java.util.ArrayList;
import java.util.List;

public class SignPacketUtil {

    public void editSign(NbtCompound compound, MessagePacket messagePacket, PacketEvent event, DebugLogger logger, MetricsComponents metrics, String side) {
        if (!compound.containsKey(side)) return;
        NbtList<Object> lines = compound.getCompound(side).getList("messages");
        if (lines == null) return;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String message = lines.getValue(i).toString();
            list.add(message);
            MessagePacketComponents components = new MessagePacketComponents(WrappedChatComponent.fromLegacyText(message), WrappedChatComponent.fromLegacyText(message).getJson(), message);
            components = messagePacket.processPacket(event, logger, metrics, components);
            list.set(i, components.getWrappedChatComponent().getJson());
        }
        if (list.isEmpty()) return;
        lines.asCollection().clear();
        list.forEach(lines::add);
    }
}

