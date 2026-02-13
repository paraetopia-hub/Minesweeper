package com.losenlaces.minesweeper.controller;

import com.losenlaces.minesweeper.model.IGame;
import com.losenlaces.minesweeper.model.component.Game;
import com.losenlaces.minesweeper.view.ConsoleUI;

public class GameController {

    private IGame currentGame;
    private ConsoleUI consoleUI;

    public GameController() {
        this.currentGame = new Game(9, 9, 20);
        this.consoleUI = new ConsoleUI(currentGame);
    }

    public void start() {
        try {
            consoleUI.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
