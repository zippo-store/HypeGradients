package me.doublenico.hypegradients.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.AbstractPacket;

public class WrapperScoreboardObjective extends AbstractPacket {
    public static final PacketType TYPE =
            PacketType.Play.Server.SCOREBOARD_OBJECTIVE;

    public WrapperScoreboardObjective() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperScoreboardObjective(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Objective DisplayName.
     * <p>
     * Notes: only if mode is 0 or 2. The text to be displayed for the score.
     *
     * @return The current Objective value
     */
    public WrappedChatComponent getDisplayName() {
        return handle.getChatComponents().read(0);
    }

    /**
     * Set Objective DisplayName.
     *
     * @param value - new value.
     */
    public void setDisplayName(WrappedChatComponent value) {
        handle.getChatComponents().write(0, value);
    }
}
