package me.doublenico.hypegradients.commands.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.chat.ColorChat;
import me.doublenico.hypegradients.api.configuration.ConfigurationManager;
import me.doublenico.hypegradients.api.detection.ChatDetectionManager;
import me.doublenico.hypegradients.api.packet.MessagePacketHandler;
import me.doublenico.hypegradients.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
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
                case "configs" -> {
                    if (!hasPermission(sender, "hypegradients.reload.configs")) return;
                    plugin.getColorConfig().checkConfig(plugin);
                    plugin.getColorConfig().getConfig().reload();
                    plugin.getSettingsConfig().getConfig().reload();
                    plugin.getAnimationsConfig().getConfig().reload();
                    plugin.getMessageDetectionConfig().getConfig().reload();
                    plugin.getTagConfig().getConfig().reload();
                    (new ColorChat("[info]Reloaded configs")).sendMessage(sender);
                }
                case "animations" -> {
                    if (!hasPermission(sender, "hypegradients.reload.animations")) return;
                    plugin.getAnimationsConfig().getConfig().reload();
                    (new ColorChat("[info]Reloaded animations list")).sendMessage(sender);
                }
                case "colors" -> {
                    if (!hasPermission(sender, "hypegradients.reload.colors")) return;
                    plugin.getColorConfig().checkConfig(plugin);
                    plugin.getColorConfig().getConfig().reload();
                    (new ColorChat("[info]Reloaded colors list")).sendMessage(sender);
                }
                case "tags" -> {
                    if (!hasPermission(sender, "hypegradients.reload.tags")) return;
                    plugin.getTagConfig().getConfig().reload();
                    (new ColorChat("[info]Reloaded tags")).sendMessage(sender);
                }
                case "settings" -> {
                    if (!hasPermission(sender, "hypegradients.reload.settings")) return;
                    plugin.getSettingsConfig().getConfig().reload();
                    reloadPackets(plugin, sender);
                    (new ColorChat("[info]Reloaded settings")).sendMessage(sender);
                }
                case "all" -> {
                    if (!hasPermission(sender, "hypegradients.reload.all")) return;
                    ConfigurationManager.getInstance().getConfigurations().values().forEach(configuration -> configuration.getConfig().reload());
                    plugin.getColorConfig().checkConfig(plugin);
                    reloadPackets(plugin, sender);
                    (new ColorChat("[info]Reloaded everything")).sendMessage(sender);
                }
                default -> {
                    if (ConfigurationManager.getInstance().getConfiguration(args[1]) != null) {
                        if (!hasPermission(sender, "hypegradients.reload." + args[1])) return;
                        ConfigurationManager.getInstance().getConfiguration(args[1]).getConfig().reload();
                        new ColorChat("[info]Reloaded configuration " + args[1]);
                    }
                }
            }
        } else (new ColorChat("[warn]Unknown argument")).sendMessage(sender);
    }


    public void tabCompleter(HypeGradients plugin, CommandSender sender, String[] args, List<String> completions) {
        if (args.length == 2) {
            completions.addAll(Arrays.asList("colors", "settings", "all", "animations", "configs", "tags"));
            completions.addAll(ConfigurationManager.getInstance().getConfigurations().keySet());
        }
    }

    private void reloadPackets(HypeGradients plugin, CommandSender sender) {
        if (!plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.enabled", true)){
            new ColorChat("[warn]Chat detection is disabled, disabling all packets").sendMessage(sender);
            return;
        }
        plugin.getMessageDetectionConfig().getConfig().reload();
        ChatDetectionManager.getInstance().getChatDetections().forEach(config -> ChatDetectionManager.getInstance().getConfiguration(config.configName()).getConfig().reload());
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.removePacketListeners(plugin);
        MessagePacketHandler.getPackets().clear();
        plugin.initialisePackets();
        new ColorChat("[info]Added [important]" + MessagePacketHandler.getPacketCount() + " [info]packets").sendMessage(sender);
    }
}
