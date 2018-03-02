package distributedhangman_cli.controller.interfaces;

import java.rmi.RemoteException;

public interface IGameController {
    void start() throws RemoteException;
}