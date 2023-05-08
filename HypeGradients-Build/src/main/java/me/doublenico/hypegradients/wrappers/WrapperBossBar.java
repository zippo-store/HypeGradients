package me.doublenico.hypegradients.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.doublenico.hypegradients.api.AbstractPacket;

public class WrapperBossBar extends AbstractPacket {
    /**
     * Constructs a new strongly typed wrapper for the given packet.
     *
     * @param handle - handle to the raw packet data.
     * @param type   - the packet type.
     */
    protected WrapperBossBar(PacketContainer handle, PacketType type) {
        super(handle, type);
    }

    public WrapperBossBar(PacketContainer handle) {
        super(handle, PacketType.Play.Server.BOSS);
    }

    public void debugBossbar() {
        System.out.println(handle);
        System.out.println(handle.getSpecificModifier(Class.class).read(0));

    }

    public Action getAction() {
        return handle.getEnumModifier(Action.class, 1).read(0);
    }

    public void setAction(Action value) {
        handle.getEnumModifier(Action.class, 1).write(0, value);
    }


    public static enum Action {
        ADD, REMOVE, UPDATE_PCT, UPDATE_NAME, UPDATE_STYLE, UPDATE_PROPERTIES;
    }
}
