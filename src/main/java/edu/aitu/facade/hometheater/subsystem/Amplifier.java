package edu.aitu.facade.hometheater.subsystem;

/** Controls amplifier power, input, volume, and surround-sound mode. */
public final class Amplifier {
    private boolean poweredOn;
    private String input = "None";
    private int volume;
    private boolean surroundSoundEnabled;

    public void turnOn() {
        poweredOn = true;
    }

    public void turnOff() {
        poweredOn = false;
        input = "None";
        volume = 0;
        surroundSoundEnabled = false;
    }

    public void selectInput(String input) {
        requirePoweredOn();
        this.input = requireText(input, "Input");
    }

    public void enableSurroundSound() {
        requirePoweredOn();
        surroundSoundEnabled = true;
    }

    public void setVolume(int volume) {
        requirePoweredOn();
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Volume must be between 0 and 100.");
        }
        this.volume = volume;
    }

    private void requirePoweredOn() {
        if (!poweredOn) {
            throw new IllegalStateException("The amplifier must be on before it can be configured.");
        }
    }

    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank.");
        }
        return value.trim();
    }
}
