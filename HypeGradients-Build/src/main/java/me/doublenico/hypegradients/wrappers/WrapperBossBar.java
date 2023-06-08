package me.doublenico.hypegradients.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.AbstractPacket;


public class WrapperBossBar extends AbstractPacket {

    public WrapperBossBar(PacketContainer handle) {
        super(handle, PacketType.Play.Server.BOSS);
    }

    public WrappedChatComponent getTitle() {
        try {
            return handle.getStructures().read(1).getChatComponents().read(0);
        } catch (FieldAccessException ignored) {
        }
        return null;
    }

    public void setTitle(WrappedChatComponent component) {
        try {
            handle.getStructures().read(1).getChatComponents().write(0, component);
        } catch (FieldAccessException ignored) {
        }
    }
}
