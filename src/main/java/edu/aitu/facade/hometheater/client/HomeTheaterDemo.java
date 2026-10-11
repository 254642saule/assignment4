package edu.aitu.facade.hometheater.client;

import edu.aitu.facade.hometheater.facade.HomeTheaterFacade;

/** Demonstrates the facade without directly accessing subsystem classes. */
public final class HomeTheaterDemo {
    private HomeTheaterDemo() {
    }

    public static void main(String[] args) {
        String movieTitle = args.length == 0 ? "Interstellar" : String.join(" ", args);
        HomeTheaterFacade homeTheater = new HomeTheaterFacade();
        homeTheater.watchMovie(movieTitle);
        homeTheater.endMovie();
    }
}
