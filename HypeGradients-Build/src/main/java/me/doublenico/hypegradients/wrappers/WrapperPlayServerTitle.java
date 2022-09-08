package me.doublenico.hypegradients.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.AbstractPacket;

public class WrapperPlayServerTitle extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SET_TITLE_TEXT;

    public WrapperPlayServerTitle() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerTitle(PacketContainer packet) {
        super(packet, TYPE);
    }

    public WrappedChatComponent getTitle() {
        return this.handle.getChatComponents().read(0);
    }

    public void setTitle(WrappedChatComponent value) {
        this.handle.getChatComponents().write(0, value);
    }
}
