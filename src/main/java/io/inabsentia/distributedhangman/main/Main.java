package io.inabsentia.distributedhangman.main;

import io.inabsentia.distributedhangman.controller.ScreenController;
import io.inabsentia.distributedhangman.controller.interfaces.IScreenController;

public class Main {

    private static final IScreenController gameController = ScreenController.getInstance();

    public static void main(String[] args) {
        gameController.startLoop();
    }

}