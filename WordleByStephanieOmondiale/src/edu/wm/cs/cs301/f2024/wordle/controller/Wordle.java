package edu.wm.cs.cs301.f2024.wordle.controller;
import edu.wm.cs.cs301.f2024.wordle.model.GameStrategy;
import edu.wm.cs.cs301.f2024.wordle.model.MixedModel;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import edu.wm.cs.cs301.f2024.wordle.model.AbsurdleModel;
import edu.wm.cs.cs301.f2024.wordle.model.AcceptanceRule;
import edu.wm.cs.cs301.f2024.wordle.model.Model;
import edu.wm.cs.cs301.f2024.wordle.model.RuleBasic;
import edu.wm.cs.cs301.f2024.wordle.model.RuleHard;
import edu.wm.cs.cs301.f2024.wordle.model.RuleLegitimateWordsOnly;
import edu.wm.cs.cs301.f2024.wordle.model.SwitchAfterNGuesses;
import edu.wm.cs.cs301.f2024.wordle.model.SwitchRandomly;
import edu.wm.cs.cs301.f2024.wordle.model.SwitchStrategy;
import edu.wm.cs.cs301.f2024.wordle.model.SwitchWhenWordListIsBelowThreshold;
import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;
import edu.wm.cs.cs301.f2024.wordle.view.WordleFrame;
import java.util.logging.Logger;


public class Wordle implements Runnable {
	
	private static final String GAME_OVER = "Game Over!";

	private static final String GUESS_ACCEPTED = "Guess accepted!";

	private static final String INVALID_GUESS_PLEASE_TRY_AGAIN = "Invalid guess. Please try again.";

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
		final Logger logger = Logger.getLogger(MixedModel.class.getName());
		// Default configurations
	    String strategy = "random";
	    boolean hardMode = false;
	    boolean onlyProperWords = false;

	    // Parse command-line arguments
	    for (int i = 0; i < args.length; i++) {
	        SwitchAfterNGuesses switchStrategy;
			switch (args[i]) {
	            case "-s":
	                if (i + 1 < args.length) {
	                    strategy = args[++i];
	                    if (!strategy.equals("random") && !strategy.equals("absurdle") && !strategy.equals("mixed")) {
	                        System.out.println("Invalid strategy. Use 'random' or 'absurdle' or 'mixed'.");
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
	            case "-ssn":
	                if (i + 1 < args.length) {
	                    int maxGuesses = Integer.parseInt(args[++i]);
	                    switchStrategy = new SwitchAfterNGuesses(maxGuesses);
	                    logger.info("Configured SwitchAfterNGuesses with max guesses: " + maxGuesses);
	                }
	                break;
	            case "-ssr":
	                switchStrategy = new SwitchRandomly(50); // Default 50% probability
	                logger.info("Configured SwitchRandomly with default 50% probability.");
	                break;
	            case "-sst":
	                if (i + 1 < args.length) {
	                    int threshold = Integer.parseInt(args[++i]);
	                    switchStrategy = new SwitchWhenWordListIsBelowThreshold(threshold);
	                    logger.info("Configured SwitchWhenWordListIsBelowThreshold with threshold: " + threshold);
	                }
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
	    }  if (strategy.equals("mixed")) {
	    	model = configureMixedStrategy(args); // Mixed strategy setup
	        if (model == null) return; // Exit if configuration failed
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

	private static Model configureMixedStrategy(String[] args) {
		SwitchStrategy switchStrategy = null;
	    GameStrategy absurdleModel = new AbsurdleModel(); // Replace with actual implementation
	    GameStrategy wordleModel = new WordleModel();     // Replace with actual implementation

	    // Parse additional parameters for the mixed strategy
	    for (int i = 2; i < args.length; i++) {
	        switch (args[i]) {
	            case "-ssn": // SwitchAfterNGuesses
	                if (i + 1 < args.length) {
	                    try {
	                        int maxGuesses = Integer.parseInt(args[++i]);
	                        switchStrategy = new SwitchAfterNGuesses(maxGuesses);
	                    } catch (NumberFormatException e) {
	                        System.out.println("Error: Invalid number for -ssn.");
	                        return null;
	                    }
	                } else {
	                    System.out.println("Error: -ssn requires a number (e.g., -ssn 3).");
	                    return null;
	                }
	                break;

	            case "-ssr": // SwitchRandomly
	                switchStrategy = new SwitchRandomly(50); // Default 50% chance
	                break;

	            case "-sst": // SwitchWhenWordListIsBelowThreshold
	                if (i + 1 < args.length) {
	                    try {
	                        int threshold = Integer.parseInt(args[++i]);
	                        switchStrategy = new SwitchWhenWordListIsBelowThreshold(threshold);
	                    } catch (NumberFormatException e) {
	                        System.out.println("Error: Invalid number for -sst.");
	                        return null;
	                    }
	                } else {
	                    System.out.println("Error: -sst requires a number (e.g., -sst 75).");
	                    return null;
	                }
	                break;

	            default:
	                System.out.println("Invalid parameter for mixed strategy: " + args[i]);
	                return null;
	        }
	    }

	    if (switchStrategy == null) {
	        System.out.println("Error: No valid switch strategy provided for mixed strategy.");
	        return null;
	    }

	    // Return a MixedModel with the specified strategies
	    return new MixedModel(absurdleModel, wordleModel, switchStrategy);
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
            System.out.println(INVALID_GUESS_PLEASE_TRY_AGAIN);

            return false; // Guess not accepted
        }

        System.out.println(GUESS_ACCEPTED);
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
        System.out.println(GAME_OVER);
    }

}

