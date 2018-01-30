package io.inabsentia.hangman.ui;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Tui implements ITui {

    private static ITui instance;

    private boolean isCmd = false;
    private final String RMI_URL = "rmi://javabog.dk/brugeradmin";

    private final Scanner scanner;
    private Brugeradmin brugerAdmin;
    private Bruger nuvaerendeBruger;

    static {
        try {
            instance = new Tui();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton instance!");
        }
    }

    private Tui() {
        scanner = new Scanner(System.in);
        startLoop();
    }

    public static synchronized ITui getInstance() {
        return instance;
    }

    @Override
    public void startLoop() {
        clearScreen();
        printStartScreen();

        while (true) {

            String command = getCommand();

            clearScreen();
            printStartScreen();

            executeCommand(command);
        }
    }

    @Override
    public String getCommand() {
        printArrow();
        String command = scanner.nextLine();
        return command;
    }

    @Override
    public void executeCommand(String command) {
        switch (command) {
            case "q":
                signIn();
                break;
            case "w":
                signOut();
                break;
            case "e":
                break;
            case "r":
                isCmd = !isCmd;
                break;
            case "t":
                printAbout();
                break;
            case "x":
                exit();
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("\n/---------------------------\\\n");
                sb.append("|          Command          |\n");
                sb.append("|---------------------------|\n");
                sb.append("|  Command not recognized!  |\n");
                sb.append("\\---------------------------/\n");
                printMessage(sb.toString(), true, false);
                break;
        }

    }

    @Override
    public void signIn() {
        StringBuilder sb = new StringBuilder();

        if (brugerAdmin == null) {
            try {
                brugerAdmin = (Brugeradmin) Naming.lookup(RMI_URL);
            } catch (NotBoundException | RemoteException | MalformedURLException e) {
                e.printStackTrace();
            }
        }

        sb.append("\n/---------------------------\\\n");
        sb.append("|          Sign In          |\n");
        sb.append("|---------------------------|\n");
        sb.append("| Please provide username   |\n");
        sb.append("| and password below.       |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);

        while (true) {
            printArrow("Username");
            String userName = scanner.nextLine();
            printArrow("Password");
            String password = scanner.nextLine();

            try {
                sb = new StringBuilder();
                nuvaerendeBruger = brugerAdmin.hentBruger(userName, password);

                clearScreen();
                printStartScreen();

                sb.append("\n/---------------------------\\\n");
                sb.append("|          Sign In          |\n");
                sb.append("|---------------------------|\n");
                sb.append("|  Successfully signed in!  |\n");
                sb.append("\\---------------------------/\n");
                printMessage(sb.toString(), true, false);
                break;
            } catch (Exception e) {
                clearScreen();
                printStartScreen();

                sb = new StringBuilder();
                sb.append("\n/---------------------------\\\n");
                sb.append("|          Sign In          |\n");
                sb.append("|---------------------------|\n");
                sb.append("|  Authentication failed!   |\n");
                sb.append("\\---------------------------/\n");

                printMessage(sb.toString(), true, false);
            }
        }

    }

    @Override
    public void signOut() {
        StringBuilder sb = new StringBuilder();

        if (nuvaerendeBruger == null) {
            sb.append("\n/---------------------------\\\n");
            sb.append("|         Sign Out          |\n");
            sb.append("|---------------------------|\n");
            sb.append("|       Not signed in!      |\n");
            sb.append("\\---------------------------/\n");
            printMessage(sb.toString(), true, false);
            return;
        }

        nuvaerendeBruger = null;

        clearScreen();
        printStartScreen();

        sb.append("\n/---------------------------\\\n");
        sb.append("|         Sign Out          |\n");
        sb.append("|---------------------------|\n");
        sb.append("| Successfully signed out!  |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    @Override
    public void exit() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n/---------------------------\\\n");
        sb.append("|            Exit           |\n");
        sb.append("|---------------------------|\n");
        sb.append("|            Bye!           |\n");
        sb.append("\\---------------------------/");
        printMessage(sb.toString(), true, false);
        System.exit(0);
    }

    @Override
    public void printStartScreen() {
        StringBuilder sb = new StringBuilder();

        String signedInMessage = "Not Signed In";

        if (nuvaerendeBruger != null)
            signedInMessage = nuvaerendeBruger.fornavn + " " + nuvaerendeBruger.efternavn;

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

        String isCmdOn = "[OFF]";
        if (isCmd)
            isCmdOn = "[ON]";

        sb.append("|---------------------------|\n");
        sb.append("| <Key>           Command   |\n");
        sb.append("|---------------------------|\n");
        sb.append("| <q>             Sign In   |\n");
        sb.append("| <w>             Sign Out  |\n");
        sb.append("| <e>             Play      |\n");
        sb.append("| <r>             CMD " + isCmdOn);
        if (!isCmd)
            sb.append(" |\n");
        else
            sb.append("  |\n");
        sb.append("| <t>             About     |\n");
        sb.append("| <x>             Exit      |\n");
        sb.append("\\---------------------------/");

        printMessage(sb.toString(), true, false);
    }

    @Override
    public void printAbout() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n/---------------------------\\\n");
        sb.append("|           About           |\n");
        sb.append("|---------------------------|\n");
        sb.append("|          Made By          |\n");
        sb.append("|   s151641 Daniel Larsen   |\n");
        sb.append("|            DTU            |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

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

    private void clearScreen() {
        try {
            if (isCmd)
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void printArrow(String postfix) {
        printMessage("[" + postfix + "] --> ", false, false);
    }

    private void printArrow() {
        printMessage("--> ", false, false);
    }

}