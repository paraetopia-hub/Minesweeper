package com.losenlaces.minesweeper.model.component;

import com.losenlaces.minesweeper.model.IBoard;
import com.losenlaces.minesweeper.model.ICell;
import com.losenlaces.minesweeper.model.IGame;

import java.util.Arrays;

public class Board implements IBoard {

    private final IGame game;
    private final ICell[][] cells;
    private final int rows;
    private final int columns;
    private final int mines;

    public Board(IGame game, int rows, int columns, int mines) {
        this.game = game;
        this.rows = Math.clamp(rows, 2, 9);
        this.columns = Math.clamp(columns, 2, 9);
        this.cells = new ICell[rows][columns];
        this.mines = mines;
        initializeCells(mines);
    }

    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public ICell getCell(int row, int column) {
        if (row < 0 || row >= cells.length || column < 0 || column >= cells[0].length) throw new IndexOutOfBoundsException("Invalid cell coordinates");
        return cells[row][column];
    }

    @Override
    public ICell[][] getCells() {
        return cells;
    }

    @Override
    public int getMines() {
        return mines;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public void refill() {
        for (ICell[] cell : cells) Arrays.fill(cell, null);
        initializeCells(mines);
    }

    private void initializeCells(int mines) {
        // Place mines randomly
        int placedMines = 0;
        while (placedMines < mines) {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * columns);
            if (cells[row][col] == null) {
                cells[row][col] = new Cell(game, row, col, true);
                placedMines++;
            }
        }
        // Fill remaining cells
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (cells[row][col] == null) {
                    cells[row][col] = new Cell(game, row, col, false);
                    var cell = cells[row][col];
                    cell.getAdjacentMines();
                }
            }
        }
    }
}
