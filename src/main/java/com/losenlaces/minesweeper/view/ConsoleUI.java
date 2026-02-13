package com.losenlaces.minesweeper.view;

import java.util.Scanner;

import com.losenlaces.minesweeper.model.GameState;
import com.losenlaces.minesweeper.model.IBoard;
import com.losenlaces.minesweeper.model.ICell;
import com.losenlaces.minesweeper.model.IGame;

public class ConsoleUI {
    private IGame game;
    private volatile boolean running;
    private volatile String statusMessage = "";

    public ConsoleUI(IGame game) {
        this.game = game;
        this.running = true;
    }

    public void start() {
        displayGame();
        Scanner scanner = new Scanner(System.in);

        // Display thread - updates every second
        
        while (running && game.getGameState() == GameState.PLAYING) {
            String input = scanner.nextLine();
            
            try {
                statusMessage = "";
                handleInput(input);
            } finally {

            }
        }

        // Stop display thread
        running = false;

        // Show final state
        displayGame();
        try {
            clearConsole();
            System.out.println("\n" + getGameEndMessage());
        } finally {

        }
        
        scanner.close();
    }

    private void displayGame() {
        System.out.println("╔════════════════════════════╗");
        System.out.println("║         MINESWEEPER        ║");
        System.out.println("╠════════════════════════════╣");
        System.out.println("╚════════════════════════════╝");
        System.out.println();
        
        printBoard(game.getBoard());
        
        // Show status message if any
        if (!statusMessage.isEmpty()) {
            System.out.println("\n⚠️  " + statusMessage);
        }
        
        System.out.println("\nCommands: r <row> <col> (reveal) | f <row> <col> (flag) | q (quit)");
        System.out.print("> ");
    }

    private void handleInput(String input) {
        String[] parts = input.trim().split("\\s+");
        
        if (parts.length == 0) return;
        
        if (parts[0].equalsIgnoreCase("q")) {
            running = false;
            return;
        }
        
        if (parts.length != 3) {
            statusMessage = "Invalid format. Use: r <row> <col> or f <row> <col>";
            return;
        }
        
        try {
            String command = parts[0].toLowerCase();
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            
            if (command.equals("r")) {
                game.revealCell(row, col);
            } else if (command.equals("f")) {
                game.toggleFlag(row, col);
            } else {
                statusMessage = "Unknown command: " + command;
                return;
            }
            
            if (game.getGameState() != GameState.PLAYING) {
                running = false;
            }

            displayGame();
        } catch (NumberFormatException e) {
            statusMessage = "Invalid coordinates. Must be numbers.";
        } catch (IndexOutOfBoundsException e) {
            statusMessage = "Coordinates out of bounds.";
        }
    }

    private void printBoard(IBoard board) {
        // Print column headers
        System.out.print("    ");
        for (int col = 0; col < board.getColumns(); col++) {
            System.out.printf("%2d ", col);
        }
        System.out.println();
        
        // Print rows with row numbers
        for (int row = 0; row < board.getRows(); row++) {
            System.out.printf("%2d  ", row);
            for (int col = 0; col < board.getColumns(); col++) {
                ICell cell = board.getCell(row, col);
                if (cell.isRevealed()) {
                    if (cell.isMine()) System.out.print(" ✵ ");
                    else System.out.print(" " + cell.getAdjacentMines() + " ");
                } else if (cell.isFlagged()) System.out.print(" ▷ ");
                else System.out.print(" ▢ ");
            }
            System.out.println();
        }
    }

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // ANSI escape codes for Unix/Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback: print newlines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private String getGameEndMessage() {
        switch (game.getGameState()) {
            case WON: return "🎉 Congratulations! You won! 🎉";
            case LOST: return "💥 Game Over! You hit a mine 💥!";
            default: return "Game ended.";
        }
    }
}