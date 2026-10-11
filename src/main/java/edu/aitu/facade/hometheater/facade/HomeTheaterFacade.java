package edu.aitu.facade.hometheater.facade;

import edu.aitu.facade.hometheater.subsystem.Amplifier;
import edu.aitu.facade.hometheater.subsystem.DiscPlayer;
import edu.aitu.facade.hometheater.subsystem.PopcornPopper;
import edu.aitu.facade.hometheater.subsystem.Projector;
import edu.aitu.facade.hometheater.subsystem.Screen;
import edu.aitu.facade.hometheater.subsystem.TheaterLights;

import java.util.Objects;
import java.util.function.Consumer;

/** Offers simple movie-session operations over the home-theater subsystem. */
public final class HomeTheaterFacade {
    private final Amplifier amplifier = new Amplifier();
    private final DiscPlayer discPlayer = new DiscPlayer();
    private final Projector projector = new Projector();
    private final Screen screen = new Screen();
    private final TheaterLights theaterLights = new TheaterLights();
    private final PopcornPopper popcornPopper = new PopcornPopper();
    private final Consumer<String> output;
    private String activeMovieTitle;

    /** Creates a facade that prints the coordinated steps to the console. */
    public HomeTheaterFacade() {
        this(System.out::println);
    }

    /** Creates a facade with an output sink, useful for tests and alternate UIs. */
    public HomeTheaterFacade(Consumer<String> output) {
        this.output = Objects.requireNonNull(output, "Output sink must not be null.");
    }

    /** Starts a movie session and configures all devices in the required order. */
    public void watchMovie(String movieTitle) {
        String title = requireMovieTitle(movieTitle);
        if (activeMovieTitle != null) {
            throw new IllegalStateException("A movie is already playing. End it before starting another.");
        }

        try {
            perform("Turning on popcorn popper.", popcornPopper::turnOn);
            perform("Popping 1 batch of popcorn.", () -> popcornPopper.pop(1));
            perform("Dimming theater lights to 15%.", () -> theaterLights.setBrightness(15));
            perform("Lowering screen.", screen::lower);
            perform("Turning on projector.", projector::turnOn);
            perform("Selecting DVD input on projector.", () -> projector.selectInput("DVD"));
            perform("Enabling widescreen mode.", projector::enableWidescreen);
            perform("Turning on amplifier.", amplifier::turnOn);
            perform("Selecting DVD input on amplifier.", () -> amplifier.selectInput("DVD"));
            perform("Enabling surround sound.", amplifier::enableSurroundSound);
            perform("Setting amplifier volume to 35.", () -> amplifier.setVolume(35));
            perform("Turning on disc player.", discPlayer::turnOn);
            perform("Loading movie: " + title + ".", () -> discPlayer.loadMovie(title));
            perform("Starting movie playback.", discPlayer::play);
            activeMovieTitle = title;
        } catch (RuntimeException cause) {
            IllegalStateException failure = new IllegalStateException(
                    "Could not start the movie session for '" + title + "'.", cause);
            rollBackStartup(failure);
            throw failure;
        }
    }

    /** Ends the active session and attempts every subsystem cleanup step. */
    public void endMovie() {
        if (activeMovieTitle == null) {
            throw new IllegalStateException("There is no active movie session to end.");
        }

        RuntimeException failure = null;
        failure = runCleanupStep("Stopping movie playback.", discPlayer::stop, failure);
        failure = runCleanupStep("Ejecting disc.", discPlayer::eject, failure);
        failure = runCleanupStep("Turning off disc player.", discPlayer::turnOff, failure);
        failure = runCleanupStep("Turning off amplifier.", amplifier::turnOff, failure);
        failure = runCleanupStep("Turning off projector.", projector::turnOff, failure);
        failure = runCleanupStep("Raising screen.", screen::raise, failure);
        failure = runCleanupStep("Restoring theater lights to 100%.",
                () -> theaterLights.setBrightness(100), failure);
        failure = runCleanupStep("Turning off popcorn popper.", popcornPopper::turnOff, failure);

        if (failure != null) {
            throw new IllegalStateException("One or more devices could not be shut down cleanly.", failure);
        }
        activeMovieTitle = null;
    }

    private void rollBackStartup(RuntimeException failure) {
        tryCleanup(discPlayer::stop, failure);
        tryCleanup(discPlayer::eject, failure);
        tryCleanup(discPlayer::turnOff, failure);
        tryCleanup(amplifier::turnOff, failure);
        tryCleanup(projector::turnOff, failure);
        tryCleanup(screen::raise, failure);
        tryCleanup(() -> theaterLights.setBrightness(100), failure);
        tryCleanup(popcornPopper::turnOff, failure);
        activeMovieTitle = null;
    }

    private RuntimeException runCleanupStep(
            String description, Runnable action, RuntimeException firstFailure) {
        try {
            action.run();
            output.accept(description);
        } catch (RuntimeException cleanupFailure) {
            if (firstFailure == null) {
                return cleanupFailure;
            }
            if (cleanupFailure != firstFailure) {
                firstFailure.addSuppressed(cleanupFailure);
            }
        }
        return firstFailure;
    }

    private static void tryCleanup(Runnable cleanup, RuntimeException originalFailure) {
        try {
            cleanup.run();
        } catch (RuntimeException cleanupFailure) {
            if (cleanupFailure != originalFailure) {
                originalFailure.addSuppressed(cleanupFailure);
            }
        }
    }

    private void perform(String description, Runnable action) {
        action.run();
        output.accept(description);
    }

    private static String requireMovieTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Movie title must not be blank.");
        }
        return title.trim();
    }
}
