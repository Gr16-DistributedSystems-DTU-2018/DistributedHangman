package distributedhangman_cli.main;

import distributedhangman_cli.controller.MenuController;
import distributedhangman_cli.controller.interfaces.IMenuController;

public class Main {

    private static final IMenuController menuController = MenuController.getInstance();

    public static void main(String[] args) {
        menuController.start();
    }

}