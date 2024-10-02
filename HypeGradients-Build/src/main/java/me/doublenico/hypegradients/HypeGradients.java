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
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.api.bstats.MetricsWrapper;
import me.doublenico.hypegradients.api.version.ServerVersion;
import me.doublenico.hypegradients.commands.CommandsManager;
import me.doublenico.hypegradients.commands.CommodoreHandler;
import me.doublenico.hypegradients.config.*;
import me.doublenico.hypegradients.api.log.DebugLogger;
import me.doublenico.hypegradients.packets.*;
import me.doublenico.hypegradients.packets.boss.BossBarMessagePacket;
import me.doublenico.hypegradients.packets.chat.ChatMessagePacket;
import me.doublenico.hypegradients.packets.chat.NewSignatureMessagePacket;
import me.doublenico.hypegradients.packets.chat.SignatureMessagePacket;
import me.doublenico.hypegradients.packets.entity.EntityMessagePacket;
import me.doublenico.hypegradients.packets.entity.EntityMetaDataMessagePacket;
import me.doublenico.hypegradients.packets.gui.GuiMessagePacket;
import me.doublenico.hypegradients.packets.gui.GuiSlotMessagePacket;
import me.doublenico.hypegradients.packets.gui.GuiTitleMessagePacket;
import me.doublenico.hypegradients.packets.motd.ServerInfoMessagePacket;
import me.doublenico.hypegradients.packets.scoreboard.ScoreboardObjectiveMessagePacket;
import me.doublenico.hypegradients.packets.scoreboard.ScoreboardTeamMessagePacket;
import me.doublenico.hypegradients.packets.sign.SignLinesMessagePacket;
import me.doublenico.hypegradients.packets.sign.SignUpdateMessagePacket;
import me.doublenico.hypegradients.packets.tab.FooterMessagePacket;
import me.doublenico.hypegradients.packets.tab.HeaderMessagePacket;
import me.doublenico.hypegradients.packets.tab.PlayerInfoMessagePacket;
import me.doublenico.hypegradients.packets.title.LegacyTitleMessagePacket;
import me.doublenico.hypegradients.packets.title.SubtitleMessagePacket;
import me.doublenico.hypegradients.packets.title.TitleMessagePacket;
import me.doublenico.hypegradients.placeholder.AnimationPlaceholder;
import me.doublenico.hypegradients.placeholder.GradientPlaceholder;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class HypeGradients extends JavaPlugin {
    private SettingsConfig settingsConfig;
    private MessageDetectionConfig messageDetectionConfig;
    private TagConfig tagConfig;

    private ColorConfig colorConfig;
    private AnimationsConfig animationsConfig;

    private MetricsWrapper metricsWrapper;
    private boolean placeholderAPI = false;
    private boolean protocolLib = false;
    private DebugLogger debugLogger;

    @Override
    public void onEnable() {
        getLogger().info("Loading configurations");
        DynamicConfigurationDirectory parent = new DynamicConfigurationDirectory(this, getDataFolder());
        DynamicConfigurationDirectory detections = new DynamicConfigurationDirectory(this, new File(getDataFolder() + "/detections"));
        this.settingsConfig = new SettingsConfig(parent, "settings");
        if (parent.getConfiguration("colors.yml") == null) this.colorConfig = new ColorConfig(parent, "colors", true);
        this.colorConfig = new ColorConfig(parent, "colors", false);
        this.colorConfig.checkConfig(this);
        if (parent.getConfiguration("animations.yml") == null)
            this.animationsConfig = new AnimationsConfig(parent, "animations", true);
        this.animationsConfig = new AnimationsConfig(parent, "animations", false);
        this.tagConfig = new TagConfig(parent, "tags", true);
        this.messageDetectionConfig = new MessageDetectionConfig(detections, "messageDetection");
        getLogger().finest("Configurations are loaded!");
        getLogger().info("Loading custom configurations");
        new ChatDetection("gradient", true);
        ChatDetectionManager.getInstance().getChatDetections().forEach(chatDetection -> {
            chatDetection.build(detections);
            getLogger().info("Added configuration for " + chatDetection.configName());
        });
        ChatDetectionConfiguration gradientConfiguration = ChatDetectionManager.getInstance().getConfiguration("gradient");
        if (gradientConfiguration == null) {
            if (settingsConfig.getConfig().getString("no-found-packet", "disable").equalsIgnoreCase("disable")) {
                getLogger().severe("CANNOT FIND GRADIENT CONFIGURATION, DISABLING PLUGIN");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            } else if (settingsConfig.getConfig().getString("no-found-packet", "disable").equalsIgnoreCase("stop")) {
                getLogger().severe("CANNOT FIND GRADIENT CONFIGURATION, CLOSING THE SERVER");
                Bukkit.shutdown();
                return;
            }
        }
        getLogger().finest("Custom Configurations are loaded!");
        debugLogger = new DebugLogger(this, settingsConfig.getConfig().getBoolean("debug", false));
        if (settingsConfig.getChatDetectionValues().enabled()) {
            if (getServer().getPluginManager().getPlugin("ProtocolLib") == null)
                getLogger().warning("Could not find ProtocolLib! Disabling gradient chat detection.");
            else {
                try {
                    getLogger().info("Registering ProtocolLib packet listener...");
                    new MessagePacketHolder().initialisePackets(this, settingsConfig, messageDetectionConfig, debugLogger);
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
        Objects.requireNonNull(getCommand("hypegradients"), "HypeGradients command cannot be found").setExecutor(commandsManager);
        if (CommodoreProvider.isSupported())
            new CommodoreHandler().registerCommodoreSupport(this, commandsManager);
        else Objects.requireNonNull(getCommand("hypegradients"), "HypeGradients command cannot be found").setTabCompleter(commandsManager);
        getLogger().finest("Commands are enabled!");
        if (settingsConfig.getConfig().getBoolean("bstats.enabled", true)) {
            getLogger().info("Loading bStats metrics...");
            this.metricsWrapper = new MetricsWrapper(this, 17671);
            getLogger().finest("bStats metrics are enabled...");
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderAPI = true;
            if (settingsConfig.getConfig().getBoolean("placeholders", true)) {
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
        }
        if (!protocolLib && !placeholderAPI) {
            getLogger().warning("You don't have ProtocolLib or PlaceholderAPI installed, the plugin becomes useless, disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (this.getServer().getPluginManager().getPlugin("ProtocolLib") != null)
            if (MessagePacketHandler.getPackets() != null) MessagePacketHandler.getPackets().clear();
    }

    public MessageDetectionConfig getMessageDetectionConfig() {
        return this.messageDetectionConfig;
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

    public TagConfig getTagConfig() {
        return tagConfig;
    }

    public MetricsWrapper getMetricsWrapper() {
        return this.metricsWrapper;
    }

    public DebugLogger getDebugLogger() {
        return debugLogger;
    }

    private void throwError() {
        if (settingsConfig.getConfig().getString("no-found-packet", "disable").equalsIgnoreCase("disable")) {
            getLogger().severe("The packet is not found, disabling the plugin...");
            getLogger().severe("This could be a problem with the ProtocolLib version, please update it or downgrade the version.");
            getLogger().severe("If the problem persists, please contact Zippo™ - Store | 2024.");
            getLogger().severe("Join this discord and create a ticket, https://discord.com/invite/j5Fb3jj2Sq");
            Bukkit.getPluginManager().disablePlugin(this);
        } else if (settingsConfig.getConfig().getString("no-found-packet", "disable").equalsIgnoreCase("stop")) {
            getLogger().severe("The packet is not found, shutting down the server...");
            getLogger().severe("This could be a problem with the ProtocolLib version, please update it or downgrade the version.");
            getLogger().severe("If the problem persists, please contact Zippo™ - Store | 2024.");
            getLogger().severe("Join this discord and create a ticket, https://discord.com/invite/j5Fb3jj2Sq");
            Bukkit.shutdown();
        }
    }

}
