package me.doublenico.hypegradients.wrappers.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import me.doublenico.hypegradients.api.packet.AbstractPacket;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WrapperMetaData extends AbstractPacket {


    public WrapperMetaData(PacketContainer handle) {
        super(handle, PacketType.Play.Server.ENTITY_METADATA);
    }

    public List<WrappedChatComponent> getMessages() {
        List<WrappedChatComponent> components = new ArrayList<>();
        if (handle.getDataValueCollectionModifier().readSafely(0) == null) return components;
        if (handle.getDataValueCollectionModifier().read(0).isEmpty()) return components;
        handle.getDataValueCollectionModifier().read(0).forEach(wrappedDataValue -> {
            if (!(wrappedDataValue.getValue() instanceof Optional<?> optional)) return;
            if (optional.isPresent()) {
                if (!(optional.get() instanceof WrappedChatComponent component)) return;
                wrappedDataValue.setValue(Optional.of(WrappedChatComponent.fromText("")));
                components.add(component);
            }
        });
        return components;
    }

    public void setMessages(WrappedChatComponent component) {
        handle.getDataValueCollectionModifier().read(0).forEach(wrappedDataValue -> {
            if (!(wrappedDataValue.getValue() instanceof Optional<?> optional)) return;
            if(optional.isPresent()){
                if(!(optional.get() instanceof WrappedChatComponent)) return;
                wrappedDataValue.setValue(Optional.empty());
                wrappedDataValue.setValue(Optional.of(component));
            }
        });
    }

    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the entity of the painting that will be spawned.
     *
     * @param event - the packet event.
     * @return The spawned entity.
     */
    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }

    /**
     * Retrieve Metadata.
     *
     * @return The current Metadata
     */
    public List<WrappedDataValue> getMetadata() {
        return handle.getDataValueCollectionModifier().read(0);
    }

    /**
     * Set Metadata.
     *
     * @param value - new value.
     */
    public void setMetadata(List<WrappedDataValue> value) {
        handle.getDataValueCollectionModifier().write(0, value);
    }

    @Override
    public WrappedChatComponent getWrappedChatComponent() {
        return null;
    }

    @Override
    public void setWrappedChatComponent(WrappedChatComponent value) {

    }
}
