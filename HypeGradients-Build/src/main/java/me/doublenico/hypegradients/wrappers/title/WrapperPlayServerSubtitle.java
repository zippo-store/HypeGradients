package me.doublenico.hypegradients.wrappers.title;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

public class WrapperPlayServerSubtitle extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SET_SUBTITLE_TEXT;

    public WrapperPlayServerSubtitle() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerSubtitle(PacketContainer packet) {
        super(packet, TYPE);
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
