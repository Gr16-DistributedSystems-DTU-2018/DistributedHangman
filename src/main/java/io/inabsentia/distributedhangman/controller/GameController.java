package io.inabsentia.distributedhangman.controller;

import brugerautorisation.data.Bruger;
import io.inabsentia.distributedhangman.controller.interfaces.IGameController;
import io.inabsentia.distributedhangman.ui.Tui;
import io.inabsentia.distributedhangman.util.Utils;
import io.inabsentia.gameserver.logic.rmi.IGameLogic;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public final class GameController implements IGameController {

    private final IGameLogic logic;
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
            throw new RuntimeException("Fatal error creating Singleton GameController instance!");
        }
    }

    /*
     * Private constructor for Singleton.
     */
    private GameController() throws Exception {
        scanner = new Scanner(System.in);
        logic = (IGameLogic) Naming.lookup(Utils.RMI_STUB_URL_REMOTE_LOGIC_JAVABOG);
    }

    /*
     * Singleton instance getter.
     */
    public static synchronized IGameController getInstance() {
        return instance;
    }

    public void start() throws RemoteException {
        /* Reset the logic */
        logic.resetGame();

        /* Start the timer */
        logic.startGameTimer();

        /* Clear screen */
        tui.clearScreen();

        /* Get user */
        Bruger user;
        try {
            user = logic.getCurrentUser();
        } catch (RemoteException e) {
            e.printStackTrace();
            tui.printError();
            return;
        }

        /* Print hangman */
        tui.printHangman(user.fornavn, logic.getGameTimeElapsed(), logic.getGameLife(), logic.getGameScore(), logic.getHiddenWord(), logic.getUsedCharacters());

        /* Infinite game loop */
        while (true) {

            /* Get guessCharacter from the user */
            Character guess;

            while (true) {
                guess = tui.getUserCommand(Utils.CMD_ARROW, "Guess").charAt(0);
                if (logic.isCharGuessed(guess)) {
                    tui.clearScreen();
                    tui.printHangman(user.fornavn, logic.getGameTimeElapsed(), logic.getGameLife(), logic.getGameScore(), logic.getHiddenWord(), logic.getUsedCharacters());
                    tui.printAlreadyGuessed(guess);
                    continue;
                }
                break;
            }

            /* Clear screen */
            tui.clearScreen();

            /* Guess */
            if (logic.guessCharacter(guess)) {
                logic.addGameScore(Utils.SINGLE_CHAR_SCORE);
                tui.printHangman(user.fornavn, logic.getGameTimeElapsed(), logic.getGameLife(), logic.getGameScore(), logic.getHiddenWord(), logic.getUsedCharacters());
                tui.printCorrectGuess();
            } else {
                logic.decreaseLife();
                tui.printHangman(user.fornavn, logic.getGameTimeElapsed(), logic.getGameLife(), logic.getGameScore(), logic.getHiddenWord(), logic.getUsedCharacters());
                tui.printWrongGuess();
            }

            /* Check if the user has won */
            if (isFinished(user, logic.isGameWon(), logic.isGameLost(), logic.getGameScore())) {
                if (playAgain()) {
                    /* Save the old score if the user wants to play again */
                    int saveScore = 0;
                    if (logic.isGameWon())
                        saveScore = logic.getGameScore();

                    logic.resetGame();

                    /* And add it back */
                    logic.addGameScore(saveScore);

                    logic.resetGameTimer();
                    tui.clearScreen();
                    logic.startGameTimer();
                    tui.printHangman(user.fornavn, logic.getGameTimeElapsed(), logic.getGameLife(), logic.getGameScore(), logic.getHiddenWord(), logic.getUsedCharacters());
                } else {
                    addHighScore(user, logic.getGameScore());
                    tui.clearScreen();
                    tui.printMenu(user, false);
                    break;
                }
            }

        }
    }

    private boolean isFinished(Bruger user, boolean isWon, boolean isLost, int score) {
        try {
            if (isWon) {
                tui.printWin(logic.getGameWord());
                tui.getUserCommand(Utils.CMD_ARROW, "Press Enter");
                showScore(logic.getGameScore());
                tui.getUserCommand(Utils.CMD_ARROW, "Press Enter");
                addHighScore(user, score);
                return true;
            } else if (isLost) {
                tui.printLoss(logic.getGameWord());
                tui.getUserCommand(Utils.CMD_ARROW, "Press Enter");

                showScore(logic.getGameScore());
                tui.getUserCommand(Utils.CMD_ARROW, "Press Enter");
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * Ask the user whether they would
     * like to play again or not.
     */
    private boolean playAgain() {
        tui.printPlayAgain();
        String command = tui.getUserCommand(Utils.CMD_ARROW, "y/N");
        return command.equals("y");
    }

    // If a high score was achieved, show it
    // then save it via the UserController
    // if a high score was not acheived, just print the score.
    private void showScore(int score) {
        try {
            boolean isHighScore = logic.isHighScore(score);
            tui.printNewScore(Integer.toString(score), isHighScore);
        } catch (RemoteException e) {
            e.printStackTrace();
            tui.printError();
        }
    }

    private void addHighScore(Bruger user, int score) {
        try {
            boolean isHighScore = logic.isHighScore(score);

            if (isHighScore)
                logic.setUserField(user.brugernavn, user.adgangskode, Utils.HIGH_SCORE_FIELD_KEY, String.valueOf(score));

        } catch (RemoteException e) {
            e.printStackTrace();
            tui.printError();
        }
    }

}