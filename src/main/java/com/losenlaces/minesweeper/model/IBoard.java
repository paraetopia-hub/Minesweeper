package com.losenlaces.minesweeper.model;

public interface IBoard {
    IGame getGame();
    ICell getCell(int row, int column);
    ICell[][] getCells();
    int getMines();
    int getRows();
    int getColumns();

    void refill();
}
