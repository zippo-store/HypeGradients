package me.doublenico.hypegradients.commands.impl;

import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacketHandler;
import me.doublenico.hypegradients.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PacketsSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "packets";
    }

    @Override
    public String getPermission() {
        return "hypegradients.packets";
    }

    @Override
    public void execute(HypeGradients plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            MessagePacketHandler.packets.forEach(messagePacket -> {
                sender.sendMessage(messagePacket.toString());
                sender.sendMessage(messagePacket.getType().name());
                sender.sendMessage(messagePacket.getPriority().name());
            });
        }
    }

    @Override
    public void tabCompleter(HypeGradients plugin, CommandSender sender, String[] args, List<String> completions) {

    }
}
