package me.doublenico.hypegradients.dev.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

public class WrapperScoreboardTeam extends AbstractPacket {

    public WrapperScoreboardTeam(PacketContainer packet) {
        super(packet, PacketType.Play.Server.SCOREBOARD_TEAM);

    }

}
