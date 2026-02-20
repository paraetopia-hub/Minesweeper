package com.losenlaces.minesweeper.model.component;

import com.losenlaces.minesweeper.model.IBoard;
import com.losenlaces.minesweeper.model.ICell;

public class Cell implements ICell {

    private final IBoard board;
    private final int row;
    private final int column;
    private final boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int adjacentMines;
    private boolean adjacentMinesCalculated;

    public Cell(IBoard board, int row, int column, boolean isMine) {
        this.board = board;
        this.row = row;
        this.column = column;
        this.isMine = isMine;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
        this.adjacentMinesCalculated = false;
    }

    @Override
    public IBoard getBoard() {
        return board;
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
        return adjacentMines;
    }

    public void calculateAdjacentMines() {
        if (adjacentMinesCalculated || isMine) return;
        adjacentMines = 0;
        ICell[][] cells = board.getCells();
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                if (r >= 0 && r < board.getCells().length && c >= 0 && c < cells[0].length) {
                    if (r != row || c != column) if (cells[r][c].isMine()) adjacentMines++;
                }
            }
        }
        adjacentMinesCalculated = true;
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
    }
}
