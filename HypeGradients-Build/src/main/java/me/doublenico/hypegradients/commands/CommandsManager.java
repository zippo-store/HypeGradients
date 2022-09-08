package me.doublenico.hypegradients.commands;

import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.chat.ColorChat;
import me.doublenico.hypegradients.commands.impl.ColorsSubCommand;
import me.doublenico.hypegradients.commands.impl.DebugSubCommand;
import me.doublenico.hypegradients.commands.impl.ReloadSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class CommandsManager implements CommandExecutor, TabExecutor {
    private final LinkedList<SubCommand> subcmds = new LinkedList<>();

    private final HypeGradients plugin;

    public CommandsManager(HypeGradients plugin) {
        this.plugin = plugin;
        this.subcmds.add(new ColorsSubCommand());
        this.subcmds.add(new ReloadSubCommand());
        this.subcmds.add(new DebugSubCommand());
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SubCommand subCommand = null;
        if (args.length == 0) {
            if (!sender.hasPermission("hypegradients.default"))
                return true;
            (new ColorChat("[info]HypeGradient Commands")).sendMessage(sender);
            (new ColorChat("[info]Arguments:")).sendMessage(sender);
            (new ColorChat("[info]important = [important]|[info] argument = [argument]|[info] optional = [optional]|[info] required = [required]|")).sendMessage(sender);
            (new ColorChat("")).sendMessage(sender);
            (new ColorChat("[important]/hypegradients [argument]colors [optional]add [required]<hex> [description]- Add a color to the colors list")).sendMessage(sender);
            (new ColorChat("[important]/hypegradients [argument]colors [optional]remove [required]<hex> [description]- Remove a color from the colors list")).sendMessage(sender);
            (new ColorChat("[important]/hypegradients [argument]colors [optional]list [description]- List the colors list")).sendMessage(sender);
            (new ColorChat("[important]/hypegradients [argument]reload [optional]colors [description]- Reload colors list")).sendMessage(sender);
            (new ColorChat("[important]/hypegradients [argument]reload [optional]settings [description]- Reload the settings")).sendMessage(sender);
            (new ColorChat("[important]/hypegradients [argument]reload [optional]all [description]- Reload everything")).sendMessage(sender);
            (new ColorChat("[important]/hypegradients [argument]debug [optional]message [required]<message> [description]- Send a debug message")).sendMessage(sender);
            (new ColorChat("[important]/hypegradients [argument]debug [optional]title [required]<message> [description]- Send a debug title")).sendMessage(sender);
            (new ColorChat("[important]/hypegradients [argument]debug [optional]subtitle [required]<message> [description]- Send a debug subtitle")).sendMessage(sender);
            return true;
        }
        for (int i = 0; i < getSubCommands().size(); i++) {
            if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                subCommand = getSubCommands().get(i);
                break;
            }
        }
        if (subCommand == null) {
            (new ColorChat("[warn]Unknown command")).sendMessage(sender);
            return true;
        }
        if (!sender.hasPermission(subCommand.getPermission())) {
            (new ColorChat("[warn]You don't have permission to use this command")).sendMessage(sender);
            return true;
        }
        subCommand.execute(this.plugin, sender, args);
        return true;
    }

    public LinkedList<SubCommand> getSubCommands() {
        return this.subcmds;
    }

    public boolean checkPermission(CommandSender sender) {
        return getSubCommands().stream().anyMatch(subCommand -> sender.hasPermission(subCommand.getPermission()));
    }

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SubCommand subCommand = null;
        for (int i = 0; i < getSubCommands().size(); i++) {
            if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                subCommand = getSubCommands().get(i);
                break;
            }
        }
        if (subCommand == null)
            return null;
        LinkedList<String> subcmds = new LinkedList<>();
        subCommand.tabCompleter(this.plugin, sender, args, subcmds);
        return subcmds;
    }
}
