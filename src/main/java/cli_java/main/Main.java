package cli_java.main;

import cli_java.controller.MenuController;
import cli_java.controller.interfaces.IMenuController;

public class Main {

    private static final IMenuController menuController = MenuController.getInstance();

    public static void main(String[] args) {
        menuController.start();
    }

}