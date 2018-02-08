package io.inabsentia.distributedhangman.main;

import io.inabsentia.distributedhangman.logic.GameLogic;
import io.inabsentia.distributedhangman.logic.IGameLogic;
import io.inabsentia.distributedhangman.util.Utils;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public final class MainServer {

    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(1099);

        IGameLogic logic = new GameLogic();
        System.setProperty("java.rmi.server.hostname", "javabog.dk");
        Naming.rebind(Utils.RMI_STUB_URL_LOCAL_LOGIC, logic);

        System.out.println("GameLogic registered at: " + Utils.RMI_STUB_URL_LOCAL_LOGIC);
    }

}