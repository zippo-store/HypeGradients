package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacket;
import me.doublenico.hypegradients.chat.ChatGradient;
import me.doublenico.hypegradients.chat.ChatJson;
import me.doublenico.hypegradients.dev.AdventureChatComponent;
import me.doublenico.hypegradients.dev.packets.NewSignatureChatMessagePacket;
import me.doublenico.hypegradients.dev.wrappers.NewWrapperSignatureChat;
import org.bukkit.plugin.java.JavaPlugin;

public class NewSignaturePacket extends MessagePacket {

    public NewSignaturePacket(JavaPlugin plugin) {
        super(new NewSignatureChatMessagePacket(plugin).getPlugin(), new NewSignatureChatMessagePacket(plugin).getPriority(), new NewSignatureChatMessagePacket(plugin).getType());
    }

    @Override
    public boolean register() {
        HypeGradients plugin = (HypeGradients) getPlugin();
        if (plugin.getSettingsConfig().getConfig().getBoolean("chat-detection.chat", true))
            return isNewSignature();
        return false;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        NewWrapperSignatureChat wrapper = new NewWrapperSignatureChat(packet);
        WrappedChatComponent component = wrapper.getMessage();
        if (component == null) return;
        String message = component.getJson();
        String string = (new ChatJson(message)).convertToString();
        ChatGradient gradient = new ChatGradient(string);
        if (gradient.isGradient((HypeGradients) getPlugin())) {
            wrapper.setMessage(new ChatJson(gradient.translateGradient((HypeGradients) getPlugin())).convertToJson());
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Signature", "Chat");
        } else if (((HypeGradients) getPlugin()).getSettingsConfig().getConfig().getBoolean("chat-detection-minimessage.enabled", true) || ((HypeGradients) getPlugin()).getSettingsConfig().getConfig().getBoolean("chat-detection-minimessage.chat", true)) {
            AdventureChatComponent adventureChatComponent = new AdventureChatComponent(string);
            if (adventureChatComponent.getFormattedComponent() == null) return;
            wrapper.setMessage(adventureChatComponent.getFormattedComponent());
            if (((HypeGradients) getPlugin()).getMetricsWrapper() == null) return;
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientChart();
            ((HypeGradients) getPlugin()).getMetricsWrapper().gradientDetectionChart("Signature", "Chat");
        }
    }
}
