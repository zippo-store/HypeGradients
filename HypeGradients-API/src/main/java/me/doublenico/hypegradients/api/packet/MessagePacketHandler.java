package me.doublenico.hypegradients.api.packet;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import java.util.LinkedList;
import java.util.List;

public class MessagePacketHandler {

    private static final List<MessagePacket> packets = new LinkedList<>();


    /**
     * Get the list of packets, should be used only for manipulating the packets
     *
     * @return the list of packets
     */
    public static List<MessagePacket> getPackets() {
        return packets;
    }

    /**
     * @return the amount of packets
     */
    public int getPacketCount() {
        return packets.size();
    }

    /**
     * This will add every packet from the {@link #getPackets()} list to the ProtocolLib packet listener
     *
     * @return true if the packet was added
     */
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

    /**
     * This will add the packet to the ProtocolLib packet listener
     *
     * @param packet the packet to add
     * @return true if the packet was added
     */
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
