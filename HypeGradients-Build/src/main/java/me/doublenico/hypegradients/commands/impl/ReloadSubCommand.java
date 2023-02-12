package me.doublenico.hypegradients.commands.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacketHandler;
import me.doublenico.hypegradients.chat.ColorChat;
import me.doublenico.hypegradients.commands.SubCommand;
import me.doublenico.hypegradients.packets.*;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadSubCommand extends SubCommand {
    public String getName() {
        return "reload";
    }

    public String getPermission() {
        return "hypegradients.reload";
    }

    public void execute(HypeGradients plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            plugin.getColorConfig().getConfig().reload();
            plugin.getSettingsConfig().getConfig().reload();
            (new ColorChat("[info]Reloaded everything")).sendMessage(sender);
        } else if (args.length == 2) {
            switch (args[1]) {
                case "colors" -> {
                    plugin.getColorConfig().getConfig().reload();
                    (new ColorChat("[info]Reloaded colors list")).sendMessage(sender);
                }
                case "settings" -> {
                    plugin.getSettingsConfig().getConfig().reload();
                    reloadPackets(plugin, sender);
                    (new ColorChat("[info]Reloaded settings")).sendMessage(sender);
                }
                case "all" -> {
                    plugin.getColorConfig().getConfig().reload();
                    plugin.getSettingsConfig().getConfig().reload();
                    reloadPackets(plugin, sender);
                    (new ColorChat("[info]Reloaded everything")).sendMessage(sender);
                }
                default -> (new ColorChat("[error]Unknown argument")).sendMessage(sender);
            }
        } else (new ColorChat("[warn]Unknown argument")).sendMessage(sender);
    }


    public void tabCompleter(HypeGradients plugin, CommandSender sender, String[] args, List<String> completions) {
    }

    private void reloadPackets(HypeGradients plugin, CommandSender sender) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.removePacketListeners(plugin);
        MessagePacketHandler.packets.clear();
        if (!plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.enabled", true))
            return;
        if (plugin.isLegacy())
            new LegacyTitleMessagePacket(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.TITLE);
        else {
            new TitleMessagePacket(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.SET_TITLE_TEXT);
            new SubtitleMessagePacket(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.SET_SUBTITLE_TEXT);
        }
        if (plugin.supportsSignature())
            new SignaturePacket(plugin);
        else
            new ChatMessagePacket(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.CHAT);
        new GuiMessagePacket(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.WINDOW_ITEMS);
        new GuiSlotMessage(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.SET_SLOT);
        new GuiTitleMessagePacket(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.OPEN_WINDOW);
        new ScoreboardTeamPacket(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.SCOREBOARD_TEAM);
        new ScoreboardObjectivePacket(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        if (plugin.getPacketHandler().registerPacketListener()) {
            new ColorChat("[info]Registered " + plugin.getPacketHandler().getPacketCount() + " packet listeners.").sendMessage(sender);
            new ColorChat("[info]ProtocolLib packet listener is enabled...").sendMessage(sender);
        } else
            new ColorChat("[error]Could not register ProtocolLib packet listener! Disabling gradient chat detection.").sendMessage(sender);
    }
}
