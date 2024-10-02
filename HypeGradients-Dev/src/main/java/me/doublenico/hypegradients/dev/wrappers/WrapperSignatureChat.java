package me.doublenico.hypegradients.dev.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

public class WrapperSignatureChat extends AbstractPacket {

    public WrapperSignatureChat(PacketContainer packet) {
        super(packet, PacketType.Play.Server.SYSTEM_CHAT);
    }

    public String getMessage() {
        return handle.getStrings().read(0);
    }

    public void setMessage(String message) {
        handle.getStrings().write(0, message);
    }

    public boolean isActionbar() {
        return handle.getBooleans().read(0);
    }

    @Override
    public WrappedChatComponent getWrappedChatComponent() {
        return WrappedChatComponent.fromJson(getMessage());
    }

    @Override
    public void setWrappedChatComponent(WrappedChatComponent value) {
        setMessage(value.getJson());
    }
}
