package com.losenlaces.minesweeper.model.component;

import com.losenlaces.minesweeper.model.GameState;
import com.losenlaces.minesweeper.model.IBoard;
import com.losenlaces.minesweeper.model.ICell;
import com.losenlaces.minesweeper.model.IGame;

public class Game implements IGame {

    private final IBoard board;
    private GameState gameState;

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
            ICell cell = board.getCell(row, column);
            if (cell.isRevealed() || cell.isFlagged()) return;
            cell.reveal();
            if (cell.isMine()) {
                gameState = GameState.LOST;
                revealMines();
            } else if (checkWinCondition()) gameState = GameState.WON;
        } catch (IndexOutOfBoundsException e) {
            // Handle invalid cell coordinates
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
}
