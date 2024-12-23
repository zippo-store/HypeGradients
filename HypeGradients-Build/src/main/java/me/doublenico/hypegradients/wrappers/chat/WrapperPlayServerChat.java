package me.doublenico.hypegradients.wrappers.chat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

public class WrapperPlayServerChat extends AbstractPacket {
    public WrapperPlayServerChat(PacketContainer packet) {
        super(packet, PacketType.Play.Server.CHAT);
    }

    public WrappedChatComponent getMessage() {
        return this.handle.getChatComponents().read(0);
    }

    public void setMessage(WrappedChatComponent value) {
        this.handle.getChatComponents().write(0, value);
    }

    @Override
    public WrappedChatComponent getWrappedChatComponent() {
        return getMessage();
    }

    @Override
    public void setWrappedChatComponent(WrappedChatComponent value) {
        setMessage(value);
    }
}
