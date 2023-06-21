package me.doublenico.hypegradients;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.animations.AnimationListener;
import me.doublenico.hypegradients.api.chat.ChatTranslators;
import me.doublenico.hypegradients.api.detection.ChatDetection;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.detection.ChatDetectionManager;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacketHandler;
import me.doublenico.hypegradients.bstats.MetricsWrapper;
import me.doublenico.hypegradients.commands.CommandsManager;
import me.doublenico.hypegradients.commands.CommodoreHandler;
import me.doublenico.hypegradients.config.AnimationsConfig;
import me.doublenico.hypegradients.config.ColorConfig;
import me.doublenico.hypegradients.config.SettingsConfig;
import me.doublenico.hypegradients.packets.boss.BossBarPacket;
import me.doublenico.hypegradients.packets.chat.ChatMessagePacket;
import me.doublenico.hypegradients.packets.chat.NewSignaturePacket;
import me.doublenico.hypegradients.packets.chat.SignaturePacket;
import me.doublenico.hypegradients.packets.entity.EntityMetaDataPacket;
import me.doublenico.hypegradients.packets.entity.EntityPacket;
import me.doublenico.hypegradients.packets.gui.GuiMessagePacket;
import me.doublenico.hypegradients.packets.gui.GuiSlotMessage;
import me.doublenico.hypegradients.packets.gui.GuiTitleMessagePacket;
import me.doublenico.hypegradients.packets.motd.ServerInfoPacket;
import me.doublenico.hypegradients.packets.scoreboard.ScoreboardObjectivePacket;
import me.doublenico.hypegradients.packets.scoreboard.ScoreboardTeamPacket;
import me.doublenico.hypegradients.packets.tab.HeaderFooterPacket;
import me.doublenico.hypegradients.packets.tab.PlayerInfoPacket;
import me.doublenico.hypegradients.packets.title.LegacyTitleMessagePacket;
import me.doublenico.hypegradients.packets.title.SubtitleMessagePacket;
import me.doublenico.hypegradients.packets.title.TitleMessagePacket;
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
    private boolean placeholderAPI = false;
    private boolean protocolLib = false;

    @Override
    public void onEnable() {
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
        getLogger().info("Loading custom configurations");
        new ChatDetection("gradient", true);
        ChatDetectionManager.getInstance().getChatDetections().forEach(chatDetection -> {
            chatDetection.build(parent);
            getLogger().info("Added configuration for " + chatDetection.configName());
        });
        ChatDetectionConfiguration gradientConfiguration = ChatDetectionManager.getInstance().getConfiguration("gradient");
        if (gradientConfiguration == null) {
            if (settingsConfig.getConfig().getString("no-found-packet", "disable").equalsIgnoreCase("disable")) {
                getLogger().severe("CANNOT FIND GRADIENT CONFIGURATION, DISABLING PLUGIN");
                Bukkit.getPluginManager().disablePlugin(this);
            } else if (settingsConfig.getConfig().getString("no-found-packet", "disable").equalsIgnoreCase("stop")) {
                getLogger().severe("CANNOT FIND GRADIENT CONFIGURATION, CLOSING THE SERVER");
                Bukkit.shutdown();
            }
        }
        getLogger().finest("Custom Configurations are loaded!");
        if (settingsConfig.getChatDetectionValues().enabled()) {
            if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null)
                getLogger().warning("Could not find ProtocolLib! Disabling gradient chat detection.");
            else {
                try {
                    getLogger().info("Registering ProtocolLib packet listener...");
                    if (isLegacy()) {
                        new LegacyTitleMessagePacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.TITLE, MessageType.TITLE);
                    } else {
                        new TitleMessagePacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.SET_TITLE_TEXT, MessageType.TITLE_TEXT);
                        new SubtitleMessagePacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.SET_SUBTITLE_TEXT, MessageType.SUBTITLE_TEXT);
                    }
                    if (supportsSignature() && !isNewSignature()) {
                        new SignaturePacket(this, MessageType.CHAT, gradientConfiguration);
                    } else if (isNewSignature()) {
                        new NewSignaturePacket(this, MessageType.CHAT, gradientConfiguration);
                    } else {
                        new ChatMessagePacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.CHAT, MessageType.CHAT);
                    }
                    new ServerInfoPacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Status.Server.SERVER_INFO, MessageType.MOTD);
                    new PlayerInfoPacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.PLAYER_INFO, MessageType.PLAYER_INFO);
                    new HeaderFooterPacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER, MessageType.MOTD);
                    new EntityMetaDataPacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.ENTITY_METADATA, MessageType.METADATA);
                    new EntityPacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.SPAWN_ENTITY, MessageType.ENTITY);
                    new BossBarPacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.BOSS, MessageType.BOSSBAR);
                    new GuiMessagePacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.WINDOW_ITEMS, MessageType.GUI_ITEM);
                    new GuiSlotMessage(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.SET_SLOT, MessageType.GUI_ITEM);
                    new GuiTitleMessagePacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.OPEN_WINDOW, MessageType.GUI_TITLE);
                    new ScoreboardTeamPacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.SCOREBOARD_TEAM, MessageType.SCOREBOARD_TEAM);
                    new ScoreboardObjectivePacket(this, gradientConfiguration, ListenerPriority.MONITOR, PacketType.Play.Server.SCOREBOARD_OBJECTIVE, MessageType.SCOREBOARD_OBJECTIVE);
                } catch (NoSuchFieldError e) {
                    getLogger().severe("Report this error to the developer: " + e.getMessage());
                    throwError();
                }
                protocolLib = true;
            }
        }
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
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderAPI = true;
            getLogger().finest("Registering PlaceholderAPI placeholders...");
            new GradientPlaceholder(this).register();
            getLogger().info("PlaceholderAPI placeholders are enabled...");
            if (settingsConfig.getConfig().getBoolean("animations.enabled", true)) {
                getLogger().info("Loading animations...");
                AnimationPlaceholder animationPlaceholder = new AnimationPlaceholder(this);
                animationPlaceholder.register();
                getServer().getPluginManager().registerEvents(new AnimationListener(animationPlaceholder), this);
                getLogger().finest("Animations are enabled...");
            }
        }
        if (!protocolLib && !placeholderAPI) {
            getLogger().warning("You don't have ProtocolLib or PlaceholderAPI installed, the plugin becomes useless, disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
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
