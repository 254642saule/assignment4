package edu.aitu.facade.hometheater.subsystem;
public final class DiscPlayer {
    private boolean poweredOn;
    private String loadedMovie;
    private boolean playing;

    public void turnOn() {
        poweredOn = true;
    }

    public void turnOff() {
        playing = false;
        loadedMovie = null;
        poweredOn = false;
    }

    public void loadMovie(String title) {
        requirePoweredOn();
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Movie title must not be blank.");
        }
        if (loadedMovie != null) {
            throw new IllegalStateException("Eject the current disc before loading another movie.");
        }
        loadedMovie = title.trim();
    }

    public void play() {
        requirePoweredOn();
        if (loadedMovie == null) {
            throw new IllegalStateException("Load a movie before starting playback.");
        }
        playing = true;
    }

    public void stop() {
        playing = false;
    }

    public void eject() {
        if (playing) {
            throw new IllegalStateException("Stop playback before ejecting the disc.");
        }
        loadedMovie = null;
    }

    private void requirePoweredOn() {
        if (!poweredOn) {
            throw new IllegalStateException("The disc player must be on before it can be used.");
        }
    }
}
