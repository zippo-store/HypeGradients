package me.doublenico.hypegradients.api;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import me.doublenico.hypegradients.api.packet.MessagePacketHandler;
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
     * Get the JSON message
     * @param player the player
     * @param jsonMessage the json message
     * @return the json message
     */
    String getJSON(Player player, String jsonMessage);

    /**
     * Get the plain message
     * @param player the player
     * @param plainMessage the plain message
     * @return the plain message
     */
    String getPlainMessage(Player player, String plainMessage);

    /**
     * Get the chat component
     * @param player the player
     * @param component the chat component, see {@link WrappedChatComponent}
     * @return the chat component
     */
    WrappedChatComponent getChatComponent(Player player, WrappedChatComponent component);

    /**
     * Check if the message is enabled, this is used to check if the message should be enabled or not
     * @param player the player
     * @param plainMessage the plain message
     * @param jsonMessage the json message
     * @param component the chat component
     * @return true if the message is enabled
     */
    boolean isEnabled(Player player, String plainMessage, String jsonMessage, WrappedChatComponent component);

    /**
     * This if used to set the {@link ChatDetectionConfiguration} for the
     * @param player the player
     * @param directory the directory for the configuration, should be the in the same folder as the plugin
     * @return the {@link ChatDetectionConfiguration}
     */
    ChatDetectionConfiguration chatDetectionConfiguration(Player player, DynamicConfigurationDirectory directory);


}
