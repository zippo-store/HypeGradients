package me.doublenico.hypegradients.wrappers.scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

import java.util.Optional;

public class WrapperScoreboardTeam extends AbstractPacket {
    public static final PacketType TYPE =
            PacketType.Play.Server.SCOREBOARD_TEAM;

    public WrapperScoreboardTeam() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperScoreboardTeam(PacketContainer packet) {
        super(packet, TYPE);
    }


    /**
     * Retrieve Team Display Name.
     * <p>
     * Notes: only if Mode = 0 or 2.
     *
     * @return The current Team Display Name
     */
    public Optional<WrappedChatComponent> getDisplayName() {
        if (handle.getOptionalStructures().read(0) == null) return Optional.empty();
        return handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().read(0));
    }

    /**
     * Set Team Display Name.
     *
     * @param value - new value.
     */
    public void setDisplayName(WrappedChatComponent value) {
        handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().write(0, value));
    }

    public Optional<WrappedChatComponent> getPrefix() {
        return handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().read(1));
    }

    public void setPrefix(WrappedChatComponent value) {
        handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().write(1, value));
    }

    public Optional<WrappedChatComponent> getSuffix() {
        return handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().read(2));
    }

    public void setSuffix(WrappedChatComponent value) {
        handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().write(2, value));
    }

}