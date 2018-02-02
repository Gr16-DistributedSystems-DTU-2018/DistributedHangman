package io.inabsentia.distributedhangman.controller.interfaces;

public interface IScreenController {
    void startLoop();
    String getUserCommand();
    void executeUserCommand(String command);
    void signIn();
    void signOut();
    void exit();
}