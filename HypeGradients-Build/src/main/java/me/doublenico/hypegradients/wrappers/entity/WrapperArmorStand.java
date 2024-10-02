package me.doublenico.hypegradients.wrappers.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.doublenico.hypegradients.api.packet.AbstractPacket;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperArmorStand extends AbstractPacket {
    public WrapperArmorStand(PacketContainer handle) {
        super(handle, PacketType.Play.Server.SPAWN_ENTITY);
    }

    /**
     * Retrieve entity ID of the Object.
     *
     * @return The current EID
     */
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }

    /**
     * Retrieve the entity that will be spawned.
     *
     * @param world - the current world of the entity.
     * @return The spawned entity.
     */
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }

    /**
     * Retrieve the entity that will be spawned.
     *
     * @param event - the packet event.
     * @return The spawned entity.
     */
    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }


    @Override
    public WrappedChatComponent getWrappedChatComponent() {
        return null;
    }

    @Override
    public void setWrappedChatComponent(WrappedChatComponent value) {

    }
}
