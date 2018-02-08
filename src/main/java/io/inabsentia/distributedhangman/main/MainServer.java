package io.inabsentia.distributedhangman.main;

import io.inabsentia.distributedhangman.logic.GameLogic;
import io.inabsentia.distributedhangman.logic.IGameLogic;
import io.inabsentia.distributedhangman.util.Utils;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public final class MainServer {

    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(Integer.parseInt(String.valueOf(Utils.REMOTE_PORT)));
        System.setProperty("java.rmi.server.hostname", "ubuntu4.javabog.dk");

        IGameLogic logic = new GameLogic();
        Naming.rebind(Utils.RMI_STUB_URL_REMOTE_LOGIC_JAVABOG, logic);

        System.out.println("GameLogic registered at: " + Utils.RMI_STUB_URL_REMOTE_LOGIC_JAVABOG);
    }

}