package edu.aitu.facade.hometheater.verification;

import edu.aitu.facade.hometheater.facade.HomeTheaterFacade;

import java.util.ArrayList;
import java.util.List;

/** Dependency-free checks for the facade API, validation, and coordination order. */
public final class FacadeBehaviorChecks {
    private FacadeBehaviorChecks() {
    }

    public static void main(String[] args) {
        List<String> events = new ArrayList<>();
        HomeTheaterFacade facade = new HomeTheaterFacade(events::add);

        expectThrows(IllegalArgumentException.class, () -> facade.watchMovie("  "));
        require(events.isEmpty(), "Blank titles must be rejected before device setup.");

        facade.watchMovie("Interstellar");
        List<String> startEvents = List.of(
                "Turning on popcorn popper.",
                "Popping 1 batch of popcorn.",
                "Dimming theater lights to 15%.",
                "Lowering screen.",
                "Turning on projector.",
                "Selecting DVD input on projector.",
                "Enabling widescreen mode.",
                "Turning on amplifier.",
                "Selecting DVD input on amplifier.",
                "Enabling surround sound.",
                "Setting amplifier volume to 35.",
                "Turning on disc player.",
                "Loading movie: Interstellar.",
                "Starting movie playback.");
        require(events.equals(startEvents), "The facade must coordinate startup in the documented order.");
        expectThrows(IllegalStateException.class, () -> facade.watchMovie("Arrival"));
        require(events.equals(startEvents), "A duplicate start must not change device state.");

        facade.endMovie();
        List<String> expectedEvents = new ArrayList<>(startEvents);
        expectedEvents.addAll(List.of(
                "Stopping movie playback.",
                "Ejecting disc.",
                "Turning off disc player.",
                "Turning off amplifier.",
                "Turning off projector.",
                "Raising screen.",
                "Restoring theater lights to 100%.",
                "Turning off popcorn popper."));
        require(events.equals(expectedEvents), "The facade must restore the subsystem during shutdown.");
        expectThrows(IllegalStateException.class, facade::endMovie);

        System.out.println("Facade behavior checks passed.");
    }

    private static void expectThrows(Class<? extends RuntimeException> expectedType, Runnable action) {
        try {
            action.run();
        } catch (RuntimeException actual) {
            if (expectedType.isInstance(actual)) {
                return;
            }
            throw new AssertionError("Expected " + expectedType.getSimpleName()
                    + " but caught " + actual.getClass().getSimpleName() + ".", actual);
        }
        throw new AssertionError("Expected " + expectedType.getSimpleName() + " to be thrown.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
