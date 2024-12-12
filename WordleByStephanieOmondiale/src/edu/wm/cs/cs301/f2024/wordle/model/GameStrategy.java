package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.ArrayList;
import java.util.List;

public class GameStrategy {
	
	protected List<String> wordList = new ArrayList<>(); 

    public void processGuess(String guess, int currentRow, int wordListSize) {
        System.out.println("Processing guess in GameStrategy: " + guess);
        System.out.println("Current Row: " + currentRow);
        System.out.println("Remaining word list size: " + wordListSize);

        if (wordList.contains(guess)) {
            wordList.remove(guess);
        } else {
            System.out.println("Incorrect guess. Try again.");
        }
    }
}
