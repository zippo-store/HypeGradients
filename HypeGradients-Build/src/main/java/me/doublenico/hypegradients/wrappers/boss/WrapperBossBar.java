package me.doublenico.hypegradients.wrappers.boss;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.AbstractPacket;


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

    @Override
    public WrappedChatComponent getWrappedChatComponent() {
        return getTitle();
    }

    @Override
    public void setWrappedChatComponent(WrappedChatComponent value) {
        setTitle(value);
    }
}
