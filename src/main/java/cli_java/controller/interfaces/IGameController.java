package cli_java.controller.interfaces;

import server.logic.rmi.IGameLobby;
import server.logic.rmi.IGameLogic;

import java.rmi.RemoteException;

public interface IGameController {
    void start(IGameLobby lobby, IGameLogic logic) throws RemoteException;
}