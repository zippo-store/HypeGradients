package me.doublenico.hypegradients.wrappers.gui;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.doublenico.hypegradients.api.packet.AbstractPacket;
import org.bukkit.inventory.ItemStack;

public class WrapperGuiSlotMessage extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.SET_SLOT;

    public WrapperGuiSlotMessage() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperGuiSlotMessage(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Slot data.
     *
     * @return The current Slot data
     */
    public ItemStack getSlotData() {
        return handle.getItemModifier().read(0);
    }

    /**
     * Set Slot data.
     *
     * @param value - new value.
     */
    public void setSlotData(ItemStack value) {
        handle.getItemModifier().write(0, value);
    }

}
