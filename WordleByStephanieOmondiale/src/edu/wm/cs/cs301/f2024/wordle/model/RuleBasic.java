package edu.wm.cs.cs301.f2024.wordle.model;

public class RuleBasic implements AcceptanceRule {
	 @Override
	    public boolean isAcceptableGuess(Model model) {
		 
		 String[][] grid = new String[6][5]; // Initialize grid
		 int currentRow = 0; // Track the current row

		// Get the current guess from the model
	        String currentGuess = model.getCurrentGuess();

	        // Check if the guess has exactly 5 letters
	        if (currentGuess == null || currentGuess.length() != 5) {
				clearRow(grid, currentRow); // Clear the row in the grid
	            System.out.println("Invalid guess. Guess must be exactly 5 letters.");
	            return false;
	        }

	        return true; // Guess is valid
	    }

	    // Method to clear a row in the grid
	    public void clearRow(String[][] grid, int rowIndex) {
	        for (int i = 0; i < grid[rowIndex].length; i++) {
	            grid[rowIndex][i] = ""; // Clear each cell in the row
	        }
	    }

}
