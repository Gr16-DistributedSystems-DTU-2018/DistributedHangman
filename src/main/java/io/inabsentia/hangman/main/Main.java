package io.inabsentia.hangman.main;

import io.inabsentia.hangman.ui.ITui;
import io.inabsentia.hangman.ui.Tui;

public class Main {

    public static void main(String[] args) {
        ITui tui = Tui.getInstance();
        tui.printStartScreen();

    }

}