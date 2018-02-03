package io.inabsentia.distributedhangman.controller;

import io.inabsentia.distributedhangman.controller.interfaces.IGameController;
import io.inabsentia.distributedhangman.logic.GameLogic;
import io.inabsentia.distributedhangman.ui.Tui;

public final class GameController implements IGameController {

    private final GameLogic logic = GameLogic.getInstance();
    private final Tui tui = Tui.getInstance();

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

    public void startGame() {
        tui.printHangman(logic.getTime(), logic.getLifeLeft(), logic.getScore(), logic.getHiddenWord(), logic.getUsedLettersString());
    }

}