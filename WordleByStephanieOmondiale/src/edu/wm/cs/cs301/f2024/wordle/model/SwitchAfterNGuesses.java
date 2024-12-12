package edu.wm.cs.cs301.f2024.wordle.model;

public class SwitchAfterNGuesses implements SwitchStrategy {
	private final int maxGuesses;

    public SwitchAfterNGuesses(int maxGuesses) {
        this.maxGuesses = maxGuesses;
    }

    @Override
 // Switch if the current number of guesses has reached or exceeded maxGuesses
    public boolean shouldSwitch(int currentRow, int wordListSize) {
        return currentRow >= maxGuesses;
    }
}
