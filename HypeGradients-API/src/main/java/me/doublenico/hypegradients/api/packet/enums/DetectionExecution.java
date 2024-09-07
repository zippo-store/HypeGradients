package me.doublenico.hypegradients.api.packet.enums;

/**
 * Used to determine when the detection should be executed
 * <p>
 * {@link #BEFORE} -> Execute the detection before the gradient is detected
 * <p>
 * {@link #AFTER} -> Execute the detection after the gradient is detected
 */
public enum DetectionExecution {
    /**
     * Execute the detection before the gradient is detected
     */
    BEFORE(true),
    /**
     * Execute the detection after the gradient is detected
     */
    AFTER(false);

    private final boolean before;

    DetectionExecution(boolean before) {
        this.before = before;
    }

    public boolean isBefore() {
        return before;
    }
}
