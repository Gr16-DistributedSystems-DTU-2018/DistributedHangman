package io.inabsentia.distributedhangman.controller.interfaces;

import java.rmi.RemoteException;

public interface IGameController {
    void start() throws RemoteException;
}