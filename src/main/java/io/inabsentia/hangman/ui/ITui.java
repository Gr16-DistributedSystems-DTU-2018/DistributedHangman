package io.inabsentia.hangman.ui;

public interface ITui {
    void startLoop();
    String getCommand();
    void executeCommand(String command);
    void signIn();
    void signOut();
    void exit();
    void printMenu();
    void printAbout();
    void printUnrecognizedCommand();
    void printMessage(String message, boolean newLine, boolean isRed);
}