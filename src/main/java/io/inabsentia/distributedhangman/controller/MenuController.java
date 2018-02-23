package io.inabsentia.distributedhangman.controller;

import brugerautorisation.data.Bruger;
import io.inabsentia.distributedhangman.controller.interfaces.IGameController;
import io.inabsentia.distributedhangman.controller.interfaces.IMenuController;
import io.inabsentia.distributedhangman.ui.Tui;
import io.inabsentia.distributedhangman.util.Utils;
import io.inabsentia.gameserver.logic.rmi.IGameLogic;

import java.rmi.Naming;
import java.rmi.RemoteException;

public final class MenuController implements IMenuController {

    /* Fields */
    private boolean isCLSOn = false;

    /* Singleton Objects */
    private final Tui tui = Tui.getInstance();
    private final IGameController gameController = GameController.getInstance();
    private final IGameLogic logic;

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
        logic = (IGameLogic) Naming.lookup(Utils.RMI_STUB_URL_REMOTE_LOGIC_JAVABOG);
    }

    /*
     * Singleton instance getter.
     */
    public static synchronized IMenuController getInstance() {
        return instance;
    }

    @Override
    public void start() {
        clearScreenHelper();

        tui.printMenu(getUserHelper(), isCLSOn);

        while (true) {
            String command = tui.getUserCommand(Utils.CMD_ARROW);

            clearScreenHelper();
            tui.printMenu(getUserHelper(), isCLSOn);

            try {
                executeUserCommand(command);
            } catch (RemoteException e) {
                tui.printError();
            }
        }

    }

    @Override
    public void executeUserCommand(String command) throws RemoteException {
        switch (command) {
            case "q":
                if (!logic.isLoggedIn()) {
                    tui.printSignInPrompt();
                    signIn();
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "w":
                if (logic.isLoggedIn())
                    signOut();
                else
                    tui.printUnrecognizedCommand();
                break;
            case "e":
                if (logic.isLoggedIn())
                    gameController.start();
                else
                    tui.printUnrecognizedCommand();
                break;
            case "r":
                if (logic.isLoggedIn())
                    tui.printUserInformation(getUserHelper());
                else
                    tui.printUnrecognizedCommand();
                break;
            case "a":
                if (logic.isLoggedIn()) {
                    String highscore = logic.getUserField(getUserHelper().brugernavn, getUserHelper().adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
                    tui.printUserHighScore(getUserHelper(), highscore);
                } else {
                    tui.printUnrecognizedCommand();
                }
                break;
            case "z":
                clearScreenHelper();
                isCLSOn = !isCLSOn;
                clearScreenHelper();
                tui.printMenu(getUserHelper(), isCLSOn);
                break;
            case "x":
                tui.printAbout();
                break;
            case "c":
                tui.printExit();
                exit();
                break;
            default:
                tui.printUnrecognizedCommand();
                break;
        }
    }

    @Override
    public void signIn() {
        String username = tui.getUserCommand(Utils.CMD_ARROW, "Username");
        String password = tui.getUserCommand(Utils.CMD_ARROW, "Password");

        clearScreenHelper();

        try {
            logic.logIn(username, password);
            tui.printMenu(getUserHelper(), isCLSOn);
            tui.printLogInSuccess();
        } catch (RemoteException e) {
            tui.printMenu(getUserHelper(), isCLSOn);
            tui.printLogInFailure();
        }
    }

    @Override
    public void signOut() {
        clearScreenHelper();
        try {
            logic.logOut();
            tui.printMenu(getUserHelper(), isCLSOn);
            tui.printSignOutSuccess();
        } catch (RemoteException e) {
            tui.printMenu(getUserHelper(), isCLSOn);
            tui.printSignOutFailure();
        }
    }

    @Override
    public void exit() {
        /* Cleanup, saving, before exit, etc.. */
        System.exit(0);
    }

    private Bruger getUserHelper() {
        try {
            return logic.getCurrentUser();
        } catch (RemoteException e) {
            return null;
        }
    }

    private void clearScreenHelper() {
        if (isCLSOn) tui.clearScreen();
    }

}