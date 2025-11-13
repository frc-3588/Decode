package org.firstinspires.ftc.teamcode.utils; // Or your preferred package

import java.util.function.BooleanSupplier;

/**
 * A Button that remains active for a short period after its condition is no longer met.
 * This is useful for noisy sensors, like an optical distance sensor, where the reading
 * might briefly drop out.
 */
public class DebouncedButton  {

    private final BooleanSupplier condition;
    private final double debounceDelay; // in milliseconds
    private long lastActiveTime = 0;

    /**
     * Creates a new DebouncedButton.
     *
     * @param debounceDelay Delay in ms from when true swaps to false
     * @param condition Boolean input to check
     */
    public DebouncedButton(double debounceDelay, BooleanSupplier condition) {
        this.debounceDelay = debounceDelay;
        this.condition = condition;
    }

    /**
     * @return If the button is currently ture or if it was recently true
     */
    public boolean get() {
        if (condition.getAsBoolean()) {
            lastActiveTime = System.currentTimeMillis();
        }

        // Return true if the current time is within the debounce window of the last true signal.
        return (System.currentTimeMillis() - lastActiveTime) < debounceDelay;
    }
}
