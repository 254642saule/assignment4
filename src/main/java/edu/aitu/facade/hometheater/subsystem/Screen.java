package edu.aitu.facade.hometheater.subsystem;
public final class Screen {
    private boolean lowered;

    public void lower() {
        lowered = true;
    }

    public void raise() {
        lowered = false;
    }
}
