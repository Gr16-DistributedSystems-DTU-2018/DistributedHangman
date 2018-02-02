package io.inabsentia.distributedhangman.controller;

import io.inabsentia.distributedhangman.controller.interfaces.IGameController;

public final class GameController implements IGameController {

    /* Static Singleton instance */
    private static IGameController instance;

    /*
     * Static initialization block for the Singleton instance.
     */
    static {
        try {
            instance = new GameController();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton ScreenController instance!");
        }
    }

    /*
     * Private constructor for Singleton.
     */
    private GameController() {

    }

    /*
     * Singleton instance getter.
     */
    public static synchronized IGameController getInstance() {
        return instance;
    }

}