package me.doublenico.hypegradients.wrappers.tab;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

public class WrapperHeader extends AbstractPacket {
    public WrapperHeader(PacketContainer handle) {
        super(handle, PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
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
