package cli_java.controller;

import brugerautorisation.data.Bruger;
import cli_java.controller.interfaces.IGameController;
import cli_java.controller.interfaces.IMenuController;
import cli_java.handler.UserHandler;
import cli_java.ui.Tui;
import server.logic.rmi.IGameLobby;
import server.util.Utils;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

public final class MenuController implements IMenuController {

    /* Singleton Objects */
    private final IGameController gameController = GameController.getInstance();
    private final Tui tui = Tui.getInstance();

    private final IGameLobby lobby;

    /* Static Singleton instance */
    private static IMenuController instance;

    /*
     * Static initialization block for the Singleton instance.
     */
    static {
        try {
            instance = new MenuController();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton MenuController instance!");
        }
    }

    /*
     * Private constructor for Singleton.
     */
    private MenuController() {
        try {
            lobby = (IGameLobby) Naming.lookup(Utils.RMI_LOBBY_STUB_URL_LOCAL);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException("Failed to get the RMI Lobby stub!");
        }
    }

    /*
     * Singleton instance getter.
     */
    public static synchronized IMenuController getInstance() {
        return instance;
    }

    @Override
    public void start() {
        tui.printMenu(getUserHelper());

        while (true) {
            String command = tui.getUserCommand();
            tui.printMenu(getUserHelper());

            try {
                executeUserCommand(command);
            } catch (RemoteException e) {
                tui.printError();
            }
        }

    }

    @Override
    public void executeUserCommand(String command) throws RemoteException {
        command = command.toLowerCase();
        switch (command) {
            case "q":
                if (!UserHandler.isLoggedIn()) {
                    tui.printLogInPrompt();
                    logIn();
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "w":
                if (UserHandler.isLoggedIn()) {
                    logOut();
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "e":
                if (UserHandler.isLoggedIn()) {
                    tui.printUserInformation(getUserHelper());
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "r":
                if (UserHandler.isLoggedIn()) {
                    gameController.start();
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "t":
                if (UserHandler.isLoggedIn()) {
                    Map<String, Integer> scoreMap = lobby.getAllUsersScore();
                    tui.printLobby(scoreMap);
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "a":
                if (UserHandler.isLoggedIn()) {
                    Map<String, Integer> highscoreMap = lobby.getAllUsersScore();
                    tui.printHighScoreList(highscoreMap);
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "s":
                if (UserHandler.isLoggedIn()) {
                    // Send Email
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "d":
                if (!UserHandler.isLoggedIn()) {
                    // Forgot Pass
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "f":
                if (UserHandler.isLoggedIn()) {
                    // New Pass
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "g":
                tui.printAbout();
                break;
            case "x":
                tui.printExit();
                exit();
                break;
            default:
                tui.printUnrecognizedCommand();
                break;
        }
    }

    @Override
    public void logIn() {
        String username = tui.getUserCommand("Username");
        String password = tui.getUserCommand("Password");

        try {
            try {
                lobby.logIn(username, password);
            } catch (IllegalArgumentException e) {
                lobby.logOut(username);
                lobby.logIn(username, password);
            }

            UserHandler.setCurrentUsername(username);
            tui.printMenu(getUserHelper());
            tui.printLogInSuccess();
        } catch (RemoteException e) {
            tui.printMenu(getUserHelper());
            tui.printLogInFailure();
        }
    }

    @Override
    public void logOut() {
        try {
            if (!UserHandler.getCurrentUsername().equals(UserHandler.NOT_LOGGED_IN)) {
                lobby.logOut(UserHandler.getCurrentUsername());
                UserHandler.resetUsername();
            } else {
                return;
            }
            tui.printMenu(getUserHelper());
            tui.printLogOutSuccess();
        } catch (RemoteException e) {
            tui.printMenu(getUserHelper());
            tui.printLogOutFailure();
        }
    }

    @Override
    public void exit() {
        try {
            if (!UserHandler.getCurrentUsername().equals(UserHandler.NOT_LOGGED_IN))
                lobby.logOut(UserHandler.getCurrentUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private Bruger getUserHelper() {
        try {
            return lobby.getLoggedInUser(UserHandler.getCurrentUsername());
        } catch (RemoteException e) {
            return null;
        }
    }

    private void sendEmail() {

    }

    private void forgotPassword() {

    }

    private void newPassword() {

    }

}