package me.doublenico.hypegradients.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.doublenico.hypegradients.api.AbstractPacket;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WrapperGuiMessage extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.WINDOW_ITEMS;

    public WrapperGuiMessage() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperGuiMessage(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Window ID.
     * <p>
     * Notes: the id of window which items are being sent for. 0 for player
     * inventory.
     *
     * @return The current Window ID
     */
    public int getWindowId() {
        return handle.getIntegers().read(0);
    }

    /**
     * Set Window ID.
     *
     * @param value - new value.
     */
    public void setWindowId(int value) {
        handle.getIntegers().write(0, value);
    }

    /**
     * Retrieve Slot data.
     *
     * @return The current Slot data
     */
    public List<ItemStack> getSlotData() {
        return handle.getItemListModifier().read(0);
    }

    /**
     * Set Slot data.
     *
     * @param value - new value.
     */
    public void setSlotData(List<ItemStack> value) {
        handle.getItemListModifier().write(0, value);
    }
}
