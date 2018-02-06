package io.inabsentia.distributedhangman.controller;

import brugerautorisation.data.Bruger;
import com.sun.xml.internal.ws.assembler.jaxws.MustUnderstandTubeFactory;
import io.inabsentia.distributedhangman.controller.exceptions.UserControllerException;
import io.inabsentia.distributedhangman.controller.interfaces.IGameController;
import io.inabsentia.distributedhangman.controller.interfaces.IMenuController;
import io.inabsentia.distributedhangman.controller.interfaces.IUserController;
import io.inabsentia.distributedhangman.ui.Tui;
import io.inabsentia.distributedhangman.util.Utils;

import java.rmi.RemoteException;
import java.util.Scanner;

public final class MenuController implements IMenuController {

    /* Fields */
    private boolean isCLSOn = true;

    /* Singleton Objects */
    private final Tui tui = Tui.getInstance();
    private final IUserController userController = UserController.getInstance();
    private final IGameController gameController = GameController.getInstance();

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

            executeUserCommand(command);
        }

    }

    @Override
    public void executeUserCommand(String command) {
        switch (command) {
            case "q":
                tui.printSignInPrompt();
                signIn();
                break;
            case "w":
                if (userController.isSignedIn())
                    signOut();
                else
                    tui.printUnrecognizedCommand();
                break;
            case "e":
                if (userController.isSignedIn()) {
                    try {
                        gameController.start();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else
                    tui.printUnrecognizedCommand();
                break;
            case "r":
                if (userController.isSignedIn())
                    tui.printUserInformation(getUserHelper());
                else
                    tui.printUnrecognizedCommand();
                break;
            case "a":
                if (userController.isSignedIn()) {
                    try {
                        String highscore = userController.getUserField(getUserHelper().brugernavn, getUserHelper().adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
                        tui.printUserHighScore(getUserHelper(), highscore);
                    } catch (UserControllerException e) {
                        e.printStackTrace();
                    }
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
            userController.signIn(username, password);
            tui.printMenu(getUserHelper(), isCLSOn);
            tui.printSignInSuccess();
        } catch (UserControllerException e) {
            tui.printMenu(getUserHelper(), isCLSOn);
            tui.printSignInFailure();
        }
    }

    @Override
    public void signOut() {
        clearScreenHelper();
        try {
            userController.signOut();
            tui.printMenu(getUserHelper(), isCLSOn);
            tui.printSignOutSuccess();
        } catch (UserControllerException e) {
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
            Bruger user;
            user = userController.getUser();
            return user;
        } catch (UserControllerException e) {
            return null;
        }
    }

    private void clearScreenHelper() {
        if (isCLSOn) tui.clearScreen();
    }

}