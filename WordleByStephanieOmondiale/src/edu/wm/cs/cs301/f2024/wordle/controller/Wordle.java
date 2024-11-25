package edu.wm.cs.cs301.f2024.wordle.controller;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import edu.wm.cs.cs301.f2024.wordle.model.AbsurdleModel;
import edu.wm.cs.cs301.f2024.wordle.model.AcceptanceRule;
import edu.wm.cs.cs301.f2024.wordle.model.Model;
import edu.wm.cs.cs301.f2024.wordle.model.RuleBasic;
import edu.wm.cs.cs301.f2024.wordle.model.RuleHard;
import edu.wm.cs.cs301.f2024.wordle.model.RuleLegitimateWordsOnly;
import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;
import edu.wm.cs.cs301.f2024.wordle.view.WordleFrame;

public class Wordle implements Runnable {
	
	//Rule for validating guesses
	private static AcceptanceRule rule;
	
	// The game grid
	private static String[][] grid; 
	
	// The game model
    private static Model model; 
    
    int currentRow;
    
    
    
    // Constructor
    public Wordle(AcceptanceRule rule, Model model, String[][] grid) {
        Wordle.rule = rule;
        Wordle.model = model;
        Wordle.grid = grid;
    }

	/**
	 * This method starts the program by initializing the game
	 * @param args Array of strings that holds any arguments passed to the program
	 */
	public static void main(String[] args) {
		// Default configurations
	    String strategy = "random";
	    boolean hardMode = false;
	    boolean onlyProperWords = false;

	    // Parse command-line arguments
	    for (int i = 0; i < args.length; i++) {
	        switch (args[i]) {
	            case "-s":
	                if (i + 1 < args.length) {
	                    strategy = args[++i];
	                    if (!strategy.equals("random") && !strategy.equals("absurdle")) {
	                        System.out.println("Invalid strategy. Use 'random' or 'absurdle'.");
	                        return;
	                    }
	                } else {
	                    System.out.println("Missing argument for -s.");
	                    return;
	                }
	                break;
	            case "-h":
	                hardMode = true;
	                break;
	            case "-wo":
	                onlyProperWords = true;
	                break;
	            default:
	                System.out.println("Invalid argument: " + args[i]);
	                return;
	        }
	    }

	    // Select the appropriate strategy
	    Model model;
	    if (strategy.equals("random")) {
	        model = new WordleModel(); // Random strategy
	    } else {
	        model = new AbsurdleModel(); // Absurdle strategy
	    }

	    // Set up the acceptance rule based on modes
	    AcceptanceRule rule = new RuleBasic();
	    if (hardMode) {
	        rule = new RuleHard(rule); // Wrap with hard mode
	    }
	    if (onlyProperWords) {
	        rule = new RuleLegitimateWordsOnly(rule); // Wrap with proper words rule
	    }

	    // Initialize the grid
	    String[][] grid = new String[6][5]; // Example: 6 rows, 5 columns

	    // Launch the game
	    SwingUtilities.invokeLater(new Wordle(rule, model, grid));
	}

	/**
	 * Starts up a new game
	 */
	@Override
	public void run() {
		new WordleFrame(new WordleModel());
	}
	
	// Method to validate and submit guesses
    public boolean validateAndSubmitGuess(int currentRow) {

        if (!rule.isAcceptableGuess(model)) {
            // Clear the row in the grid for the current guess
            clearRow(grid, currentRow);

            // Inform the user to re-enter their guess
            System.out.println("Invalid guess. Please try again.");

            return false; // Guess not accepted
        }

        System.out.println("Guess accepted!");
        return true; // Guess accepted
    }

    // Method to clear a row in the grid
    private void clearRow(String[][] grid, int rowIndex) {
        for (int i = 0; i < grid[rowIndex].length; i++) {
            grid[rowIndex][i] = ""; // Clear each cell
        }
    }
    
    public void playGame() {
    	//read user input
        Scanner scanner = new Scanner(System.in);
        
        //Ensures the game continues until the user has exhausted all attempts
		while (currentRow < grid.length) {
            System.out.print("Enter your guess: ");
            String guess = scanner.nextLine();
            model.setCurrentGuess(guess);

            if (validateAndSubmitGuess(currentRow)) {
                currentRow++; // Move to the next row if guess is valid
            }
        }
		scanner.close();
        System.out.println("Game Over!");
    }

}

