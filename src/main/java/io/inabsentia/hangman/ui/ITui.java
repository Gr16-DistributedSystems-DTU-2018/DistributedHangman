package io.inabsentia.hangman.ui;

public interface ITui {
    void startLoop();
    String getCommand();
    void executeCommand(String command);
    void signIn() throws Exception;
    void signOut() throws Exception;
    void exit();
    void printStartScreen();
    void printAbout();
    void printMessage(String message, boolean newLine, boolean isRed);
}