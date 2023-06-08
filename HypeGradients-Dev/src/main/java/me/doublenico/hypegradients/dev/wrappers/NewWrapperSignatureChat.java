package me.doublenico.hypegradients.dev.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.AdventureComponentConverter;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.AbstractPacket;

public class NewWrapperSignatureChat extends AbstractPacket {

    public NewWrapperSignatureChat(PacketContainer packet) {
        super(packet, PacketType.Play.Server.SYSTEM_CHAT);
    }

    public WrappedChatComponent getMessage() {
        WrappedChatComponent component = null;
        try {
            StructureModifier<Object> adventureModifier = handle.getModifier().withType(AdventureComponentConverter.getComponentClass());
            if (!adventureModifier.getFields().isEmpty()) { // Only true on Paper
                net.kyori.adventure.text.Component comp = (net.kyori.adventure.text.Component) adventureModifier.read(0);
                if (comp != null) {
                    component = AdventureComponentConverter.fromComponent(comp);
                    adventureModifier.write(0, null); // Reset to null - the JSON message will be set instead
                }
            }
        } catch (Throwable ignored) {
        } // Ignore if paper is unavailable

        if (component == null) {
            component = WrappedChatComponent.fromJson(handle.getStrings().read(0));
        }
        handle.getStrings().write(0, component.getJson());
        return component;
    }


    public void setMessage(String json) {
        handle.getStrings().write(0, json);
    }


    public boolean isActionbar() {
        return handle.getBooleans().read(0);
    }


}
