package io.inabsentia.distributedhangman.controller;

import brugerautorisation.data.Bruger;
import io.inabsentia.distributedhangman.controller.exceptions.UserControllerException;
import io.inabsentia.distributedhangman.controller.interfaces.IGameController;
import io.inabsentia.distributedhangman.controller.interfaces.IUserController;
import io.inabsentia.distributedhangman.logic.IGameLogic;
import io.inabsentia.distributedhangman.ui.Tui;
import io.inabsentia.distributedhangman.util.Utils;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public final class GameController implements IGameController {

    private final IGameLogic logic;
    private final IUserController userController = UserController.getInstance();
    private final Tui tui = Tui.getInstance();

    /* Static Singleton instance */
    private static IGameController instance;

    private final Scanner scanner;

    /*
     * Static initialization block for the Singleton instance.
     */
    static {
        try {
            instance = new GameController();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton MenuController instance!");
        }
    }

    /*
     * Private constructor for Singleton.
     */
    private GameController() throws Exception {
        scanner = new Scanner(System.in);
        logic = (IGameLogic) Naming.lookup(Utils.RMI_STUB_URL_LOGIC);
    }

    /*
     * Singleton instance getter.
     */
    public static synchronized IGameController getInstance() {
        return instance;
    }

    public void start() throws RemoteException {
        /* Reset the logic */
        logic.reset();

        /* Start the timer */
        logic.startTimer();

        /* Clear screen */
        tui.clearScreen();

        /* Get user, because why not.. */
        Bruger user = null;
        try {
            user = userController.getUser();
        } catch (UserControllerException e) {
            e.printStackTrace();
        }

        /* Print hangman */
        tui.printHangman(user.fornavn, logic.getTimeElapsed(), logic.getLifeLeft(), logic.getScore(), logic.getHiddenWord(), logic.getUsedCharactersString());
        System.out.println("DEBUG Word: " + logic.getWord());

        /* Infinite game loop */
        while (true) {

            /* Get guess from the user */
            Character guess;
            do {
                guess = tui.getUserCommand(Utils.CMD_ARROW, "Guess").charAt(0);
            } while (logic.isCharGuessed(guess));

            /* Clear screen */
            tui.clearScreen();

            if (logic.guess(guess)) {
                logic.addScore(Utils.SINGLE_CHAR_SCORE);
                tui.printHangman(user.fornavn, logic.getTimeElapsed(), logic.getLifeLeft(), logic.getScore(), logic.getHiddenWord(), logic.getUsedCharactersString());
                tui.printCorrectGuess();
            } else {
                logic.decreaseLife();
                tui.printHangman(user.fornavn, logic.getTimeElapsed(), logic.getLifeLeft(), logic.getScore(), logic.getHiddenWord(), logic.getUsedCharactersString());
                tui.printWrongGuess();
            }



        }
    }

}