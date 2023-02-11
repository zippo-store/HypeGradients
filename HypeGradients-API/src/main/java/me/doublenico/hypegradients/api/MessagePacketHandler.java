package me.doublenico.hypegradients.api;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import java.util.LinkedList;

public class MessagePacketHandler {

    public static LinkedList<MessagePacket> packets = new LinkedList<>();

    public LinkedList<MessagePacket> getPackets() {
        return packets;
    }

    public int getPacketCount() {
        return packets.size();
    }

    public boolean registerPacketListener() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        if (manager == null) return false;
        if (packets.isEmpty()) return false;
        for (MessagePacket packet : packets) {
            if (!packet.register()) {
                packets.remove(packet);
                continue;
            }
            manager.addPacketListener(new PacketAdapter(packet.getPlugin(), packet.getPriority(), packet.getType()) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    packet.onPacketSending(event);
                }
            });
        }
        return true;
    }

    public boolean registerPacketListener(MessagePacket packet) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        if (manager == null) return false;
        if (packet == null) return false;
        if (!packet.register()) {
            packets.remove(packet);
            return false;
        }
        manager.addPacketListener(new PacketAdapter(packet.getPlugin(), packet.getPriority(), packet.getType()) {
            @Override
            public void onPacketSending(PacketEvent event) {
                packet.onPacketSending(event);
            }
        });
        return true;
    }

}
