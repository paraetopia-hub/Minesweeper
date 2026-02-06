package com.losenlaces.minesweeper.model.component;

import com.losenlaces.minesweeper.model.ICell;
import com.losenlaces.minesweeper.model.IGame;

public class Cell implements ICell {

    private final IGame game;
    private final int row;
    private final int column;
    private final boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int adjacentMines;

    public Cell(IGame game, int row, int column, boolean isMine) {
        this.game = game;
        this.row = row;
        this.column = column;
        this.isMine = isMine;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
    }

    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public boolean isMine() {
        return isMine;
    }

    @Override
    public boolean isRevealed() {
        return isRevealed;
    }

    @Override
    public boolean isFlagged() {
        return isFlagged;
    }

    @Override
    public int getAdjacentMines() {
        if (isMine) return -1;
        if (adjacentMines == 0) {
            ICell[][] cells = game.getBoard().getCells();
            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = column - 1; c <= column + 1; c++) {
                    if (r >= 0 && r < cells.length && c >= 0 && c < cells[0].length) {
                        if (cells[r][c].isMine()) adjacentMines++;
                    }
                }
            }
        }
        return adjacentMines;
    }

    @Override
    public void flag() {
        this.isFlagged = true;
    }

    @Override
    public void unflag() {
        this.isFlagged = false;
    }

    @Override
    public void reveal() {
        if (isFlagged || isRevealed) return;
        this.isRevealed = true;
        game.revealCell(row, column);
    }
}
