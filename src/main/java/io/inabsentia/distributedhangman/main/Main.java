package io.inabsentia.distributedhangman.main;

import io.inabsentia.distributedhangman.controller.MenuController;
import io.inabsentia.distributedhangman.controller.interfaces.IMenuController;

public class Main {

    private static final IMenuController menuController = MenuController.getInstance();

    public static void main(String[] args) {
        menuController.start();
    }

}