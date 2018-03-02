package distributedhangman_cli.controller;

import brugerautorisation.data.Bruger;
import distributedhangman_cli.controller.interfaces.IGameController;
import distributedhangman_cli.ui.Tui;
import distributedhangman_cli.util.Utils;
import server.logic.rmi.IGameLogic;

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
        /* Reset the logic and score */
        logic.resetGame();
        logic.resetScore();

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
        tui.printHangman(user.fornavn, logic.getGameTimeElapsed(), logic.getCurrentLife(), logic.getCurrentScore(), logic.getCurrentGuessedWord(), logic.getGuessedChars());

        /* Infinite game loop */
        while (true) {

            /* Get guessCharacter from the user */
            Character guess;

            while (true) {
                guess = tui.getUserCommand(Utils.CMD_ARROW, "Guess").charAt(0);
                if (logic.isCharGuessed(guess)) {
                    tui.clearScreen();
                    tui.printHangman(user.fornavn, logic.getGameTimeElapsed(), logic.getCurrentLife(), logic.getCurrentScore(), logic.getCurrentGuessedWord(), logic.getGuessedChars());
                    tui.printAlreadyGuessed(guess);
                    continue;
                }
                break;
            }

            /* Clear screen */
            tui.clearScreen();

            /* Guess */
            if (logic.guess(guess)) {
                //logic.addGameScore(Utils.SINGLE_CHAR_SCORE);
                tui.printHangman(user.fornavn, logic.getGameTimeElapsed(), logic.getCurrentLife(), logic.getCurrentScore(), logic.getCurrentGuessedWord(), logic.getGuessedChars());
                tui.printCorrectGuess();
            } else {
                //logic.decreaseLife();
                tui.printHangman(user.fornavn, logic.getGameTimeElapsed(), logic.getCurrentLife(), logic.getCurrentScore(), logic.getCurrentGuessedWord(), logic.getGuessedChars());
                tui.printWrongGuess();
            }

            /* Check if the user has won */
            if (isFinished(user, logic.isGameWon(), logic.isGameLost(), logic.getCurrentScore())) {
                if (playAgain()) {

                    if (logic.isGameLost())
                        logic.resetScore();

                    logic.resetGame();

                    tui.clearScreen();
                    tui.printHangman(user.fornavn, logic.getGameTimeElapsed(), logic.getCurrentLife(), logic.getCurrentScore(), logic.getCurrentGuessedWord(), logic.getGuessedChars());
                } else {
                    addHighScore(user);
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
                tui.printWin(logic.getCurrentGuessedWord());
                tui.getUserCommand(Utils.CMD_ARROW, "Press Enter");
                showScore();
                tui.getUserCommand(Utils.CMD_ARROW, "Press Enter");
                addHighScore(user);
                return true;
            } else if (isLost) {
                tui.printLoss();
                tui.getUserCommand(Utils.CMD_ARROW, "Press Enter");
                showScore();
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
    private void showScore() {
        try {
            boolean isHighScore = logic.isHighScore();
            tui.printNewScore(Integer.toString(logic.getCurrentScore()), isHighScore);
        } catch (RemoteException e) {
            e.printStackTrace();
            tui.printError();
        }
    }

    private void addHighScore(Bruger user) {
        try {
            boolean isHighScore = logic.isHighScore();

            if (isHighScore)
                logic.setUserField(user.brugernavn, user.adgangskode, Utils.HIGH_SCORE_FIELD_KEY, String.valueOf(logic.getCurrentScore()));

        } catch (RemoteException e) {
            e.printStackTrace();
            tui.printError();
        }
    }

}