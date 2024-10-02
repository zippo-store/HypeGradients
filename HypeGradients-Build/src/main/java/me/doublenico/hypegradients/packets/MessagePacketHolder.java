package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.detection.ChatDetectionManager;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.log.DebugLogger;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.api.version.ServerVersion;
import me.doublenico.hypegradients.config.MessageDetectionConfig;
import me.doublenico.hypegradients.config.SettingsConfig;
import me.doublenico.hypegradients.packets.boss.BossBarMessagePacket;
import me.doublenico.hypegradients.packets.chat.ChatMessagePacket;
import me.doublenico.hypegradients.packets.chat.NewSignatureMessagePacket;
import me.doublenico.hypegradients.packets.chat.SignatureMessagePacket;
import me.doublenico.hypegradients.packets.entity.EntityMessagePacket;
import me.doublenico.hypegradients.packets.entity.EntityMetaDataMessagePacket;
import me.doublenico.hypegradients.packets.gui.GuiMessagePacket;
import me.doublenico.hypegradients.packets.gui.GuiSlotMessagePacket;
import me.doublenico.hypegradients.packets.gui.GuiTitleMessagePacket;
import me.doublenico.hypegradients.packets.motd.ServerInfoMessagePacket;
import me.doublenico.hypegradients.packets.scoreboard.ScoreboardObjectiveMessagePacket;
import me.doublenico.hypegradients.packets.scoreboard.ScoreboardTeamMessagePacket;
import me.doublenico.hypegradients.packets.sign.SignLinesMessagePacket;
import me.doublenico.hypegradients.packets.sign.SignUpdateMessagePacket;
import me.doublenico.hypegradients.packets.tab.FooterMessagePacket;
import me.doublenico.hypegradients.packets.tab.HeaderMessagePacket;
import me.doublenico.hypegradients.packets.tab.PlayerInfoMessagePacket;
import me.doublenico.hypegradients.packets.title.LegacyTitleMessagePacket;
import me.doublenico.hypegradients.packets.title.SubtitleMessagePacket;
import me.doublenico.hypegradients.packets.title.TitleMessagePacket;

public class MessagePacketHolder {

    @SuppressWarnings("deprecation")
    public void initialisePackets(HypeGradients plugin, SettingsConfig settingsConfig, MessageDetectionConfig messageDetectionConfig, DebugLogger logger) {
        MessagePacketConfigurations configurations = new MessagePacketConfigurations(settingsConfig.getChatDetectionValues(), messageDetectionConfig.getChatDetectionValues(), ChatDetectionManager.getInstance().getConfiguration("gradient"));
        if (new ServerVersion().isLegacy()) {
            new LegacyTitleMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.TITLE, MessageType.TITLE);
        } else {
            new TitleMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.SET_TITLE_TEXT, MessageType.TITLE_TEXT);
            new SubtitleMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.SET_SUBTITLE_TEXT, MessageType.SUBTITLE_TEXT);
        }
        if (new ServerVersion().supportsSignature() && !new ServerVersion().isNewSignature()) {
            new SignatureMessagePacket(plugin, configurations, MessageType.CHAT);
        } else if (new ServerVersion().isNewSignature()) {
            new NewSignatureMessagePacket(plugin, configurations, MessageType.CHAT);
        } else {
            new ChatMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.CHAT, MessageType.CHAT);
        }
        new ServerInfoMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Status.Server.SERVER_INFO, MessageType.MOTD);
        new PlayerInfoMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.PLAYER_INFO, MessageType.PLAYER_INFO);
        new FooterMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER, MessageType.MOTD);
        new HeaderMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER, MessageType.MOTD);
        new EntityMetaDataMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.ENTITY_METADATA, MessageType.METADATA);
        new EntityMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.SPAWN_ENTITY, MessageType.ENTITY);
        new BossBarMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.BOSS, MessageType.BOSSBAR);
        new GuiMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.WINDOW_ITEMS, MessageType.GUI_ITEM);
        new GuiSlotMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.SET_SLOT, MessageType.GUI_ITEM);
        new GuiTitleMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.OPEN_WINDOW, MessageType.GUI_TITLE);
        new ScoreboardTeamMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.SCOREBOARD_TEAM, MessageType.SCOREBOARD_TEAM);
        new ScoreboardObjectiveMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.SCOREBOARD_OBJECTIVE, MessageType.SCOREBOARD_OBJECTIVE);
        new SignLinesMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.TILE_ENTITY_DATA, MessageType.SIGN);
        new SignUpdateMessagePacket(plugin, configurations, ListenerPriority.MONITOR, PacketType.Play.Server.MAP_CHUNK, MessageType.SIGN);
    }
}
