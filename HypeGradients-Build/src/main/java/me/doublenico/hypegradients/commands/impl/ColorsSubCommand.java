package me.doublenico.hypegradients.commands.impl;

import dev.perryplaysmc.dynamicconfigurations.IDynamicConfigurationSection;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.chat.ColorChat;
import me.doublenico.hypegradients.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;

public class ColorsSubCommand extends SubCommand {

    @Override
    public String getName() {
        return "colors";
    }

    @Override
    public String getPermission() {
        return "hypegradient.color";
    }

    @Override
    public void execute(HypeGradients plugin, CommandSender sender, String[] args) {
        if (args.length == 1)
            switch (args[0]) {
                case "add" -> {
                    if (!sender.hasPermission("hypegradients.color.add"))
                        return;
                    (new ColorChat("[important]/hypegradients [argument]colors [optional]add [required]<hex> [description]- Add a color to the colors list")).sendMessage(sender);
                    return;
                }
                case "remove" -> {
                    if (!sender.hasPermission("hypegradients.color.remove"))
                        return;
                    (new ColorChat("[important]/hypegradients [argument]colors [optional]remove [required]<hex> [description]- Remove a color from the colors list")).sendMessage(sender);
                    return;
                }
                case "list" -> {
                    if (!sender.hasPermission("hypegradients.color.list"))
                        return;
                    (new ColorChat("[important]/hypegradients [argument]colors [optional]list [description]- List the colors list")).sendMessage(sender);
                    return;
                }
                default -> {
                    (new ColorChat("[warn]Unknown argument")).sendMessage(sender);
                    return;
                }
            }
        if (args.length >= 2) {
            switch (args[1]) {
                case "add" -> {
                    if (!sender.hasPermission("hypegradients.color.add"))
                        return;
                    if (args.length == 4) {
                        IDynamicConfigurationSection colors = plugin.getColorConfig().getConfig().getSection("colors");
                        if (colors.isSet(args[2])) {
                            (new ColorChat("[error]Color already exists")).sendMessage(sender);
                            return;
                        }
                        for (String color : colors.getKeys(false)) {
                            if (colors.getString(color).equalsIgnoreCase(args[3])) {
                                (new ColorChat("[error]Hex already exists on color " + color)).sendMessage(sender);
                                return;
                            }
                        }
                        if (!args[3].matches("[a-fA-F\\d]{6}")) {
                            (new ColorChat("[error]Invalid color hex code!")).sendMessage(sender);
                            return;
                        }
                        colors.set(args[2], args[3]);
                        (new ColorChat("[info]Color #" + args[3] + args[2] + " [info]was added")).sendMessage(sender);
                    }
                }
                case "remove" -> {
                    if (!sender.hasPermission("hypegradients.color.remove"))
                        return;
                    if (args.length == 3) {
                        IDynamicConfigurationSection colors = plugin.getColorConfig().getConfig().getSection("colors");
                        if (!colors.isSet(args[2])) {
                            (new ColorChat("[error]Color doesn't exist")).sendMessage(sender);
                            return;
                        }
                        colors.set(args[2], null);
                        (new ColorChat("[info]Color " + args[2] + " removed!")).sendMessage(sender);
                    }
                }
                case "list" -> {
                    if (!sender.hasPermission("hypegradients.color.list"))
                        return;
                    IDynamicConfigurationSection colors = plugin.getColorConfig().getConfig().getSection("colors");
                    Set<String> keys = colors.getKeys(false);
                    if (keys.isEmpty()) {
                        (new ColorChat("[error]No colors found")).sendMessage(sender);
                        return;
                    }
                    (new ColorChat("[info]Colors:")).sendMessage(sender);
                    for (String key : keys) {
                        new ColorChat("[info] - " + key + ": #" + colors.getString(key) + "|||||||||").sendMessage(sender);
                    }
                }
                default -> (new ColorChat("[warn]Unknown argument")).sendMessage(sender);

            }
        }
    }

    @Override
    public void tabCompleter(HypeGradients plugin, CommandSender sender, String[] args, List<String> completions) {
        if (args.length == 1) {
            if (sender.hasPermission("hypegradients.color.add"))
                completions.add("add");
            if (sender.hasPermission("hypegradients.color.remove"))
                completions.add("remove");
            if (sender.hasPermission("hypegradients.color.list"))
                completions.add("list");
            return;
        }
        if (args.length == 2)
            switch (args[1]) {
                case "add" -> {
                    completions.add("<name> <hex>");
                    return;
                }
                case "remove" -> {
                    completions.add("<name>");
                    return;
                }
            }
        switch (args[1]) {
            case "add" -> {
                if (args.length == 3)
                    completions.add("<name>");
                if (args.length == 4)
                    completions.add("123456");
            }
            case "remove" -> completions.addAll(plugin.getColorConfig().getColors());
        }
    }
}
