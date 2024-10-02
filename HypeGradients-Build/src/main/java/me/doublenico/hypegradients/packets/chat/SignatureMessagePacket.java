package me.doublenico.hypegradients.packets.chat;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.dev.packets.SignatureChatMessagePacket;
import me.doublenico.hypegradients.dev.wrappers.WrapperSignatureChat;
import org.bukkit.plugin.java.JavaPlugin;

public class SignatureMessagePacket extends MessagePacket {

    public SignatureMessagePacket(JavaPlugin plugin, MessagePacketConfigurations configurations, MessageType messageType) {
        super(new SignatureChatMessagePacket(plugin).getPlugin(), configurations, new SignatureChatMessagePacket(plugin).getPriority(), new SignatureChatMessagePacket(plugin).getType(), messageType);
    }

    @Override
    public boolean register() {
        ((HypeGradients) getPlugin()).getDebugLogger().sendRegisterMessage(getMessagePacketConfigurations().settings().chat());
        if (getMessagePacketConfigurations().settings().chat()) {
            ((HypeGradients) getPlugin()).getDebugLogger().sendChatEnabledMessage(getServerVersion().supportsSignature());
            return getServerVersion().supportsSignature();
        }
        return false;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        HypeGradients plugin = (HypeGradients) getPlugin();
        plugin.getDebugLogger().sendMessagePacketMessage(getType(), getMessageType(), getPriority());
        PacketContainer packet = event.getPacket();
        plugin.getDebugLogger().sendWrapperMessage();
        WrapperSignatureChat wrapper = new WrapperSignatureChat(packet);
        if (wrapper.getWrappedChatComponent() == null)
            return;
        MetricsComponents metrics = new MetricsComponents(plugin.getMetricsWrapper(), "Signature", "Chat");
        processPacket(event, wrapper, plugin.getDebugLogger(), metrics);
    }
}