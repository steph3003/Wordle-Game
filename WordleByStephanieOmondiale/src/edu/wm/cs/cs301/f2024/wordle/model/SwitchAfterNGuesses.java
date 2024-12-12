package edu.wm.cs.cs301.f2024.wordle.model;
import java.util.logging.Logger;


public class SwitchAfterNGuesses implements SwitchStrategy {
    private static final Logger logger = Logger.getLogger(SwitchAfterNGuesses.class.getName());

	private final int maxGuesses;

    public SwitchAfterNGuesses(int maxGuesses) {
        this.maxGuesses = maxGuesses;
    }

    @Override
 // Switch if the current number of guesses has reached or exceeded maxGuesses
    public boolean shouldSwitch(int currentRow, int wordListSize) {
    	
    	// Log the current row and max guesses
        logger.info("Current row: " + currentRow + ", Max guesses: " + maxGuesses);
        
        boolean shouldSwitch = currentRow >= maxGuesses;
        
     // Log the decision
        logger.info("Decision to switch: " + (shouldSwitch ? "Yes" : "No"));
        
        return shouldSwitch;
    }
}
