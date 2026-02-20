package com.losenlaces.minesweeper.model.component;

import com.losenlaces.minesweeper.model.IBoard;
import com.losenlaces.minesweeper.model.ICell;
import com.losenlaces.minesweeper.model.IGame;

public class Board implements IBoard {

    private final IGame game;
    private final ICell[][] cells;
    private final int rows;
    private final int columns;
    private final int mines;
    private boolean initialized;

    public Board(IGame game, int rows, int columns, int mines) {
        this.game = game;
        this.rows = Math.clamp(rows, 2, 9);
        this.columns = Math.clamp(columns, 2, 9);
        this.cells = new ICell[rows][columns];
        this.mines = mines;
        this.initialized = false;
        // Create empty cells initially
        createEmptyCells();
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
        initialized = false;
        createEmptyCells();
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public void initializeWithSafeZone(int safeRow, int safeCol) {
        if (initialized) return;
        placeMinesAvoidingSafeZone(safeRow, safeCol);
        calculateAllAdjacentMines();
        initialized = true;
    }
    
    private void createEmptyCells() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                cells[row][col] = new Cell(this, row, col, false);
            }
        }
    }

    private void placeMinesAvoidingSafeZone(int safeRow, int safeCol) {
        int placedMines = 0;
        while (placedMines < mines) {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * columns);
            
            // Check if this position is in the safe zone (clicked cell + neighbors)
            if (isInSafeZone(row, col, safeRow, safeCol)) continue;
            
            // Check if already a mine
            if (cells[row][col].isMine()) continue;
            
            // Replace cell with a mine
            cells[row][col] = new Cell(this, row, col, true);
            placedMines++;
        }
    }
    
    private boolean isInSafeZone(int row, int col, int safeRow, int safeCol) {
        return Math.abs(row - safeRow) <= 1 && Math.abs(col - safeCol) <= 1;
    }
    
    private void calculateAllAdjacentMines() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                ((Cell) cells[row][col]).calculateAdjacentMines();
            }
        }
    }
}
