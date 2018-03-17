package cli_java.ui;

import brugerautorisation.data.Bruger;
import server.logic.local.GameLogic;

import java.io.IOException;
import java.util.List;
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
     * Method to print log in prompt.
     */
    public final void printSignInPrompt() {
        String msg = "┌───────────────────────────┐\n" +
                "│           Log In          │\n" +
                "├───────────────────────────┤\n" +
                "│ Please provide username   │\n" +
                "│ and password below.       │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    /*
     * Method to print log in success.
     */
    public final void printLogInSuccess() {
        String msg = "┌───────────────────────────┐\n" +
                "│           Log In          │\n" +
                "├───────────────────────────┤\n" +
                "│  Successfully logged in!  │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    /*
     * Method to print log in failure.
     */
    public final void printLogInFailure() {
        String msg = "┌───────────────────────────┐\n" +
                "│           Log In          │\n" +
                "├───────────────────────────┤\n" +
                "│  Authentication failed!   │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    /*
     * Method to print sign out success.
     */
    public final void printSignOutSuccess() {
        String msg = "┌───────────────────────────┐\n" +
                "│          Log Out          │\n" +
                "├───────────────────────────┤\n" +
                "│ Successfully logged out!  │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);

    }

    /*
     * Method to print sign out failure.
     */
    public final void printSignOutFailure() {
        String msg = "┌───────────────────────────┐\n" +
                "│          Log Out          │\n" +
                "├───────────────────────────┤\n" +
                "│       Not logged in!      │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    /*
     * Method to print exit message.
     */
    public final void printExit() {
        String msg = "┌───────────────────────────┐\n" +
                "│            Exit           │\n" +
                "├───────────────────────────┤\n" +
                "│            Bye!           │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    /*
     * Method to print menu screen.
     */
    public final void printMenu(Bruger user) {
        StringBuilder sb = new StringBuilder();

        String loggedInMessage = "Not Logged In";

        if (user != null)
            loggedInMessage = user.fornavn + " " + user.efternavn;

        sb.append("\n┌───────────────────────────┐\n");
        sb.append("│         Welcome to        │\n");
        sb.append("│    Distributed Hangman!   │\n");
        sb.append("├───────────────────────────┤\n"); // 27 chars long without the two |

        String text = parseString(loggedInMessage, 27, true);
        sb.append(text).append("\n");

        sb.append("├───────────────────────────┤\n");
        sb.append("│ (Key)          Command    │\n");
        sb.append("├───────────────────────────┤\n");

        if (user == null)
            sb.append("│ (1)            Log In     │\n");

        if (user != null)
            sb.append("│ (2)            Log Out    │\n");

        sb.append("│                           │\n");

        if (user != null) {
            sb.append("│ (3)            Play       │\n");
            sb.append("│ (4)            Show Lobby │\n");
            sb.append("│                           │\n");
            sb.append("│ (5)            User (i)   │\n");
            sb.append("│ (6)            User Score │\n");
            sb.append("│                           │\n");
        }

        sb.append("│ (7)            About      │\n");
        sb.append("│ (0)            Exit       │\n");
        sb.append("└───────────────────────────┘\n");

        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print about screen.
     */
    public final void printAbout() {
        String msg = "┌───────────────────────────┐\n" +
                "│            About          │\n" +
                "├───────────────────────────┤\n" +
                "│        Made at DTU        │\n" +
                "│    Distributed Systems    │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
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
        String msg = "┌───────────────────────────┐\n" +
                "│          Command          │\n" +
                "├───────────────────────────┤\n" +
                "│  Command not recognized!  │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    /*
     * Method to print user information.
     */
    public final void printUserInformation(Bruger user) {
        if (user == null)
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────────────────────────┐\n");
        sb.append("│            User Information           │\n");
        sb.append("├───────────────────────────────────────┤\n");

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

        sb.append("│ CampusNet ID : ").append(campusnetId).append("\n");
        sb.append("│ Username     : ").append(userName).append("\n");
        sb.append("│ Password     : ").append(password).append("\n");
        sb.append("│                                       │\n");
        sb.append("│ First Name   : ").append(firstName).append("\n");
        sb.append("│ Last Name    : ").append(lastName).append("\n");
        sb.append("│ E-mail       : ").append(email).append("\n");
        sb.append("│                                       │\n");
        sb.append("│ Study Field  : ").append(studyField).append("\n");
        sb.append("│ Last Active  : ").append(lastActive).append("\n");
        sb.append("└───────────────────────────────────────┘\n");
        printMessage(sb.toString(), true, false);
    }

    /*
     * Method to print user high score.
     */
    public final void printUserHighScore(Bruger user, String highScore) {
        if (user == null)
            return;

        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────────────┐\n");
        sb.append("│         High Score        │\n");
        sb.append("├───────────────────────────┤\n");

        String userText = user.fornavn + " " + user.efternavn;
        userText = parseString(userText, 27, true) + "\n";
        sb.append(userText);
        sb.append("├───────────────────────────┤\n");

        int lineLength = 14;
        String highscoreString = parseString(highScore, lineLength, false);

        sb.append("│ High Score: ").append(highscoreString).append("\n");
        sb.append("└───────────────────────────┘\n");
        printMessage(sb.toString(), true, false);
    }

    public final void printNewScore(String highScore, boolean isHighScore) {
        String newHighScore = "Score";

        if (isHighScore)
            newHighScore = "New High Score!";

        newHighScore = parseString(newHighScore, 27, true);

        String highScoreStr = "Score: " + highScore;
        highScoreStr = parseString(highScoreStr, 27, true);

        String msg = "┌───────────────────────────┐\n" +
                newHighScore + "\n" +
                "├───────────────────────────┤\n" +
                highScoreStr + "\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    /*
     * Method to print the command line arrow with a postfix.
     */
    public final void printArrow(String postfix) {
        printMessage("[" + postfix + "] » ", false, false);
    }

    /*
     * Method to print the command line arrow.
     */
    public final void printArrow() {
        printMessage("» ", false, false);
    }

    private String parseString(String text, int lineLength, boolean withStartPipe) {
        StringBuilder sb = new StringBuilder();

        if (text.length() > lineLength)
            return text.substring(0, lineLength);

        int textLength = text.length();
        lineLength -= textLength;

        if (withStartPipe)
            sb.append("│");

        for (int i = 0; i < lineLength; i++) {
            if (i == lineLength / 2)
                sb.append(text);
            sb.append(" ");
        }
        sb.append("│");

        return sb.toString();
    }

    public final void printHangman(String playerName, int lifeLeft, int score, String hiddenWord, String usedCharacters) {
        String[] hangmanBodyChars = {"○", "/", "|", "\\", "/", "\\"};
        String[] hangmanBody = new String[6];

        int maximumStringLength = 6;
        String playerNameStr = addLeadingSpacesToString(playerName, maximumStringLength);
        String lifeLeftStr = addLeadingSpacesToString(Integer.toString(lifeLeft), maximumStringLength);
        String scoreStr = addLeadingSpacesToString(Integer.toString(score), maximumStringLength);

        for (int i = 0; i < hangmanBody.length; i++)
            hangmanBody[i] = " ";

        for (int j = lifeLeft; j <= GameLogic.MAXIMUM_LIFE; j++) {
            hangmanBody[j] = hangmanBodyChars[j];
        }

        String hiddenWordStr = parseString(hiddenWord, 27, false);
        usedCharacters = "- " + usedCharacters + " -";
        String usedCharactersStr = parseString(usedCharacters, 27, false);

        String msg = "┌───────────────────────────┐\n" +
                "│    Distributed Hangman    │\n" +
                "├───────────────────────────┤\n" +
                "│                ┌──────┐   │\n" +
                "│ Name:  " + playerNameStr + "  │      │   |\n" +
                "│               │      " + hangmanBody[0] + "   │\n" +
                "│ Life:  " + lifeLeftStr + "  │     " + hangmanBody[1] + hangmanBody[2] + hangmanBody[3] + "  │\n" +
                "│ Score: " + scoreStr + "  │     " + hangmanBody[4] + " " + hangmanBody[5] + "  │\n" +
                "│                ┴          │\n" +
                "│                           │\n" +
                "│" + hiddenWordStr + "\n" +
                "│" + usedCharactersStr + "\n" +
                "│                           │\n" +
                "└───────────────────────────┘\n";

        printMessage(msg, true, false);
    }

    public final void printFirstGuessInfo() {
        String msg = "┌───────────────────────────┐\n" +
                "│            Play           │\n" +
                "├───────────────────────────┤\n" +
                "│     Go ahead and take     │\n" +
                "│     your first guess!     │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    public final void printCorrectGuess() {
        String msg = "┌───────────────────────────┐\n" +
                "│           Guess           │\n" +
                "├───────────────────────────┤\n" +
                "│    You guessed correct!   │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    public final void printWrongGuess() {
        String msg = "┌───────────────────────────┐\n" +
                "│           Guess           │\n" +
                "├───────────────────────────┤\n" +
                "│     You guessed wrong!    │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    public final void printWin(String secretWord) {
        StringBuilder sb = new StringBuilder();
        String secretWordStr = parseString(secretWord, 27, false);

        sb.append("┌───────────────────────────┐\n");
        sb.append("│       Yay, you won!       │\n");
        sb.append("├───────────────────────────┤\n");
        sb.append("│   You guessed the word!   │\n");
        sb.append("│                           │\n");
        sb.append("│       The word was:       │\n");
        sb.append("│").append(secretWordStr).append("\n");
        sb.append("└───────────────────────────┘\n");
        printMessage(sb.toString(), true, false);
    }

    public final void printLoss() {
        String msg = "┌───────────────────────────┐\n" +
                "│     Oh dear, you lost!    │\n" +
                "├───────────────────────────┤\n" +
                "│  Better luck next time!   │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    public final String getUserCommand() {
        printArrow();
        return scanner.nextLine();
    }

    public final String getUserCommand(String postfix) {
        printArrow(postfix);
        String command = scanner.nextLine();

        if (command.equals(""))
            return " ";

        return command;
    }

    public void printLobby(List<String> users, List<Integer> currentScores) {
        StringBuilder sb = new StringBuilder();

        sb.append("┌───────────────────────────┐\n");
        sb.append("│           Lobby           │\n");
        sb.append("├───────────────────────────┤\n");
        sb.append("│ Username       Live Score │\n"); // 27 i alt 27 - 11 = 16
        sb.append("├───────────────────────────┤\n");

        for (int i = 0; i < users.size(); i++) {
            sb.append("│ ").append(addLeadingSpacesToString(users.get(i), 8)).append("      ").append(addLeadingSpacesToString(String.valueOf(currentScores.get(i)), 10)).append("  │\n");
        }

        sb.append("└───────────────────────────┘\n");

        printMessage(sb.toString(), true, false);
    }

    public final void printAlreadyGuessed(Character guess) {
        String msg = "┌───────────────────────────┐\n" +
                "│           Oops!           │\n" +
                "├───────────────────────────┤\n" +
                "│ You've already guessed " + guess + "! │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    public final void printError() {
        String msg = "┌───────────────────────────┐\n" +
                "│          Oh oh!           │\n" +
                "├───────────────────────────┤\n" +
                "│   Something went wrong!   │\n" +
                "│      No need to panic.    │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
    }

    public void printPlayAgain() {
        String msg = "┌───────────────────────────┐\n" +
                "│        Play again?        │\n" +
                "├───────────────────────────┤\n" +
                "│ Feel like playing again?  │\n" +
                "└───────────────────────────┘\n";
        printMessage(msg, true, false);
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

}