package io.inabsentia.distributedhangman.ui;

import brugerautorisation.data.Bruger;
import io.inabsentia.distributedhangman.util.Utils;

import java.io.IOException;
import java.util.Scanner;

/*
 * NOTES: Most methods are public, so that the TUI user is only
 * able to call the methods, as well as final so they can not be
 * overridden. Interface is not used, since these should NEVER
 * change.
 * It should be used as a TUI API.
 */
public final class Tui {

    /* Static Singleton instance */
    private static Tui instance;

    private static Scanner scanner;

    /*
     * Static initialization block for the Singleton instance.
     */
    static {
        try {
            instance = new Tui();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton Tui instance!");
        }
    }

    /*
     * Private constructor for Singleton.
     */
    private Tui() {
        scanner = new Scanner(System.in);
    }

    /*
     * Singleton instance getter.
     */
    public static synchronized Tui getInstance() {
        return instance;
    }

    /*
     * Method to print sign in prompt.
     */
    public final void printSignInPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|          Sign In          |\n");
        sb.append("|---------------------------|\n");
        sb.append("| Please provide username   |\n");
        sb.append("| and password below.       |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print sign in success.
     */
    public final void printSignInSuccess() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|          Sign In          |\n");
        sb.append("|---------------------------|\n");
        sb.append("|  Successfully signed in!  |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print sign in failure.
     */
    public final void printSignInFailure() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|          Sign In          |\n");
        sb.append("|---------------------------|\n");
        sb.append("|  Authentication failed!   |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print sign out success.
     */
    public final void printSignOutSuccess() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|         Sign Out          |\n");
        sb.append("|---------------------------|\n");
        sb.append("| Successfully signed out!  |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);

    }

    /*
     * Method to print sign out failure.
     */
    public final void printSignOutFailure() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|         Sign Out          |\n");
        sb.append("|---------------------------|\n");
        sb.append("|       Not signed in!      |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print exit message.
     */
    public final void printExit() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|            Exit           |\n");
        sb.append("|---------------------------|\n");
        sb.append("|            Bye!           |\n");
        sb.append("\\---------------------------/");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print menu screen.
     */
    public final void printMenu(Bruger user, boolean isCLSOn) {
        StringBuilder sb = new StringBuilder();

        String signedInMessage = "Not Signed In";

        if (user != null)
            signedInMessage = user.fornavn + " " + user.efternavn;

        sb.append("\n/---------------------------\\\n");
        sb.append("|         Welcome to        |\n");
        sb.append("|    Distributed Hangman!   |\n");
        sb.append("|---------------------------|\n"); // 27 chars long without the two |

        String text = parseString(signedInMessage, 27, true);
        sb.append(text).append("\n");

        String isCLSOnStatus = "[OFF]";

        if (isCLSOn)
            isCLSOnStatus = "[ON]";

        sb.append("|---------------------------|\n");
        sb.append("| <Key>          Command    |\n");
        sb.append("|---------------------------|\n");
        sb.append("| <q>            Sign In    |\n");

        if (user != null) {
            sb.append("| <w>            Sign Out   |\n");
        }

        sb.append("|                           |\n");

        if (user != null) {
            sb.append("| <e>            Play       |\n");
            sb.append("|                           |\n");
            sb.append("| <r>            User (i)   |\n");
            sb.append("| <a>            User Score |\n");
            sb.append("|                           |\n");
        }

        sb.append("| <z>            CLS ").append(isCLSOnStatus);
        if (!isCLSOn)
            sb.append("  |\n");
        else
            sb.append("   |\n");

        sb.append("| <x>            About      |\n");
        sb.append("| <c>            Exit       |\n");
        sb.append("\\---------------------------/\n");

        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print about screen.
     */
    public final void printAbout() {
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
     * Method that clears the screen.
     * NOTICE: ONLY works if executed in Windows command prompt/CMD.
     */
    public final void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Method to print unrecognized command message.
     */
    public final void printUnrecognizedCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|          Command          |\n");
        sb.append("|---------------------------|\n");
        sb.append("|  Command not recognized!  |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print user information.
     */
    public final void printUserInformation(Bruger user) {
        if (user == null)
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------------------\\\n");
        sb.append("|            User Information           |\n");
        sb.append("|---------------------------------------|\n");

        int lineLength = 23;

        /* Parse CampusNet ID */
        String campusnetId = parseString(user.campusnetId, lineLength, false);

        /* Parse Username */
        String userName = parseString(user.brugernavn, lineLength, false);

        /* Parse Password */
        String password = parseString(user.adgangskode, lineLength, false);

        /* Parse E-mail */
        String email = parseString(user.email, lineLength, false);

        /* Parse First Name */
        String firstName = parseString(user.fornavn, lineLength, false);

        /* Parse Last Name */
        String lastName = parseString(user.efternavn, lineLength, false);

        /* Parse Study Field */
        String studyField = parseString(user.studeretning, lineLength, false);

        /* Parse Last Active */
        String lastActive = parseString(Long.toString(user.sidstAktiv), lineLength, false);

        sb.append("| CampusNet ID : ").append(campusnetId).append("\n");
        sb.append("| Username     : ").append(userName).append("\n");
        sb.append("| Password     : ").append(password).append("\n");
        sb.append("|                                       |\n");
        sb.append("| First Name   : ").append(firstName).append("\n");
        sb.append("| Last Name    : ").append(lastName).append("\n");
        sb.append("| E-mail       : ").append(email).append("\n");
        sb.append("|                                       |\n");
        sb.append("| Study Field  : ").append(studyField).append("\n");
        sb.append("| Last Active  : ").append(lastActive).append("\n");
        sb.append("\\---------------------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print user high score.
     */
    public final void printUserHighScore(Bruger user, String highscore) {
        if (user == null)
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|         High Score        |\n");
        sb.append("|---------------------------|\n");

        String userText = user.fornavn + " " + user.efternavn;
        userText = parseString(userText, 27, true) + "\n";
        sb.append(userText);
        sb.append("|---------------------------|\n");

        int lineLength = 14;
        String highscoreString = parseString(highscore, lineLength, false);

        sb.append("| High Score: ").append(highscoreString).append("\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print the command line arrow with a postfix.
     */
    public final void printArrow(String arrow, String postfix) {
        printMessage("[" + postfix + "] " + arrow + " ", false, false);
    }

    /*
     * Method to print the command line arrow.
     */
    public final void printArrow(String arrow) {
        printMessage(arrow + " ", false, false);
    }

    private String parseString(String text, int lineLength, boolean withStartPipe) {

        if (text.length() > lineLength)
            return text.substring(0, lineLength);

        int textLength = text.length();
        lineLength -= textLength;

        String msg = "";

        if (withStartPipe)
            msg = "|";

        for (int i = 0; i < lineLength; i++) {
            if (i == lineLength / 2)
                msg += text;
            msg += " ";
        }
        msg += "|";

        return msg;
    }

    public final void printHangman(String playerName, int elapsedSeconds, int lifeLeft, int score, String hiddenWord, String usedCharacters) {
        String[] hangmanBodyChars = {"0", "/", "|", "\\", "/", "\\"};
        String[] hangmanBody = new String[6];

        int maximumStringLength = 6;
        String playerNameStr = addLeadingSpacesToString(playerName, maximumStringLength);
        String elapsedSecondsStr = addLeadingSpacesToString(Integer.toString(elapsedSeconds), maximumStringLength);
        String lifeLeftStr = addLeadingSpacesToString(Integer.toString(lifeLeft), maximumStringLength);
        String scoreStr = addLeadingSpacesToString(Integer.toString(score), maximumStringLength);

        for (int i = 0; i < hangmanBody.length; i++)
            hangmanBody[i] = " ";

        for (int j = 0; j < lifeLeft; j++) {
            if (j > Utils.MAXIMUM_LIFE - 1)
                break;
            hangmanBody[j] = hangmanBodyChars[j];
        }

        String hiddenWordStr = parseString(hiddenWord, 27, false);
        String usedCharactersStr = parseString(usedCharacters, 27, false);

        StringBuilder sb = new StringBuilder();
        /*
        sb.append("/---------------------------\\\n");
        sb.append("|    Distributed Hangman    |\n");
        sb.append("|---------------------------|\n");
        sb.append("|                 ______    |\n");
        sb.append("| Time:  ").append(elapsedSecondsStr).append("   |      |   |\n");
        sb.append("| Life:  ").append(lifeLeftStr).append("   |      ").append(hangmanBody[0]).append("   |\n");
        sb.append("| Score: " + scoreStr + "   |     " + hangmanBody[1] + hangmanBody[2] + hangmanBody[3] + "  |\n");
        sb.append("|                |     ").append(hangmanBody[4]).append(" ").append(hangmanBody[5]).append("  |\n");
        sb.append("|                |          |\n");
        sb.append("|                           |\n");
        sb.append("|").append(hiddenWordStr).append("\n");
        sb.append("|").append(usedCharactersStr).append("\n");
        sb.append("|                           |\n");
        sb.append("\\---------------------------/\n");
        */

        sb.append("/---------------------------\\\n");
        sb.append("|    Distributed Hangman    |\n");
        sb.append("|---------------------------|\n");
        sb.append("|                 ______    |\n");
        sb.append("| Name:  ").append(playerNameStr).append("  |      |   |\n");
        sb.append("| Time:  ").append(elapsedSecondsStr).append("  |      ").append(hangmanBody[0]).append("   |\n");
        sb.append("| Life:  ").append(lifeLeftStr).append("  |     ").append(hangmanBody[1]).append(hangmanBody[2]).append(hangmanBody[3]).append("  |\n");
        sb.append("| Score: ").append(scoreStr).append("  |     ").append(hangmanBody[4]).append(" ").append(hangmanBody[5]).append("  |\n");
        sb.append("|                |          |\n");
        sb.append("|                           |\n");
        sb.append("|").append(hiddenWordStr).append("\n");
        sb.append("|").append(usedCharactersStr).append("\n");
        sb.append("|                           |\n");
        sb.append("\\---------------------------/\n");

        printMessage(sb.toString(), true, false);
    }

    public final void printFirstGuessInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|            Play           |\n");
        sb.append("|---------------------------|\n");
        sb.append("|     Go ahead and take     |\n");
        sb.append("|     your first guess!     |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    public final void printCorrectGuess() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|           Guess           |\n");
        sb.append("|---------------------------|\n");
        sb.append("|    You guessed correct!   |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    public final void printWrongGuess() {
        StringBuilder sb = new StringBuilder();
        sb.append("/---------------------------\\\n");
        sb.append("|           Guess           |\n");
        sb.append("|---------------------------|\n");
        sb.append("|     You guessed wrong!    |\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    public final void printWin(String secretWord) {
        StringBuilder sb = new StringBuilder();

        String secretWordStr = parseString(secretWord, 27, false);

        sb.append("/---------------------------\\\n");
        sb.append("|         You won!          |\n");
        sb.append("|---------------------------|\n");
        sb.append("|   You guessed the word!   |\n");
        sb.append("|       The word was:       |\n");
        sb.append("|").append(secretWordStr).append("\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    public final void printLoss(String secretWord) {
        StringBuilder sb = new StringBuilder();

        String secretWordStr = parseString(secretWord, 27, false);

        sb.append("/---------------------------\\\n");
        sb.append("|         You lost...       |\n");
        sb.append("|---------------------------|\n");
        sb.append("|       The word was:       |\n");
        sb.append("|").append(secretWordStr).append("\n");
        sb.append("\\---------------------------/\n");
        printMessage(sb.toString(), true, false);
    }

    private String addLeadingSpacesToString(String text, int maximumSize) {
        if (text.length() > maximumSize)
            return text.substring(0, maximumSize);

        if (text.length() == maximumSize)
            return text;

        StringBuilder sb = new StringBuilder();

        int amountOfSpaces = maximumSize - text.length();

        for (int i = 0; i < amountOfSpaces; i++)
            sb.append(" ");

        sb.append(text);

        return sb.toString();
    }

    /*
     * Internal method to print messages to the screen.
     */
    private void printMessage(String message, boolean newLine, boolean isRed) {
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

    public String getUserCommand(String arrow) {
        printArrow(arrow);
        String command = scanner.nextLine().toLowerCase();
        return command;
    }

    public String getUserCommand(String arrow, String postfix) {
        printArrow(arrow, postfix);
        String command = scanner.nextLine().toLowerCase();

        if (command.equals("") || command.equals(" "))
            return " ";

        return command;
    }

}