package edu.wm.cs.cs301.f2024.wordle.model;

public class RuleBasic implements AcceptanceRule {
	 @Override
	    public boolean isAcceptableGuess(Model model) {
		 
	        // Get the current guess from the model
	        String currentGuess = model.getCurrentGuess();

	        // Check if the guess has exactly 5 letters
	        return currentGuess != null && currentGuess.length() == 5;
	    }

}
