package edu.wm.cs.cs301.f2024.wordle.model;

public class SwitchWhenWordListIsBelowThreshold implements SwitchStrategy {
	 private final int threshold; // The word list size threshold for switching

	    public SwitchWhenWordListIsBelowThreshold(int threshold) {
	        this.threshold = threshold;
	    }

	    // Method to determine if the strategy should switch
	    @Override
	    public boolean shouldSwitch(int currentRow, int wordListSize) {
	        // Switch if the remaining word list size is less than or equal to the threshold
	        return wordListSize <= threshold;
	    }
}
