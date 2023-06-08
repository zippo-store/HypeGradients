package me.doublenico.hypegradients.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.AbstractPacket;

public class WrapperHeaderFooter extends AbstractPacket {

    public WrapperHeaderFooter(PacketContainer handle) {
        super(handle, PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
    }

    /**
     * Retrieve Header.
     *
     * @return The current Header
     */
    public WrappedChatComponent getHeader() {
        return handle.getChatComponents().read(0);
    }

    /**
     * Set Header.
     *
     * @param value - new value.
     */
    public void setHeader(WrappedChatComponent value) {
        handle.getChatComponents().write(0, value);
    }

    /**
     * Retrieve Footer.
     *
     * @return The current Footer
     */
    public WrappedChatComponent getFooter() {
        return handle.getChatComponents().read(1);
    }

    /**
     * Set Footer.
     *
     * @param value - new value.
     */
    public void setFooter(WrappedChatComponent value) {
        handle.getChatComponents().write(1, value);
    }

}
