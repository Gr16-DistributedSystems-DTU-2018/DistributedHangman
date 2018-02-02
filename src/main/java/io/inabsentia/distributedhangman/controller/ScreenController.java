package io.inabsentia.distributedhangman.controller;

import brugerautorisation.data.Bruger;
import io.inabsentia.distributedhangman.controller.exceptions.UserControllerException;
import io.inabsentia.distributedhangman.controller.interfaces.IScreenController;
import io.inabsentia.distributedhangman.controller.interfaces.IUserController;
import io.inabsentia.distributedhangman.ui.Tui;
import io.inabsentia.distributedhangman.util.Utils;

import java.util.Scanner;

public final class ScreenController implements IScreenController {

    /* Fields */
    private final Scanner scanner;
    private boolean isCLSOn = true;

    /* Singleton Objects */
    private final Tui tui = Tui.getInstance();
    private final IUserController userController = UserController.getInstance();

    /* Static Singleton instance */
    private static IScreenController instance;

    /*
     * Static initialization block for the Singleton instance.
     */
    static {
        try {
            instance = new ScreenController();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton ScreenController instance!");
        }
    }

    /*
     * Private constructor for Singleton.
     */
    private ScreenController() {
        scanner = new Scanner(System.in);
    }

    /*
     * Singleton instance getter.
     */
    public static synchronized IScreenController getInstance() {
        return instance;
    }

    @Override
    public void startLoop() {
        clearScreenHelper();

        tui.printMenu(getUserHelper(), isCLSOn);

        while (true) {
            String command = getUserCommand();

            clearScreenHelper();
            tui.printMenu(getUserHelper(), isCLSOn);

            executeUserCommand(command);
        }

    }

    @Override
    public String getUserCommand() {
        tui.printArrow(Utils.CMD_ARROW);
        String command = scanner.nextLine();
        return command;
    }

    @Override
    public void executeUserCommand(String command) {
        switch (command) {
            case "q":
                tui.printSignInPrompt();
                signIn();
                break;
            case "w":
                signOut();
                break;
            case "e":
                break;
            case "r":
                tui.printUserInformation(getUserHelper());
                break;
            case "a":
                try {
                    String highscore = userController.getUserField(getUserHelper().brugernavn, getUserHelper().adgangskode, Utils.HIGH_SCORE_FIELD_KEY);
                    tui.printUserHighScore(getUserHelper(), highscore);
                } catch (UserControllerException e) {
                    e.printStackTrace();
                }
                break;
            case "s":
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
        tui.printArrow(Utils.CMD_ARROW, "Username");
        String userName = scanner.nextLine();

        tui.printArrow(Utils.CMD_ARROW, "Password");
        String password = scanner.nextLine();

        clearScreenHelper();

        try {
            userController.signIn(userName, password);
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