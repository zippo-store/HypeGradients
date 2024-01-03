package me.doublenico.hypegradients.dev.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.AdventureComponentConverter;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.AbstractPacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

public class NewWrapperSignatureChat extends AbstractPacket {

    private final StructureModifier<Object> structureModifier = handle.getStructures().readSafely(0).getModifier().withType(AdventureComponentConverter.getComponentClass());


    public NewWrapperSignatureChat(PacketContainer packet) {
        super(packet, PacketType.Play.Server.SYSTEM_CHAT);
    }

    public WrappedChatComponent getMessage() {
        WrappedChatComponent component = null;

        try {
            StructureModifier<Object> adventureModifier = handle.getModifier().withType(AdventureComponentConverter.getComponentClass());
            if (!adventureModifier.getFields().isEmpty()) {
                Component comp = (Component) adventureModifier.read(0);
                adventureModifier.write(0, null);
                component = AdventureComponentConverter.fromComponent(comp);
            }
        } catch (Throwable ignored) {
        }


        if (structureModifier != null && structureModifier.readSafely(0) != null){
            Component comp = (Component) structureModifier.read(0);
            structureModifier.write(0, null);
            //TODO: IMPLEMENT FOR NEW HOVER
            component = WrappedChatComponent.fromJson(JSONComponentSerializer.builder().build().serialize(comp));
        } else {
            if (component == null && handle.getStrings().readSafely(0) != null) component = WrappedChatComponent.fromJson(handle.getStrings().read(0));
            else {
                component = handle.getChatComponents().read(0);
                return component;
            }
            handle.getStrings().write(0, component.getJson());
        }

        return component;
    }


    public void setMessage(String json) {
        if (structureModifier != null && structureModifier.readSafely(0) != null)
            structureModifier.write(0, AdventureComponentConverter.fromWrapper(WrappedChatComponent.fromJson(json)));
        else if (handle.getStrings().readSafely(0) != null) handle.getStrings().write(0, json);
        else handle.getChatComponents().write(0, WrappedChatComponent.fromJson(json));
    }

    public boolean isPaper(){
        return structureModifier != null;
    }

    public boolean isActionbar() {
        return handle.getBooleans().read(0);
    }


}