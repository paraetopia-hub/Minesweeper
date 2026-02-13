package com.losenlaces.minesweeper.model.component;

import com.losenlaces.minesweeper.model.GameState;
import com.losenlaces.minesweeper.model.IBoard;
import com.losenlaces.minesweeper.model.ICell;
import com.losenlaces.minesweeper.model.IGame;

public class Game implements IGame {

    private final IBoard board;
    private GameState gameState;
    private long startTime;
    private long endTime;

    public Game(int rows, int columns, int mines) {
        this.board = new Board(this, rows, columns, mines);
        this.gameState = GameState.PLAYING;
    }

    @Override
    public IBoard getBoard() {
        return board;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void revealCell(int row, int column) {
        try {
            // Initialize board on first move
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
                // Initialize board with safe zone around first click
                if (board instanceof Board boardImpl) {
                    if (!boardImpl.isInitialized()) {
                        boardImpl.initializeWithSafeZone(row, column);
                    }
                }
            }
            
            ICell cell = board.getCell(row, column);
            if (cell.isRevealed() || cell.isFlagged()) return;
            
            // Mark cell as revealed
            revealCellOnly(cell);
            
            if (cell.isMine()) {
                gameState = GameState.LOST;
                endTime = System.currentTimeMillis();
                revealMines();
            } else {
                // If cell has 0 adjacent mines, reveal all neighbors recursively
                if (cell.getAdjacentMines() == 0) {
                    revealAdjacentCells(row, column);
                }
                if (checkWinCondition()) {
                    gameState = GameState.WON;
                    endTime = System.currentTimeMillis();
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // Handle invalid cell coordinates
        }
    }
    
    private void revealCellOnly(ICell cell) {
        // Reveal cell without triggering game logic
        if (!cell.isRevealed() && !cell.isFlagged()) {
            cell.reveal();
        }
    }
    
    private void revealAdjacentCells(int row, int column) {
        // Recursively reveal all adjacent cells
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                // Skip the center cell
                if (r == row && c == column) continue;
                
                try {
                    if (r >= 0 && r < board.getRows() && c >= 0 && c < board.getColumns()) {
                        ICell adjacentCell = board.getCell(r, c);
                        if (!adjacentCell.isRevealed() && !adjacentCell.isFlagged() && !adjacentCell.isMine()) {
                            revealCellOnly(adjacentCell);
                            // Continue flood fill if this cell also has 0 adjacent mines
                            if (adjacentCell.getAdjacentMines() == 0) {
                                revealAdjacentCells(r, c);
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    // Skip invalid coordinates
                }
            }
        }
    }

    @Override
    public void toggleFlag(int row, int column) {
        try {
            ICell cell = board.getCell(row, column);
            if (cell.isRevealed()) return;
            if (cell.isFlagged()) cell.unflag();
            else cell.flag();
        } catch (IndexOutOfBoundsException e) {
            // Handle invalid cell coordinates
        }
    }

    @Override
    public void revealMines() {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                ICell cell = board.getCell(row, col);
                if (cell.isMine() && !cell.isRevealed()) cell.reveal();
            }
        }
    }

    @Override
    public void restart() {
        board.refill();
        gameState = GameState.PLAYING;
        startTime = 0;
        endTime = 0;
    }

    private boolean checkWinCondition() {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                ICell cell = board.getCell(row, col);
                if (!cell.isMine() && !cell.isRevealed() && !cell.isFlagged()) return false;
            }
        }
        return true;
    }

    public long getElapsedTimeSeconds() {
        if (startTime == 0) return 0;
        
        long endTimeToUse = (gameState == GameState.PLAYING) 
            ? System.currentTimeMillis() 
            : endTime;
            
        return (endTimeToUse - startTime) / 1000;
    }
}
