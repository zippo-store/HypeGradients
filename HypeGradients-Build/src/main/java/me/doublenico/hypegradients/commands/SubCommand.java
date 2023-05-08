package me.doublenico.hypegradients.commands;

import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.chat.ColorChat;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getPermission();

    public abstract void execute(HypeGradients plugin, CommandSender sender, String[] args);

    public abstract void tabCompleter(HypeGradients plugin, CommandSender sender, String[] args, List<String> completions);

    public boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            new ColorChat("[error]You don't have permission to use this command").sendMessage(sender);
            return false;
        }
        return true;
    }
}
