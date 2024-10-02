package me.doublenico.hypegradients.packets.chat;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.bstats.MetricsComponents;
import me.doublenico.hypegradients.api.chat.ChatJson;
import me.doublenico.hypegradients.api.event.MessageType;
import me.doublenico.hypegradients.api.log.DebugLogger;
import me.doublenico.hypegradients.api.packet.MessagePacket;
import me.doublenico.hypegradients.api.packet.components.MessagePacketComponents;
import me.doublenico.hypegradients.api.packet.components.MessagePacketConfigurations;
import me.doublenico.hypegradients.dev.packets.NewSignatureChatMessagePacket;
import me.doublenico.hypegradients.dev.util.AdventureComponent;
import me.doublenico.hypegradients.dev.wrappers.NewWrapperSignatureChat;
import org.bukkit.plugin.java.JavaPlugin;

public class NewSignatureMessagePacket extends MessagePacket
{
    public NewSignatureMessagePacket(JavaPlugin plugin, MessagePacketConfigurations configurations, MessageType messageType) {
        super(new NewSignatureChatMessagePacket(plugin).getPlugin(), configurations, new NewSignatureChatMessagePacket(plugin).getPriority(), new NewSignatureChatMessagePacket(plugin).getType(), messageType);
    }

    @Override
    public boolean register() {
        ((HypeGradients) getPlugin()).getDebugLogger().sendRegisterMessage(getMessagePacketConfigurations().settings().chat());
        if (getMessagePacketConfigurations().settings().chat()) {
            ((HypeGradients) getPlugin()).getDebugLogger().sendChatEnabledMessage(getServerVersion().isNewSignature());
            return getServerVersion().isNewSignature();
        }
        return false;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        HypeGradients plugin = (HypeGradients) getPlugin();
        plugin.getDebugLogger().sendMessagePacketMessage(getType(), getMessageType(), getPriority());
        PacketContainer packet = event.getPacket();
        plugin.getDebugLogger().sendWrapperMessage();
        NewWrapperSignatureChat wrapper = new NewWrapperSignatureChat(packet);
        if (wrapper.getWrappedChatComponent() == null)
            return;
        MetricsComponents metrics = new MetricsComponents(plugin.getMetricsWrapper(), "Signature", "Chat");
        DebugLogger logger = plugin.getDebugLogger();
        MessagePacketComponents components;
        if(!wrapper.isPaper()) components = new MessagePacketComponents(wrapper.getWrappedChatComponent(), wrapper.getWrappedChatComponent().getJson(), new ChatJson(wrapper.getWrappedChatComponent().getJson()).convertToString());
        else components = new MessagePacketComponents(wrapper.getWrappedChatComponent(), wrapper.getWrappedChatComponent().getJson(), wrapper.getWrappedChatComponent().getJson());
        processPacket(event, wrapper, logger, metrics, components);
    }
}

