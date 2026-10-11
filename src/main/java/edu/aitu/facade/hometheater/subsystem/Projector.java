package edu.aitu.facade.hometheater.subsystem;

/** Controls projector power, input selection, and display mode. */
public final class Projector {
    private boolean poweredOn;
    private String input = "None";
    private boolean widescreenEnabled;

    public void turnOn() {
        poweredOn = true;
    }

    public void turnOff() {
        poweredOn = false;
        input = "None";
        widescreenEnabled = false;
    }

    public void selectInput(String input) {
        requirePoweredOn();
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Input must not be blank.");
        }
        this.input = input.trim();
    }

    public void enableWidescreen() {
        requirePoweredOn();
        widescreenEnabled = true;
    }

    private void requirePoweredOn() {
        if (!poweredOn) {
            throw new IllegalStateException("The projector must be on before it can be configured.");
        }
    }
}
