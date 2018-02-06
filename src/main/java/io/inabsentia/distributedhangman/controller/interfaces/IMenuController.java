package io.inabsentia.distributedhangman.controller.interfaces;

public interface IMenuController {
    void start();
    void executeUserCommand(String command);
    void signIn();
    void signOut();
    void exit();
}