package me.doublenico.hypegradients.api.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import me.doublenico.hypegradients.api.log.DebugLogger;
import me.doublenico.hypegradients.api.MessageDetection;
import me.doublenico.hypegradients.api.MessageDetectionManager;
import me.doublenico.hypegradients.api.chat.ChatGradient;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.event.GradientModifyEvent;
import me.doublenico.hypegradients.api.event.MessagePacketEvent;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.annotations.Execute;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.api.packet.enums.DetectionExecution;
import me.doublenico.hypegradients.api.version.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all message packets
 */
public abstract class MessagePacket {

    private final JavaPlugin plugin;
    private final ListenerPriority priority;
    private final PacketType type;
    private final MessageType messageType;
    private final MessagePacketConfigurations messagePacketConfigurations;

    public MessagePacket(JavaPlugin plugin, MessagePacketConfigurations messagePacketConfigurations, ListenerPriority priority, PacketType type, MessageType messageType) {
        this.plugin = plugin;
        this.priority = priority;
        this.type = type;
        this.messageType = messageType;
        this.messagePacketConfigurations = messagePacketConfigurations;
        MessagePacketHandler.getPackets().add(this);
        if (new MessagePacketHandler().registerPacketListener(this))
            plugin.getLogger().info("Registered packet listener for " + type.name() + " with priority " + priority.name() + " for " + messageType.name());
        else {
            if (register())
                plugin.getLogger().warning("Failed to register packet listener for " + type.name() + " with priority " + priority.name() + " for " + messageType.name());
        }
    }

    @Override
    public String toString(){
        return "MessagePacket{" +
                "plugin=" + plugin.getName() +
                ", priority=" + priority.name() +
                ", type=" + type.name() +
                ", messageType=" + messageType.name() +
                ", messagePacketConfigurations=" + messagePacketConfigurations +
                '}';
    }

    /**
     * Checks for conditions, and if true, it will register the packet listener. {@link MessagePacketHandler#registerPacketListener()}
     *
     * @return if the packet should run or not
     */
    public abstract boolean register();

    /**
     * Called when the packet is sent.
     *
     * @param event the packet event
     */
    public abstract void onPacketSending(PacketEvent event);

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public ListenerPriority getPriority() {
        return priority;
    }

    public PacketType getType() {
        return type;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public MessagePacketConfigurations getMessagePacketConfigurations() {
        return messagePacketConfigurations;
    }

    /**
     * Sets the gradient for the component
     * @param player the player
     * @param gradient the gradient message, see {@link ChatGradient}
     * @param components the initial components of the message packet, see {@link MessagePacketComponents}
     * @return the component with the gradient
     */
    public MessagePacketComponents setGradient(Player player, ChatGradient gradient, MessagePacketComponents components) {
        GradientModifyEvent gradientModifyEvent = new GradientModifyEvent(player, components.getPlainMessage(), components.getJsonMessage(), gradient.getMessage(), getMessageType());
        Bukkit.getPluginManager().callEvent(gradientModifyEvent);
        if (gradientModifyEvent.isCancelled()) return components;
        gradient = new ChatGradient(gradientModifyEvent.getGradientMessage());
        components.setPlainMessage(gradient.translateGradient());
        components.setJsonMessage((new ChatJson(gradient.translateGradient())).convertToJson());
        components.setJsonWrappedChatComponent(components.getJsonMessage());

        return components;
    }

    /**
     * Gets the initial components of the message packet and modify them in the {@link MessagePacketEvent} event, see {@link MessagePacketComponents}
     * @param player The player
     * @param components The initial components of the message packet
     * @return the modified components after the event or null if the event is cancelled
     */
    public MessagePacketComponents getMessageEvent(Player player, MessagePacketComponents components){
        MessagePacketEvent messagePacketEvent = new MessagePacketEvent(player, getMessageType(), components);
        Bukkit.getPluginManager().callEvent(messagePacketEvent);
        if (messagePacketEvent.isCancelled()) return components;
        return new MessagePacketComponents(messagePacketEvent.getComponents().getWrappedChatComponent(), messagePacketEvent.getComponents().getJsonMessage(), messagePacketEvent.getComponents().getPlainMessage());
    }

    /**
     * Gets the message detection for the packet
     * @param plugin the plugin
     * @param player the player
     * @param components the initial components of the message packet
     * @param before if the message detection should be executed before or after the gradient, see {@link DetectionExecution}
     * @return the modified components after the message detection
     */
    public MessagePacketComponents getMessageDetection(JavaPlugin plugin, Player player, MessagePacketComponents components, boolean before){
        for(MessageDetection messageDetection : MessageDetectionManager.getInstance().getMessageDetectionList()){
            if (getExecution(messageDetection) != before) continue;
            if(!messageDetection.isEnabled(player, components)) continue;
            ChatDetectionConfiguration chatDetectionConfiguration = messageDetection.chatDetectionConfiguration(player, new DynamicConfigurationDirectory(plugin, new File(plugin.getDataFolder() + "/detections")));
            if(chatDetectionConfiguration == null) continue;
            if(!chatDetectionConfiguration.getChatDetectionValues().title()) continue;
            if (messageDetection.getChatComponent(player, components) == null) return components;
            components = messageDetection.getChatComponent(player, components);
        }
        return components;
    }

    /**
     * Checks if the component is a gradient and if the chat detection for the packet is enabled
     * @param gradient the gradient message, see {@link ChatGradient}
     * @param chatDetection if the chat detection is enabled
     * @return if the component is a gradient
     */
    public boolean isGradient(ChatGradient gradient, boolean chatDetection){
        return gradient.isGradient() && chatDetection;
    }

    /**
     * @param detection the message detection
     * @return if the message detection should be executed before or after the gradient, see {@link DetectionExecution}
     */
    public boolean getExecution(MessageDetection detection) {
        Execute priority = detection.getClass().getAnnotation(Execute.class);
        if (priority != null) return priority.value().isBefore();

        return DetectionExecution.AFTER.isBefore();
    }

    /**
     * {@link ServerVersion} is used to check if some features are supported by the server version
     */
    public ServerVersion getServerVersion() {
        return new ServerVersion();
    }

    /**
     * Processes the packet, checks for message detection, gradient and puts them accordingly
     * @param event the packet event
     * @param logger the debug logger
     * @param metrics the metrics components
     * @param components the initial components of the message packet
     * @return the modified components after gradient and message detection
     */
    public MessagePacketComponents processPacket(PacketEvent event, DebugLogger logger, MetricsComponents metrics, MessagePacketComponents components) {
        Player player = event.getPlayer();
        logger.sendComponentMessage(player, components);
        logger.sendMessageEventMessage();
        components = getMessageEvent(player, components);
        if (components == null) return null;
        logger.sendComponentMessage(player, components);
        logger.sendBeforeMessage();
        components = getMessageDetection(getPlugin(), player, components, true);
        if (components == null) return null;
        logger.sendComponentMessage(player, components);
        logger.sendGradientMessage();
        ChatGradient gradient = new ChatGradient(components.getPlainMessage());
        logger.sendGradientDetectedMessage(gradient, getMessagePacketConfigurations().gradient().getChatDetectionValues().title());
        if (isGradient(gradient, getMessagePacketConfigurations().gradient().getChatDetectionValues().title())) {
            components = setGradient(player, gradient, components);
            logger.sendComponentMessage(player, components);
            metrics.setMetrics();
        }
        logger.sendAfterMessage();
        components = getMessageDetection(getPlugin(), player, components, false);
        if (components == null) return null;
        logger.sendComponentMessage(player, components);
        return components;
    }


    /**
     * See {@link #processPacket(PacketEvent, DebugLogger, MetricsComponents, MessagePacketComponents)}
     * <p>
     * This method uses the {@link AbstractPacket} class to process the packet and set the wrapped chat component
     */
    public void processPacket(PacketEvent event, AbstractPacket packet, DebugLogger logger, MetricsComponents metrics, MessagePacketComponents components){
        packet.setWrappedChatComponent(processPacket(event, logger, metrics, components).getWrappedChatComponent());
    }

    /**
     * See {@link #processPacket(PacketEvent, DebugLogger, MetricsComponents, MessagePacketComponents)}
     * <p>
     * This method uses the {@link AbstractPacket} class to process the packet and set/get the wrapped chat component
     */
    public void processPacket(PacketEvent event, AbstractPacket packet, DebugLogger logger, MetricsComponents metrics){
        processPacket(event, packet, logger, metrics, new MessagePacketComponents(packet.getWrappedChatComponent(), packet.getWrappedChatComponent().getJson(), new ChatJson(packet.getWrappedChatComponent().getJson()).convertToString()));
    }

    /**
     * See {@link #processPacket(PacketEvent, DebugLogger, MetricsComponents, MessagePacketComponents)}
     * <p>
     * Processes the item stack, checks for message detection, gradient and puts them accordingly
     */
    public void processItemStack(PacketEvent event, DebugLogger logger, MetricsComponents metrics, ItemStack item){
        if (item == null || item.getType().isAir()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        if (meta.hasDisplayName()){
            MessagePacketComponents messagePacketComponents = new MessagePacketComponents(WrappedChatComponent.fromLegacyText(meta.getDisplayName()), new ChatJson(meta.getDisplayName()).convertToJson(), meta.getDisplayName());
            messagePacketComponents = processPacket(event, logger, metrics, messagePacketComponents);
            meta.setDisplayName(new ChatJson(messagePacketComponents.getWrappedChatComponent().getJson()).convertToString());
        }
        if (meta.hasLore()){
            List<String> lore = new ArrayList<>();
            for (String line : meta.getLore()){
                MessagePacketComponents messagePacketComponents = new MessagePacketComponents(WrappedChatComponent.fromLegacyText(line), new ChatJson(line).convertToJson(), line);
                messagePacketComponents = processPacket(event, logger, metrics, messagePacketComponents);
                if (messagePacketComponents == null) continue;
                lore.add(new ChatJson(messagePacketComponents.getWrappedChatComponent().getJson()).convertToString());
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
    }
}
