package edu.aitu.facade.hometheater.subsystem;

/** Controls the popcorn popper and records the number of servings made. */
public final class PopcornPopper {
    private boolean poweredOn;
    private int servingsPopped;

    public void turnOn() {
        poweredOn = true;
    }

    public void turnOff() {
        poweredOn = false;
    }

    public void pop(int servings) {
        if (!poweredOn) {
            throw new IllegalStateException("Turn on the popcorn popper before popping.");
        }
        if (servings <= 0) {
            throw new IllegalArgumentException("Servings must be greater than zero.");
        }
        servingsPopped += servings;
    }
}
