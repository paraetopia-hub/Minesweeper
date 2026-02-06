package com.losenlaces.minesweeper.model;

public interface ICell {
    IGame getGame();
    int getRow();
    int getColumn();
    boolean isMine();
    boolean isRevealed();
    boolean isFlagged();
    int getAdjacentMines();

    void flag();
    void unflag();
    void reveal();
}
