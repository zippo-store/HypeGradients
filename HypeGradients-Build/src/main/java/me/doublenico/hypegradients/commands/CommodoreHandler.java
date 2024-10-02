package me.doublenico.hypegradients.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.doublenico.hypegradients.HypeGradients;
import me.lucko.commodore.CommodoreProvider;

public class CommodoreHandler {

    public void registerCommodoreSupport(HypeGradients plugin, CommandsManager commandsManager) {
        LiteralCommandNode<?> node = LiteralArgumentBuilder.literal("hypegradients")
                .then(LiteralArgumentBuilder.literal("colors")
                        .then(LiteralArgumentBuilder.literal("add")
                                .then(RequiredArgumentBuilder.argument("name", StringArgumentType.word())
                                        .then(RequiredArgumentBuilder.argument("color", StringArgumentType.greedyString()))))
                        .then(LiteralArgumentBuilder.literal("remove")
                                .then(RequiredArgumentBuilder.argument("color", StringArgumentType.word())))
                        .then(LiteralArgumentBuilder.literal("list")))
                .then(LiteralArgumentBuilder.literal("debug")
                        .then(LiteralArgumentBuilder.literal("message")
                                .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))
                        .then(LiteralArgumentBuilder.literal("title")
                                .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))
                        .then(LiteralArgumentBuilder.literal("subtitle")
                                .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))
                        .then(LiteralArgumentBuilder.literal("bossbar")
                                .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))
                        .then(LiteralArgumentBuilder.literal("actionbar")
                                .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))
                        .then(LiteralArgumentBuilder.literal("sign")
                                .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))
                        .then(LiteralArgumentBuilder.literal("item")
                                .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))
                        .then(LiteralArgumentBuilder.literal("scoreboard")
                                .then(LiteralArgumentBuilder.literal("title")
                                        .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))
                                .then(LiteralArgumentBuilder.literal("line")
                                        .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString()))))
                        .then(LiteralArgumentBuilder.literal("diagnostics")))
                .then(LiteralArgumentBuilder.literal("reload")
                        .then(LiteralArgumentBuilder.literal("settings"))
                        .then(LiteralArgumentBuilder.literal("colors"))
                        .then(LiteralArgumentBuilder.literal("all"))
                        .then(LiteralArgumentBuilder.literal("animations"))
                        .then(LiteralArgumentBuilder.literal("configs"))
                        .then(LiteralArgumentBuilder.literal("tags")))
                .then(LiteralArgumentBuilder.literal("packets")).build();
        CommodoreProvider.getCommodore(plugin).register(plugin.getCommand("hypegradients"), node, commandsManager::checkPermission);
    }
}
