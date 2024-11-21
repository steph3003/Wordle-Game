package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.List;
import java.util.Map;

public class RuleHard implements AcceptanceRule {
	
	// Wrapped rule
	private final AcceptanceRule decoratedRule; 

    // Constructor
    public RuleHard(AcceptanceRule decoratedRule) {
        this.decoratedRule = decoratedRule;
    }

    @Override
    public boolean isAcceptableGuess(Model model) {
        // Check base rule
        if (!decoratedRule.isAcceptableGuess(model)) {
            return false;
        }

        // Retrieve current guess and feedback
        String currentGuess = model.getCurrentGuess();
        Map<Integer, Character> greenMatches = model.getGreenMatches();
        Map<Integer, Character> yellowMatches = model.getYellowMatches();
        List<Character> greyMatches = model.getGreyMatches();

        // Validate green matches (exact position match)
        for (Map.Entry<Integer, Character> entry : greenMatches.entrySet()) {
            int position = entry.getKey();
            char expectedChar = entry.getValue();

            if (currentGuess.charAt(position) != expectedChar) {
                return false; // Green match violation
            }
        }

        // Validate yellow matches (must contain the letter, but not in the same position)
        for (Map.Entry<Integer, Character> entry : yellowMatches.entrySet()) {
            char requiredChar = entry.getValue();

            if (!currentGuess.contains(String.valueOf(requiredChar)) || 
                currentGuess.charAt(entry.getKey()) == requiredChar) {
                return false; // Yellow match violation
            }
        }

        // Validate grey matches (must not contain grey letters)
        for (char greyChar : greyMatches) {
            if (currentGuess.contains(String.valueOf(greyChar))) {
                return false; // Grey match violation
            }
        }

        // All checks passed
        return true;
    }
}
