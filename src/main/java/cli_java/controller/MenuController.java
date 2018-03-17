package cli_java.controller;

import brugerautorisation.data.Bruger;
import cli_java.controller.interfaces.IGameController;
import cli_java.controller.interfaces.IMenuController;
import cli_java.ui.Tui;
import server.logic.rmi.IGameLobby;
import server.util.Utils;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public final class MenuController implements IMenuController {

    /* Fields */
    public static String USERNAME = "NONE";

    /* Singleton Objects */
    private final Tui tui = Tui.getInstance();
    private final IGameController gameController = GameController.getInstance();

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
    private MenuController() throws Exception {
        //logic = (IGameLogic) Naming.lookup(Utils.RMI_STUB_URL_REMOTE_LOGIC_JAVABOG);
        lobby = (IGameLobby) Naming.lookup(Utils.RMI_LOBBY_STUB_URL_LOCAL);
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
            case "1":
                tui.printSignInPrompt();
                logIn();
                break;
            case "2":
                logOut();
                break;
            case "3":
                gameController.start();
                break;
            case "4":
                List<String> users = lobby.getAllCurrentUserNames();
                List<Integer> scores = new ArrayList<>();
                for (String user : users) {
                    int score = lobby.getGameLogicInstance(user).getScore();
                    scores.add(score);
                }
                tui.printLobby(users, scores);
                break;
            case "5":
                tui.printUserInformation(getUserHelper());
                break;
            case "6":
                //String highscore = logic.getUserField(getUserHelper().brugernavn, getUserHelper().adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
                //tui.printUserHighScore(getUserHelper(), highscore);
                break;
            case "7":
                tui.printAbout();
                break;
            case "0":
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

            MenuController.USERNAME = username;
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
            lobby.logOut(USERNAME);
            MenuController.USERNAME = "NONE";
            tui.printMenu(getUserHelper());
            tui.printSignOutSuccess();
        } catch (RemoteException e) {
            tui.printMenu(getUserHelper());
            tui.printSignOutFailure();
        }
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    private Bruger getUserHelper() {
        try {
            return lobby.getLoggedInUser(USERNAME);
        } catch (RemoteException e) {
            return null;
        }
    }

}