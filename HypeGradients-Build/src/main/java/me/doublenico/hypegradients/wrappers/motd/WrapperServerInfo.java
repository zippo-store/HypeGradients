package me.doublenico.hypegradients.wrappers.motd;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

public class WrapperServerInfo extends AbstractPacket {
    public WrapperServerInfo(PacketContainer handle) {
        super(handle, PacketType.Status.Server.SERVER_INFO);
    }

    public WrappedServerPing getServerPing() {
        return handle.getServerPings().read(0);
    }

    public void setServerPing(WrappedServerPing ping) {
        handle.getServerPings().write(0, ping);
    }

}
