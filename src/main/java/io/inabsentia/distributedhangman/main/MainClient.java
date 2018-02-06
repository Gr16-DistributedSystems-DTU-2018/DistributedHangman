package io.inabsentia.distributedhangman.main;

import io.inabsentia.distributedhangman.logic.IGameLogic;
import io.inabsentia.distributedhangman.util.Utils;

import java.rmi.Naming;

public final class MainClient {

    public static void main(String[] args) throws Exception {
        IGameLogic logic = (IGameLogic) Naming.lookup(Utils.RMI_STUB_URL_LOGIC);

        System.out.println(logic.getScore());

        logic.addScore(200);
        System.out.println(logic.getScore());

        logic.addScore(-23);
        System.out.println(logic.getScore());
    }

}