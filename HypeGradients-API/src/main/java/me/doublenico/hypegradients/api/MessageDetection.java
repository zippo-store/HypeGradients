package me.doublenico.hypegradients.api;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.perryplaysmc.dynamicconfigurations.utils.DynamicConfigurationDirectory;
import me.doublenico.hypegradients.api.detection.ChatDetectionConfiguration;
import org.bukkit.entity.Player;

public interface MessageDetection {

    String getName();

    String getJSON(Player player, String jsonMessage);

    String getPlainMessage(Player player, String plainMessage);

    WrappedChatComponent getChatComponent(Player player, WrappedChatComponent component);

    boolean isEnabled(Player player, String plainMessage, String jsonMessage, WrappedChatComponent component);

    ChatDetectionConfiguration chatDetectionConfiguration(Player player, DynamicConfigurationDirectory directory);


}
