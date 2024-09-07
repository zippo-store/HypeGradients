package me.doublenico.hypegradients.api;

import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.packet.MessagePacketHandler;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;
import org.bukkit.entity.Player;

/**
 * Used for creating custom message detection for every message packet
 */
public interface MessageDetection {

    /**
     * Get the name of the message detection, this is used to identify the custom message detection in the {@link MessagePacketHandler}
     * @return the name of the message detection
     */
    String getName();

    /**
     * Get the contents of the message, use this to modify the message packet, see {@link MessagePacketComponents}
     * @param player the player that receives the message
     * @param components the {@link MessagePacketComponents} of the message packet
     * @return the new {@link MessagePacketComponents} with the modified contents that will be sent to the packet
     */
    MessagePacketComponents getChatComponent(Player player, MessagePacketComponents components);

    /**
     * @param player the player that receives the message
     * @param components the {@link MessagePacketComponents} of the message packet
     * @return true if you want to sent this MessageDetection to the packet, false if you don't want to sent this MessageDetection to the packet
     */
    boolean isEnabled(Player player, MessagePacketComponents components);

    /**
     * This if used to set the {@link ChatDetectionConfiguration} for the
     * @param player the player that receives the message
     * @param directory the directory for the configuration, should be the in the same folder as the plugin
     * @return the {@link ChatDetectionConfiguration} that will be used for this MessageDetection
     */
    ChatDetectionConfiguration chatDetectionConfiguration(Player player, DynamicConfigurationDirectory directory);


}
