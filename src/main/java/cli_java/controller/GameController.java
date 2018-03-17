package cli_java.controller;

import brugerautorisation.data.Bruger;
import cli_java.controller.interfaces.IGameController;
import cli_java.handler.UserHandler;
import cli_java.ui.Tui;
import server.logic.rmi.IGameLobby;
import server.logic.rmi.IGameLogic;

import java.rmi.RemoteException;
import java.util.Scanner;

public final class GameController implements IGameController {

    private IGameLobby lobby;
    private IGameLogic logic;

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
    }

    /*
     * Singleton instance getter.
     */
    public static synchronized IGameController getInstance() {
        return instance;
    }

    public void start(IGameLobby lobby, IGameLogic logic) throws RemoteException {
        this.lobby = lobby;
        this.logic = logic;

        /* Reset the logic and score */
        logic.resetGame();
        logic.resetScore();

        /* Get user */
        Bruger user;
        try {
            user = lobby.getLoggedInUser(UserHandler.getCurrentUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
            tui.printError();
            return;
        }

        /* Print hangman */
        tui.printHangman(user.fornavn, logic.getLife(), logic.getScore(), logic.getWord(), logic.getGuessedChars());

        /* Infinite game loop */
        while (true) {

            /* Get guessCharacter from the user */
            Character guess;

            while (true) {
                guess = tui.getUserCommand("Guess").charAt(0);
                if (logic.isCharGuessed(guess)) {
                    tui.printHangman(user.fornavn, logic.getLife(), logic.getScore(), logic.getWord(), logic.getGuessedChars());
                    tui.printAlreadyGuessed(guess);
                    continue;
                }
                break;
            }

            /* Guess */
            if (logic.guess(guess)) {
                tui.printHangman(user.fornavn, logic.getLife(), logic.getScore(), logic.getWord(), logic.getGuessedChars());
                tui.printCorrectGuess();
            } else {
                tui.printHangman(user.fornavn, logic.getLife(), logic.getScore(), logic.getWord(), logic.getGuessedChars());
                tui.printWrongGuess();
            }

            /* Check if the user has won */
            if (isFinished(user, logic.isGameWon(), logic.isGameLost(), logic.getScore())) {
                if (playAgain()) {

                    if (logic.isGameLost())
                        logic.resetScore();

                    logic.resetGame();

                    tui.printHangman(user.fornavn, logic.getLife(), logic.getScore(), logic.getWord(), logic.getGuessedChars());
                } else {
                    addHighScore(user);
                    tui.printMenu(user);
                    break;
                }
            }

        }
    }

    private boolean isFinished(Bruger user, boolean isWon, boolean isLost, int score) {
        try {
            if (isWon) {
                tui.printWin(logic.getWord());
                tui.getUserCommand("Press Enter");
                showScore();
                tui.getUserCommand("Press Enter");
                addHighScore(user);
                return true;
            } else if (isLost) {
                tui.printLoss();
                tui.getUserCommand("Press Enter");
                showScore();
                tui.getUserCommand("Press Enter");
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
        String command = tui.getUserCommand("y/N");
        return command.equals("y");
    }

    // If a high score was achieved, show it
    // then save it via the UserController
    // if a high score was not acheived, just print the score.
    private void showScore() {
        try {
            boolean isHighScore = logic.isHighScore(UserHandler.getCurrentUsername(), "godkode");
            System.out.println();
            tui.printNewScore(Integer.toString(logic.getScore()), isHighScore);
        } catch (RemoteException e) {
            e.printStackTrace();
            tui.printError();
        }
    }

    private void addHighScore(Bruger user) {
        try {
            boolean isHighScore = logic.isHighScore(user.brugernavn, user.adgangskode);

            if (isHighScore)
                lobby.setUserHighscore(user.brugernavn, String.valueOf(logic.getScore()));

        } catch (RemoteException e) {
            e.printStackTrace();
            tui.printError();
        }
    }

}