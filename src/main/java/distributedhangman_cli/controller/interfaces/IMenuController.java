package distributedhangman_cli.controller.interfaces;

import java.rmi.RemoteException;

public interface IMenuController {
    void start();
    void executeUserCommand(String command) throws RemoteException;
    void signIn();
    void signOut();
    void exit();
}