package me.doublenico.hypegradients;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.MessagePacketHandler;
import me.doublenico.hypegradients.bstats.MetricsWrapper;
import me.doublenico.hypegradients.chat.ChatTranslators;
import me.doublenico.hypegradients.commands.CommandsManager;
import me.doublenico.hypegradients.config.impl.ColorConfig;
import me.doublenico.hypegradients.config.impl.SettingsConfig;
import me.doublenico.hypegradients.packets.*;
import me.doublenico.hypegradients.placeholder.GradientPlaceholder;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HypeGradients extends JavaPlugin {
    private MessagePacketHandler packetHandler;

    private SettingsConfig settingsConfig;

    private ColorConfig colorConfig;

    private CommandsManager commandsManager;
    private MetricsWrapper metricsWrapper;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        getLogger().info("Loading configurations");
        DynamicConfigurationDirectory parent = new DynamicConfigurationDirectory(this, getDataFolder());
        this.settingsConfig = new SettingsConfig(parent, "settings");
        if (parent.getConfiguration("colors.yml") == null) this.colorConfig = new ColorConfig(parent, "colors", true);
        this.colorConfig = new ColorConfig(parent, "colors", false);
        getLogger().finest("Configurations are loaded!");
        getLogger().finest("Registering PlaceholderAPI placeholders...");
        new GradientPlaceholder(this).register();
        getLogger().info("PlaceholderAPI placeholders are enabled...");
        if (settingsConfig.getConfig().getBoolean("chat-detection.enabled", true)) {
            if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null)
                getLogger().warning("Could not find ProtocolLib! Disabling gradient chat detection.");
            else {

                try {
                    getLogger().info("Registering ProtocolLib packet listener...");
                    this.packetHandler = new MessagePacketHandler();
                    if (isLegacy()) {
                        new LegacyTitleMessagePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.TITLE);
                    } else {
                        new TitleMessagePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.SET_TITLE_TEXT);
                        new SubtitleMessagePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.SET_SUBTITLE_TEXT);
                    }
                    if (supportsSignature()) {
                        new SignaturePacket(this);
                    } else {
                        new ChatMessagePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.CHAT);
                    }
                    new GuiMessagePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.WINDOW_ITEMS);
                    new GuiSlotMessage(this, ListenerPriority.MONITOR, PacketType.Play.Server.SET_SLOT);
                    new GuiTitleMessagePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.OPEN_WINDOW);
                    new ScoreboardTeamPacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.SCOREBOARD_TEAM);
                    new ScoreboardObjectivePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
                } catch (NoSuchFieldError e) {
                    getLogger().severe("Report this error to the developer: " + e.getMessage());
                    throwError();
                }
            }
        }
        getLogger().finest("PlaceholderAPI placehoders are loaded!");
        new ChatTranslators();
        getLogger().info("Registering commands...");
        this.commandsManager = new CommandsManager(this);
        Objects.requireNonNull(getCommand("hypegradients")).setExecutor(this.commandsManager);
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
                        .then(LiteralArgumentBuilder.literal("scoreboard")
                                .then(LiteralArgumentBuilder.literal("title")
                                        .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))
                                .then(LiteralArgumentBuilder.literal("line")
                                        .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))))
                .then(LiteralArgumentBuilder.literal("reload")
                        .then(LiteralArgumentBuilder.literal("settings"))
                        .then(LiteralArgumentBuilder.literal("colors"))
                        .then(LiteralArgumentBuilder.literal("all")))
                .then(LiteralArgumentBuilder.literal("packets")).build();

        CommodoreProvider.getCommodore(this).register(getCommand("hypegradients"), node, player -> this.commandsManager.checkPermission(player));
        getLogger().finest("Commands are enabled!");
        getLogger().info("Loading bStats metrics...");
        this.metricsWrapper = new MetricsWrapper(this, 17671);
        getLogger().finest("bStats metrics are enabled...");
    }

    @Override
    public void onDisable() {
        if (this.packetHandler != null)
            this.packetHandler.getPackets().clear();
    }

    public SettingsConfig getSettingsConfig() {
        return this.settingsConfig;
    }

    public ColorConfig getColorConfig() {
        return this.colorConfig;
    }

    public MessagePacketHandler getPacketHandler() {
        return this.packetHandler;
    }

    public MetricsWrapper getMetricsWrapper() {
        return this.metricsWrapper;
    }

    public String getNMSVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        return v.substring(v.lastIndexOf('.') + 1);
    }

    public boolean supportsSignature() {
        return switch (getNMSVersion()) {
            case "v1_16_R1", "v1_16_R2", "v1_16_R3", "v1_17_R1", "v1_18_R1", "v1_18_R2" -> false;
            default -> true;
        };
    }

    public boolean isLegacy() {
        return switch (getNMSVersion()) {
            case "v1_16_R1", "v1_16_R2", "v1_16_R3" -> true;
            default -> false;
        };
    }

    private void throwError() {
        if (settingsConfig.getConfig().getString("no-found-packet", "disable").equalsIgnoreCase("disable")) {
            getLogger().severe("The packet is not found, disabling the plugin...");
            getLogger().severe("This could be a problem with the ProtocolLib version, please update it or downgrade the version.");
            getLogger().severe("If the problem persists, please contact Zippo™ - Store | 2023.");
            getLogger().severe("Join this discord and create a ticket, https://discord.com/invite/j5Fb3jj2Sq");
            Bukkit.getPluginManager().disablePlugin(this);
        } else if (settingsConfig.getConfig().getString("no-found-packet", "disable").equalsIgnoreCase("stop")) {
            getLogger().severe("The packet is not found, shutting down the server...");
            getLogger().severe("This could be a problem with the ProtocolLib version, please update it or downgrade the version.");
            getLogger().severe("If the problem persists, please contact Zippo™ - Store | 2023.");
            getLogger().severe("Join this discord and create a ticket, https://discord.com/invite/j5Fb3jj2Sq");
            Bukkit.shutdown();
        }
    }

}
