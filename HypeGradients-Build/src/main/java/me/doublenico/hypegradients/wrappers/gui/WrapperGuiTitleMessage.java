package me.doublenico.hypegradients.wrappers.gui;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

public class WrapperGuiTitleMessage extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.OPEN_WINDOW;

    public WrapperGuiTitleMessage() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperGuiTitleMessage(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Window title.
     * <p>
     * Notes: the title of the window.
     *
     * @return The current Window title
     */
    public WrappedChatComponent getWindowTitle() {
        return handle.getChatComponents().read(0);
    }

    /**
     * Set Window title.
     *
     * @param value - new value.
     */
    public void setWindowTitle(WrappedChatComponent value) {
        handle.getChatComponents().write(0, value);
    }


    @Override
    public WrappedChatComponent getWrappedChatComponent() {
        return getWindowTitle();
    }

    @Override
    public void setWrappedChatComponent(WrappedChatComponent value) {
        setWindowTitle(value);
    }
}
