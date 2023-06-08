package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacket;
import me.doublenico.hypegradients.chat.ChatGradient;
import me.doublenico.hypegradients.chat.ChatJson;
import me.doublenico.hypegradients.wrappers.WrapperPlayerInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfoPacket extends MessagePacket {

    public PlayerInfoPacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    @Override
    public boolean register() {
        return true;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperPlayerInfo playerInfo = new WrapperPlayerInfo(event.getPacket());
        List<PlayerInfoData> players = new ArrayList<>();
        List<PlayerInfoData> playerInfoData = playerInfo.getData();
        for (PlayerInfoData data : playerInfo.getData()) {
            if (data.getDisplayName() == null) continue;
            WrappedChatComponent component = data.getDisplayName();
            if (component == null)
                return;
            String message = component.getJson();
            String string = (new ChatJson(message)).convertToString();
            ChatGradient gradient = new ChatGradient(string);
            if (gradient.isGradient((HypeGradients) getPlugin())) {
                playerInfoData.remove(data);
                data.getDisplayName().setJson((new ChatJson(gradient.translateGradient((HypeGradients) getPlugin()))).convertToJson());
                players.add(data);
                if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
                ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
                ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Tab", "Footer");
            }
        }
        if (!players.isEmpty()) {
            playerInfoData.addAll(players);
            playerInfo.setData(playerInfoData);
        }
    }
}
