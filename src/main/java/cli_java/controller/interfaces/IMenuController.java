package cli_java.controller.interfaces;

import java.rmi.RemoteException;

public interface IMenuController {
    void start();
    void executeUserCommand(String command) throws RemoteException;
    void logIn();
    void logOut();
    void exit();
}