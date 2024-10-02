package me.doublenico.hypegradients.wrappers.title;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

public class LegacyWrapperPlayServerTitle extends AbstractPacket {

    @SuppressWarnings("deprecation")
    public static final PacketType TYPE = PacketType.Play.Server.TITLE;

    public LegacyWrapperPlayServerTitle() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public LegacyWrapperPlayServerTitle(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Action.
     *
     * @return The current Action
     */
    public EnumWrappers.TitleAction getAction() {
        return handle.getTitleActions().read(0);
    }

    /**
     * Set Action.
     *
     * @param value - new value.
     */
    public void setAction(EnumWrappers.TitleAction value) {
        handle.getTitleActions().write(0, value);
    }

    @Override
    public WrappedChatComponent getWrappedChatComponent() {
        return handle.getChatComponents().read(0);
    }

    @Override
    public void setWrappedChatComponent(WrappedChatComponent value) {
        handle.getChatComponents().write(0, value);
    }
}
