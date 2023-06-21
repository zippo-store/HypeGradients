package me.doublenico.hypegradients.wrappers.tab;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import me.doublenico.hypegradients.api.packet.AbstractPacket;

import java.util.List;
import java.util.Set;

public class WrapperPlayerInfo extends AbstractPacket {


    public WrapperPlayerInfo(PacketContainer handle) {
        super(handle, PacketType.Play.Server.PLAYER_INFO);
    }

    public Set<EnumWrappers.PlayerInfoAction> getAction() {
        return handle.getPlayerInfoActions().read(0);
    }

    public void setAction(Set<EnumWrappers.PlayerInfoAction> value) {
        handle.getPlayerInfoActions().write(0, value);
    }

    public List<PlayerInfoData> getData() {
        return handle.getPlayerInfoDataLists().read(1);
    }

    public void setData(List<PlayerInfoData> value) {
        handle.getPlayerInfoDataLists().write(1, value);
    }

}
