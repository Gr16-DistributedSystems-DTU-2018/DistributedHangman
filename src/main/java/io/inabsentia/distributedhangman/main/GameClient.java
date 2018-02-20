package io.inabsentia.distributedhangman.main;

import io.inabsentia.distributedhangman.logic.IGameLogic;
import io.inabsentia.distributedhangman.util.Utils;

import java.rmi.Naming;

public final class GameClient {

    public static void main(String[] args) throws Exception {
        IGameLogic logic = (IGameLogic) Naming.lookup(Utils.RMI_STUB_URL_LOCAL_LOGIC);

        //UserController.getInstance().setUserField("s151641", "godkode", Utils.HIGH_SCORE_FIELD_KEY, "0");

        System.out.println(logic.getGameScore());

        logic.addGameScore(200);
        System.out.println(logic.getGameScore());

        logic.addGameScore(-23);
        System.out.println(logic.getGameScore());
    }

}