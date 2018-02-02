package io.inabsentia.hangman.ui;

import brugerautorisation.data.Bruger;
import io.inabsentia.hangman.controller.IUserController;
import io.inabsentia.hangman.controller.UserController;
import io.inabsentia.hangman.controller.UserControllerException;

import java.io.IOException;
import java.util.Scanner;

public class Tui implements ITui {

    private boolean isCLSOn = false;

    private final Scanner scanner;

    /* Singleton objects */
    private final IUserController userController = UserController.getInstance();

    /* ITui interface Singleton instance */
    private static ITui instance;

    /*
     * Static initialization block
     * for the Singleton instance.
     */
    static {
        try {
            instance = new Tui();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton instance!");
        }
    }

    /*
     * Private constructor for Singleton.
     * Initializes Scanner and starts the main TUI loop.
     */
    private Tui() {
        scanner = new Scanner(System.in);
        startLoop();
    }

    /*
     * Singleton instance getter.
     */
    public static synchronized ITui getInstance() {
        return instance;
    }

    /*
     * Main TUI loop.
     * Keeps waiting for the user to type in a command.
     */
    @Override
    public void startLoop() {
        clearScreen();
        printMenu();

        while (true) {
            String command = getCommand();

            clearScreen();
            printMenu();

            executeCommand(command);
        }
    }

    /*
     * Receive a command from the user.
     */
    @Override
    public String getCommand() {
        printArrow();
        String command = scanner.nextLine();
        return command;
    }

    /*
     * Execute the command, if it exists.
     * If it does not, print error message.
     */
    @Override
    public void executeCommand(String command) {
        switch (command) {
            case "q":
                signIn();
                break;
            case "w":
                /* This command only works if a user is signed in. */
                if (userController.isSignedIn())
                    signOut();
                else
                    printUnrecognizedCommand();
                break;
            case "e":
                /* This command only works if a user is signed in. */
                if (userController.isSignedIn())
                    printMessage("DEBUG: Play pressed!", true, false);
                else
                    printUnrecognizedCommand();
                break;
            case "r":
                isCLSOn = !isCLSOn;
                break;
            case "t":
                printAbout();
                break;
            case "x":
                exit();
                break;
            default:
                printUnrecognizedCommand();
                break;
        }

    }

    /*
     * Helper method that uses the UserController
     * to sign in a user.
     */
    @Override
    public void signIn() {
        StringBuilder sb = new StringBuilder();

        sb.append("/---------------------------\\\n");
        sb.append("|          Sign In          |\n");
        sb.append("|---------------------------|\n");
        sb.append("| Please provide username   |\n");
        sb.append("| and password below.       |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);

        printArrow("Username");
        String userName = scanner.nextLine();
        printArrow("Password");
        String password = scanner.nextLine();

        try {
            sb = new StringBuilder();

            userController.signIn(userName, password);

            clearScreen();
            printMenu();

            sb.append("/---------------------------\\\n");
            sb.append("|          Sign In          |\n");
            sb.append("|---------------------------|\n");
            sb.append("|  Successfully signed in!  |\n");
            sb.append("\\---------------------------/\n");
            printMessage(sb.toString(), true, false);
        } catch (UserControllerException e) {
            clearScreen();
            printMenu();

            sb = new StringBuilder();
            sb.append("/---------------------------\\\n");
            sb.append("|          Sign In          |\n");
            sb.append("|---------------------------|\n");
            sb.append("|  Authentication failed!   |\n");
            sb.append("\\---------------------------/\n");

            printMessage(sb.toString(), true, false);
        }
    }

    /*
     * Helper method that uses the UserController
     * to sign out a user.
     */
    @Override
    public void signOut() {
        StringBuilder sb = new StringBuilder();

        try {
            userController.signOut();

            clearScreen();
            printMenu();

            sb.append("/---------------------------\\\n");
            sb.append("|         Sign Out          |\n");
            sb.append("|---------------------------|\n");
            sb.append("| Successfully signed out!  |\n");
            sb.append("\\---------------------------/\n");
            printMessage(sb.toString(), true, false);
        } catch (UserControllerException e) {
            sb.append("/---------------------------\\\n");
            sb.append("|         Sign Out          |\n");
            sb.append("|---------------------------|\n");
            sb.append("|       Not signed in!      |\n");
            sb.append("\\---------------------------/\n");
            printMessage(sb.toString(), true, false);
        }
    }

    /*
     * Helper method that exits the program.
     */
    @Override
    public void exit() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|            Exit           |\n");
        sb.append("|---------------------------|\n");
        sb.append("|            Bye!           |\n");
        sb.append("\\---------------------------/");
        printMessage(sb.toString(), true, false);
        System.exit(0);
    }

    /*
     * Method that prints the menu screen.
     */
    @Override
    public void printMenu() {
        StringBuilder sb = new StringBuilder();

        String signedInMessage = "Not Signed In";

        try {
            Bruger user = userController.getUser();
            signedInMessage = user.fornavn + " " + user.efternavn;
        } catch (UserControllerException e) {

        }

        sb.append("\n/---------------------------\\\n");
        sb.append("|         Welcome to        |\n");
        sb.append("|    Distributed Hangman!   |\n");
        sb.append("|---------------------------|\n"); // 27 chars long without the two |

        int lineLength = 27;
        int userNameLength = signedInMessage.length();
        lineLength -= userNameLength;

        String msg = "|";

        for (int i = 0; i < lineLength; i++) {
            if (i == lineLength / 2)
                msg += signedInMessage;
            msg += " ";
        }
        msg += "|";

        sb.append(msg).append("\n");

        String isCLSOnStatus = "[OFF]";
        if (isCLSOn)
            isCLSOnStatus = "[ON]";

        sb.append("|---------------------------|\n");
        sb.append("| <Key>           Command   |\n");
        sb.append("|---------------------------|\n");
        sb.append("| <q>             Sign In   |\n");

        if (userController.isSignedIn()) {
            sb.append("| <w>             Sign Out  |\n");
            sb.append("| <e>             Play      |\n");
        }

        sb.append("| <r>             CLS " + isCLSOnStatus);
        if (!isCLSOn)
            sb.append(" |\n");
        else
            sb.append("  |\n");
        sb.append("| <t>             About     |\n");
        sb.append("| <x>             Exit      |\n");
        sb.append("\\---------------------------/\n");

        printMessage(sb.toString(), true, false);
    }

    /*
     * Method that prints the about screen.
     */
    @Override
    public void printAbout() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|           About           |\n");
        sb.append("|---------------------------|\n");
        sb.append("|          Made By          |\n");
        sb.append("|   s151641 Daniel Larsen   |\n");
        sb.append("|            DTU            |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method that is used to print any message to the command line/console.
     */
    @Override
    public void printMessage(String message, boolean newLine, boolean isRed) {
        if (newLine) {
            if (isRed)
                System.err.println(message);
            else
                System.out.println(message);
        } else {
            if (isRed)
                System.err.print(message);
            else
                System.out.print(message);
        }
    }

    /*
     * Method that clears the screen.
     * NOTICE: ONLY clears the screen if isCLSOn is true.
     */
    private void clearScreen() {
        try {
            if (isCLSOn)
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Method that prints the unrecognized command screen.
     */
    @Override
    public void printUnrecognizedCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|          Command          |\n");
        sb.append("|---------------------------|\n");
        sb.append("|  Command not recognized!  |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method that is used print the command line arrow
     * with a postfix. Used for username, password, etc.
     */
    private void printArrow(String postfix) {
        printMessage("[" + postfix + "] --> ", false, false);
    }

    /*
     * Method that is used print the command line arrow.
     */
    private void printArrow() {
        printMessage("--> ", false, false);
    }

}