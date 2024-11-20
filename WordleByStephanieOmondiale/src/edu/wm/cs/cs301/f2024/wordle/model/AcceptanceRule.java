package edu.wm.cs.cs301.f2024.wordle.model;

public interface AcceptanceRule {
	// Method to check if a guess is acceptable based on the provided model
	public boolean isAcceptableGuess(Model model);

}
