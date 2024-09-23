package me.doublenico.hypegradients.log;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import me.doublenico.hypegradients.api.chat.ChatGradient;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Used to log debug messages for the {@link MessagePacket}
 * @param plugin the plugin that is using to send the debug message
 * @param shouldSend if the debug message should be sent, use this to toggle debug messages with config
 */
public record DebugLogger(JavaPlugin plugin, boolean shouldSend) {

    public void sendDebugMessage(String... messages) {
        if (shouldSend) {
            for (String message : messages) {
                plugin.getLogger().info(message);
            }
        }
    }

    public void sendDebugMessage(String message) {
        if (shouldSend) {
            plugin.getLogger().info(message);
        }
    }

    public void sendRegisterMessage(boolean register) {
        sendDebugMessage("Registering Title Packet: " + register);
    }

    public void sendMessagePacketMessage(PacketType packetType, MessageType messageType, ListenerPriority priority) {
        sendDebugMessage("PacketType: " + packetType.name(),
                "MessageType: " + messageType.name(),
                "Priority: " + priority.name());
    }

    public void sendComponentMessage(Player player, MessagePacketComponents components) {
        sendDebugMessage("Sending to player: " + player.getName(),
                "Json: " + components.getJsonMessage(),
                "Plain: " + components.getPlainMessage(),
                "ChatComponent: " + components.getWrappedChatComponent());
    }

    public void sendGradientMessage(){
        sendDebugMessage("Getting gradient from message");
    }

    public void sendGradientDetectedMessage(ChatGradient gradient, boolean enabled){
        sendDebugMessage("Gradient is detected: " + gradient.isGradient(), "Gradient is enabled: " + enabled);
    }

    public void sendMessageEventMessage(){
        sendDebugMessage("Executing MessageEvent");
    }

    public void sendWrapperMessage(){
        sendDebugMessage("Checking if the wrapper is null and components are not null");
    }

    public void sendBeforeMessage(){
        sendDebugMessage("Executing before MessageDetections");
    }

    public void sendAfterMessage(){
        sendDebugMessage("Executing after MessageDetections");
    }
}
