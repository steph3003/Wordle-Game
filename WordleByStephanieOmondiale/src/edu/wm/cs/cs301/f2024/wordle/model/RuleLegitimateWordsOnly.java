package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.List;

public class RuleLegitimateWordsOnly implements AcceptanceRule {
	
	// Wrapped component
	private final AcceptanceRule decoratedRule; 

    // Constructor to initialize the decorated rule
    public RuleLegitimateWordsOnly(AcceptanceRule decoratedRule) {
        this.decoratedRule = decoratedRule;
    }

    @Override
    public boolean isAcceptableGuess(Model model) {
        //Check if the base rule is valid
        if (!decoratedRule.isAcceptableGuess(model)) {
            return false; // Fail if the base rule fails
        }

        //Validate that the guess is in the word list
        String currentGuess = model.getCurrentGuess();
        List<String> wordList = model.getWordList();

        // Return true only if the guess is in the word list
        return wordList != null && wordList.contains(currentGuess);
    }
}
