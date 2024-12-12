package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.logging.Logger;


public class SwitchWhenWordListIsBelowThreshold implements SwitchStrategy {
	 private final int threshold; // The word list size threshold for switching
	 final Logger logger = Logger.getLogger(SwitchWhenWordListIsBelowThreshold.class.getName());

	    public SwitchWhenWordListIsBelowThreshold(int threshold) {

	        this.threshold = threshold;
	    }

	    // Method to determine if the strategy should switch
	    @Override
	    public boolean shouldSwitch(int currentRow, int wordListSize) {
	    	// Log the current word list size and threshold
	        logger.info("Current word list size: " + wordListSize + ", Threshold: " + threshold);

	        // Determine if switching should occur
	        boolean shouldSwitch = wordListSize <= threshold;

	        // Log the decision
	        logger.info("Decision to switch: " + (shouldSwitch ? "Yes" : "No"));

	        return shouldSwitch;
	    
	    }
}
