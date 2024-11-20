package edu.wm.cs.cs301.f2024.wordle.model;

public class RuleBasic implements AcceptanceRule {
	 @Override
	    public boolean isAcceptableGuess(Model model) {
	        // Get the guess from the model
	        String guess = model.getGuess();

	        // Check if the guess has exactly 5 letters
	        return guess != null && guess.length() == 5;
	    }

}
