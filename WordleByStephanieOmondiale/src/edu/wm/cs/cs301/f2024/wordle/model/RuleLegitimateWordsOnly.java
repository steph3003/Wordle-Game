package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.List;

public class RuleLegitimateWordsOnly implements AcceptanceRule {
	
	
	// Wrapped component
    private final AcceptanceRule decoratedRule;

    // Constructor to initialize the decorated rule
    public RuleLegitimateWordsOnly(AcceptanceRule decoratedRule) {
        this.decoratedRule = decoratedRule;
    }
    
    /**
     * Method to clear a row in the grid if the guess is invalid
     *
     * @param grid The game grid (WordleResponse[][])
     * @param rowIndex The current row to be cleared
     */
    private void clearRow(WordleResponse[][] grid, int rowIndex) {
        if (grid != null && rowIndex >= 0 && rowIndex < grid.length) {
            for (int i = 0; i < grid[rowIndex].length; i++) {
                grid[rowIndex][i] = null; // Clear each cell in the row
            }
        }
    }

    @Override
    public boolean isAcceptableGuess(Model model) {
        // Check if the base rule is valid
        if (!decoratedRule.isAcceptableGuess(model)) {
            clearRow(model.getWordleGrid(), model.getCurrentRow()); // Clear the invalid guess
            System.out.println("Invalid guess. Please try again.");
            return false; // Fail if the base rule fails
        }

        // Validate that the guess is in the word list
        String currentGuess = model.getCurrentGuess();
        List<String> wordList = model.getWordList();

        if (wordList == null || !wordList.contains(currentGuess)) {
            clearRow(model.getWordleGrid(), model.getCurrentGuess()); // Clear the invalid guess
            System.out.println("Invalid guess. Guess is not in the word list.");
            return false;
        }

        return true; // Return true if the guess is valid
    }

    /**
     * Method to clear a row in the grid if the guess is invalid
     *
     * @param grid The game grid
     * @param rowIndex The current row to be cleared
     */
    private void clearRow(String[][] grid, int rowIndex) {
        if (grid != null && rowIndex >= 0 && rowIndex < grid.length) {
            for (int i = 0; i < grid[rowIndex].length; i++) {
                grid[rowIndex][i] = ""; // Clear each cell in the row
            }
        }
    }
}
