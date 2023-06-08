package me.doublenico.hypegradients.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.HypeGradients;
import me.doublenico.hypegradients.api.MessagePacket;
import me.doublenico.hypegradients.chat.ChatGradient;
import me.doublenico.hypegradients.chat.ChatJson;
import me.doublenico.hypegradients.wrappers.WrapperMetaData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EntityMetaDataPacket extends MessagePacket {
    public EntityMetaDataPacket(JavaPlugin plugin, ListenerPriority priority, PacketType type) {
        super(plugin, priority, type);
    }

    @Override
    public boolean register() {
        return true;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperMetaData metaData = new WrapperMetaData(event.getPacket());
        List<WrappedChatComponent> components = new ArrayList<>();
        List<WrappedChatComponent> metaComponents = metaData.getMessages();
        metaComponents.forEach(wrappedChatComponent -> {
            String message = wrappedChatComponent.getJson();
            String string = (new ChatJson(message)).convertToString();
            ChatGradient gradient = new ChatGradient(string);
            if (gradient.isGradient((HypeGradients) getPlugin())) {
                wrappedChatComponent.setJson((new ChatJson(gradient.translateGradient((HypeGradients) getPlugin()))).convertToJson());
                components.add(wrappedChatComponent);
            } else {
                components.add(wrappedChatComponent);
            }
        });
        for (WrappedChatComponent component : components) {
            metaData.setMessages(component);
        }
    }
}
