package me.doublenico.hypegradients.commands.impl;

import com.comphenix.protocol.ProtocolLibrary;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.chat.ColorChat;
import me.doublenico.hypegradients.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;

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
                new ColorChat("[important]/hypegradients [argument]debug [optional]bossbar [required]<message> [description]- Send a debug bossbar").sendMessage(sender);
                new ColorChat("[important]/hypegradients [argument]debug [optional]scoreboard [required]<line|title> <message> [description]- Send a debug scoreboard").sendMessage(sender);
                new ColorChat("[important]/hypegradients [argument]debug [optional]diagnostics [description]- Check for data and informations, good for bug report").sendMessage(sender);
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
                case "bossbar" -> {
                    if (!sender.hasPermission("hypegradients.debug.bossbar"))
                        return;
                    StringBuilder message = new StringBuilder();
                    int i;
                    for (i = 2; i < args.length; i++)
                        message.append(args[i]).append(" ");
                    BossBar bossBar = plugin.getServer().createBossBar(message.toString(), BarColor.BLUE, BarStyle.SEGMENTED_12);
                    bossBar.addPlayer((Player) sender);
                    bossBar.setVisible(true);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        bossBar.setVisible(false);
                        bossBar.removePlayer((Player) sender);
                    }, 200L);
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
                case "scoreboard" -> {
                    if (!sender.hasPermission("hypegradients.debug.scoreboard"))
                        return;
                    if (sender instanceof Player player) {
                        if (args.length > 3) {
                            if (args[2].equals("title")) {
                                StringBuilder message = new StringBuilder();
                                int i;
                                for (i = 3; i < args.length; i++)
                                    message.append(args[i]).append(" ");

                                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                                player.getScoreboard().registerNewObjective("hypegradientssb-title", Criteria.DUMMY, message.toString());
                                player.getScoreboard().getObjective("hypegradientssb-title").setDisplaySlot(DisplaySlot.SIDEBAR);
                                player.getScoreboard().getObjective("hypegradientssb-title").getScore("§r").setScore(1);
                                Bukkit.getScheduler().runTaskLater(plugin, () -> player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()), 200L);
                            }
                            if (args[2].equals("line")) {
                                StringBuilder message = new StringBuilder();
                                int i;
                                for (i = 3; i < args.length; i++)
                                    message.append(args[i]).append(" ");
                                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                                player.getScoreboard().registerNewObjective("hypegradientssb-line", Criteria.DUMMY, "§r");
                                player.getScoreboard().getObjective("hypegradientssb-line").setDisplaySlot(DisplaySlot.SIDEBAR);
                                player.getScoreboard().getObjective("hypegradientssb-line").getScore(message.toString()).setScore(0);
                                Bukkit.getScheduler().runTaskLater(plugin, () -> player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()), 200L);
                            }
                        }

                    }
                }
                case "diagnostics" -> {
                    if (!sender.hasPermission("hypegradients.debug.diagnostics"))
                        return;
                    (new ColorChat("[important]HypeGradients Diagnostics")).sendMessage(sender);
                    (new ColorChat("[important]Version: " + plugin.getDescription().getVersion())).sendMessage(sender);
                    (new ColorChat("[important]Author: " + plugin.getDescription().getAuthors().get(0))).sendMessage(sender);
                    new ColorChat("[important]Server version: " + Bukkit.getVersion()).sendMessage(sender);
                    if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
                        new ColorChat("[important]ProtocolLib version: " + ProtocolLibrary.getPlugin().getDescription().getVersion()).sendMessage(sender);
                        new ColorChat("[important]ProtocolLib API version: " + ProtocolLibrary.getPlugin().getDescription().getAPIVersion()).sendMessage(sender);
                        new ColorChat("[important]ProtocolLib full name: " + ProtocolLibrary.getPlugin().getDescription().getFullName()).sendMessage(sender);
                    }
                    new ColorChat("[important]Java version: " + System.getProperty("java.version")).sendMessage(sender);
                    new ColorChat("[important]PlaceholderAPI version: " + Bukkit.getPluginManager().getPlugin("PlaceholderAPI").getDescription().getVersion()).sendMessage(sender);
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
            if (sender.hasPermission("hypegradients.debug.scoreboard"))
                completions.add("scoreboard");
            if (sender.hasPermission("hypegradients.debug.bossbar"))
                completions.add("bossbar");
            if (sender.hasPermission("hypegradients.debug.diagnostics"))
                completions.add("diagnostics");
            return;
        }
        if (args.length == 2)
            switch (args[0]) {
                case "message" -> {
                    if (sender.hasPermission("hypegradients.debug.message"))
                        completions.add("<message>");
                }
                case "title" -> {
                    if (sender.hasPermission("hypegradients.debug.title"))
                        completions.add("<message>");
                }
                case "subtitle" -> {
                    if (sender.hasPermission("hypegradients.debug.subtitle"))
                        completions.add("<message>");
                }
                case "bossbar" -> {
                    if (sender.hasPermission("hypegradients.debug.bossbar"))
                        completions.add("<message>");
                }
                case "scoreboard" -> {
                    if (sender.hasPermission("hypegradients.debug.scoreboard"))
                        completions.add("<title/line>");
                }
            }
        if (args.length == 3) {
            if (args[0].equals("scoreboard")) {
                if (sender.hasPermission("hypegradients.debug.scoreboard"))
                    completions.add("<message>");
            }
        }
    }

}
