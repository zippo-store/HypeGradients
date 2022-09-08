package me.doublenico.hypegradients.commands.impl;

import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.chat.ColorChat;
import me.doublenico.hypegradients.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DebugSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getPermission() {
        return "hypegradients.debug";
    }

    @Override
    public void execute(HypeGradients plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            if ("debug".equals(args[0])) {
                (new ColorChat("[important]/hypegradients [argument]debug [optional]message [required]<message> [description]- Send a debug message")).sendMessage(sender);
                (new ColorChat("[important]/hypegradients [argument]debug [optional]title [required]<message> [description]- Send a debug title")).sendMessage(sender);
                (new ColorChat("[important]/hypegradients [argument]debug [optional]subtitle [required]<message> [description]- Send a debug subtitle")).sendMessage(sender);
                return;
            }
            (new ColorChat("[error]Invalid argument!")).sendMessage(sender);
            return;
        }
        if (args.length > 2) {
            switch (args[1]) {
                case "message" -> {
                    if (!sender.hasPermission("hypegradients.debug.message"))
                        return;
                    StringBuilder message = new StringBuilder();
                    int i;
                    for (i = 2; i < args.length; i++)
                        message.append(args[i]).append(" ");
                    sender.sendMessage(message.toString());
                }
                case "title" -> {
                    if (!sender.hasPermission("hypegradients.debug.title"))
                        return;
                    if (sender instanceof Player player) {
                        StringBuilder message = new StringBuilder();
                        int i;
                        for (i = 2; i < args.length; i++)
                            message.append(args[i]).append(" ");
                        player.sendTitle(message.toString(), "§r", 20, 60, 20);
                    } else (new ColorChat("[error]You must be a player to use this command!")).sendMessage(sender);
                }
                case "subtitle" -> {
                    if (!sender.hasPermission("hypegradients.debug.subtitle"))
                        return;
                    if (sender instanceof Player player) {
                        StringBuilder message = new StringBuilder();
                        int i;
                        for (i = 2; i < args.length; i++)
                            message.append(args[i]).append(" ");
                        player.sendTitle("§r", message.toString(), 20, 60, 20);
                    } else (new ColorChat("[error]You must be a player to use this command!")).sendMessage(sender);
                }
                default -> (new ColorChat("[error]Unknown argument!")).sendMessage(sender);
            }
        }
    }

    @Override
    public void tabCompleter(HypeGradients plugin, CommandSender sender, String[] args, List<String> completions) {
        if (args.length == 1) {
            if (sender.hasPermission("hypegradients.debug.message"))
                completions.add("message");
            if (sender.hasPermission("hypegradients.debug.title"))
                completions.add("title");
            if (sender.hasPermission("hypegradients.debug.subtitle"))
                completions.add("subtitle");
            return;
        }
        if (args.length == 3)
            switch (args[1]) {
                case "message":
                    if (sender.hasPermission("hypegradients.debug.message"))
                        completions.add("<message>");
                    break;
                case "title":
                    if (sender.hasPermission("hypegradients.debug.title"))
                        completions.add("<message>");
                    break;
                case "subtitle":
                    if (sender.hasPermission("hypegradients.debug.subtitle"))
                        completions.add("<message>");
                    break;
            }
    }

}
