package me.doublenico.hypegradients;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.animations.AnimationListener;
import me.doublenico.hypegradients.api.MessagePacketHandler;
import me.doublenico.hypegradients.bstats.MetricsWrapper;
import me.doublenico.hypegradients.chat.ChatTranslators;
import me.doublenico.hypegradients.commands.CommandsManager;
import me.doublenico.hypegradients.commands.CommodoreHandler;
import me.doublenico.hypegradients.config.impl.AnimationsConfig;
import me.doublenico.hypegradients.config.impl.ColorConfig;
import me.doublenico.hypegradients.config.impl.SettingsConfig;
import me.doublenico.hypegradients.packets.*;
import me.doublenico.hypegradients.placeholder.AnimationPlaceholder;
import me.doublenico.hypegradients.placeholder.GradientPlaceholder;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HypeGradients extends JavaPlugin {
    private SettingsConfig settingsConfig;

    private ColorConfig colorConfig;
    private AnimationsConfig animationsConfig;

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
        this.colorConfig.checkConfig(this);
        if (parent.getConfiguration("animations.yml") == null)
            this.animationsConfig = new AnimationsConfig(parent, "animations", true);
        this.animationsConfig = new AnimationsConfig(parent, "animations", false);
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
                    if (isLegacy()) {
                        new LegacyTitleMessagePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.TITLE);
                    } else {
                        new TitleMessagePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.SET_TITLE_TEXT);
                        new SubtitleMessagePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.SET_SUBTITLE_TEXT);
                    }
                    if (supportsSignature() && !isNewSignature()) new SignaturePacket(this);
                    else if (isNewSignature()) {
                        new NewSignaturePacket(this);
                    } else new ChatMessagePacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.CHAT);
                    new ServerInfoPacket(this, ListenerPriority.MONITOR, PacketType.Status.Server.SERVER_INFO);
                    new PlayerInfoPacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.PLAYER_INFO);
                    new HeaderFooterPacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
                    new EntityMetaDataPacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.ENTITY_METADATA);
                    new EntityPacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.SPAWN_ENTITY);
                    new BossBarPacket(this, ListenerPriority.MONITOR, PacketType.Play.Server.BOSS);
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
        getLogger().finest("PlaceholderAPI placeholders are loaded!");
        new ChatTranslators();
        getLogger().info("Registering commands...");
        CommandsManager commandsManager = new CommandsManager(this);
        Objects.requireNonNull(getCommand("hypegradients")).setExecutor(commandsManager);
        if (CommodoreProvider.isSupported())
            new CommodoreHandler().registerCommodoreSupport(this, commandsManager);
        else Objects.requireNonNull(getCommand("hypegradients")).setTabCompleter(commandsManager);
        getLogger().finest("Commands are enabled!");
        if (settingsConfig.getConfig().getBoolean("bstats.enabled", true)) {
            getLogger().info("Loading bStats metrics...");
            this.metricsWrapper = new MetricsWrapper(this, 17671);
            getLogger().finest("bStats metrics are enabled...");
        }
        if (settingsConfig.getConfig().getBoolean("animations.enabled", true)) {
            getLogger().info("Loading animations...");
            AnimationPlaceholder animationPlaceholder = new AnimationPlaceholder(this);
            animationPlaceholder.register();
            getServer().getPluginManager().registerEvents(new AnimationListener(animationPlaceholder), this);
            getLogger().finest("Animations are enabled...");
        }
        if (settingsConfig.getConfig().getBoolean("chat-detection-minimessage.enabled", false)) {
            try {
                Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
                getLogger().info("Loading MiniMessage chat detection...");
                settingsConfig.getConfig().set("chat-detection-minimessage.enabled", true);
                settingsConfig.getConfig().reload();
                getLogger().finest("Minimessage chat detection is enabled...");
            } catch (ClassNotFoundException e) {
                getLogger().warning("Could not find MiniMessage! Disabling MiniMessage chat detection.");
                settingsConfig.getConfig().set("chat-detection-minimessage.enabled", false);
                settingsConfig.getConfig().reload();
            }
        }
    }

    @Override
    public void onDisable() {
        if (MessagePacketHandler.getPackets() != null)
            MessagePacketHandler.getPackets().clear();
    }


    public SettingsConfig getSettingsConfig() {
        return this.settingsConfig;
    }

    public ColorConfig getColorConfig() {
        return this.colorConfig;
    }

    public AnimationsConfig getAnimationsConfig() {
        return animationsConfig;
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

    public boolean isNewSignature() {
        if (supportsSignature()) {
            switch (getNMSVersion()) {
                case "v1_19_R1", "v1_19_R2" -> {
                    return false;
                }
                default -> {
                    return true;
                }
            }
        }
        return false;
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
