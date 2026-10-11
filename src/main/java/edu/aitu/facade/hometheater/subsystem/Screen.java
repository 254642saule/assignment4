package edu.aitu.facade.hometheater.subsystem;

/** Raises and lowers the projection screen. */
public final class Screen {
    private boolean lowered;

    public void lower() {
        lowered = true;
    }

    public void raise() {
        lowered = false;
    }
}
