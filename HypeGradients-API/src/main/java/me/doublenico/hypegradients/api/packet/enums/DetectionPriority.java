package me.doublenico.hypegradients.api.packet.enums;

import org.bukkit.event.EventPriority;

/**
 * The priority of the detection
 * Idea from {@link EventPriority}
 * <p>
 * Detections with lower priority are called first
 * will listeners with higher priority are called last.
 * <p>
 * Detections are called in following order:
 * {@link #LOWEST} -> {@link #LOW} -> {@link #NORMAL} -> {@link #HIGH} -> {@link #HIGHEST}
 */
public enum DetectionPriority {

    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4);

    private final int slot;

    DetectionPriority(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}
