package com.losenlaces.minesweeper.model;

public interface IGame {
    IBoard getBoard();
    GameState getGameState();

    void revealCell(int row, int column);
    void toggleFlag(int row, int column);
    void revealMines();
    void restart();
}

