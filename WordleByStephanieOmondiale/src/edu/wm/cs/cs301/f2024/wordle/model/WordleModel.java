package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import edu.wm.cs.cs301.f2024.wordle.controller.ReadWordsRunnable;


//import java.util.HashMap;
//import java.util.Map;

public class WordleModel{
	// Current word to be processed, stored as an array of characters
    private char[] currentWord, guess;

    // Number of columns (letters per word) and max number of rows (guesses allowed)
    // Fixed values: 5 columns (5 letters per word) and 6 rows (6 attempts allowed)
    private final int columnCount = 5;
    private final int maximumRows = 6;

    // Index of current column and row
    // Initialized to -1 for column and 0 for row to indicate starting positions
    private int currentColumn, currentRow;

    // List of words that the player can guess from, populated from an external source
    private List<String> wordList;

    // Random object to select a random word from the word list
    private final Random random = new Random();

    // Statistics object to track game statistics, such as streak and total games played
    private final Statistics statistics = new Statistics();

    // Grid representing the game board, used to store feedback for each guessed letter
    private WordleResponse[][] wordleGrid;

    public boolean wordListLoaded = false; // Flag to check if word list is loaded
    private CompletableFuture<Void> wordListFuture;

    // Checks if the word list has finished loading
    public boolean isWordListLoaded() {
        return wordListLoaded;
    }
    
 // Tracks the color state of each letter for the visual keyboard
    private Map<Character, Color> letterState = new HashMap<>();

    // Constructor: Initializes the game model by setting up the grid, loading word list, and preparing stats
    public WordleModel() {
        this.currentColumn = -1;
        this.currentRow = 0;
        createWordList();
        this.wordleGrid = initializeWordleGrid();
        this.guess = new char[columnCount];
    }

    // Loads the word list asynchronously by starting a background thread
    public void createWordList() {
        Thread backgroundThread = new Thread(() -> wordListLoaded = true);
        backgroundThread.start();
    }

    // Initializes the grid and generates a new current word for the player to guess
    public void initialize() {
        this.wordleGrid = initializeWordleGrid();
        this.currentColumn = -1;
        this.currentRow = 0;
        generateCurrentWord();
        this.guess = new char[columnCount];
    }

    // Randomly selects a word from the word list and stores it as the current word to guess
    public void generateCurrentWord() {
        if (!wordListLoaded) {
            try {
                wordListFuture.join(); // Wait for word list to load before proceeding
                wordListLoaded = true;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (wordList != null && !wordList.isEmpty()) {
            String word = getCurrentWord();
            this.currentWord = word.toUpperCase().toCharArray();
        } else {
            System.err.println("Error: Word list is empty or failed to load.");
        }
    }

    // Returns a random word from the word list
    public String getCurrentWord() {
        return wordList.get(getRandomIndex());
    }

    // Generates a random index to select a word from the word list
    private int getRandomIndex() {
        int size = wordList.size();
        return random.nextInt(size);
    }

    // Initializes the game grid, which is a 2D array of WordleResponse objects
    private WordleResponse[][] initializeWordleGrid() {
        WordleResponse[][] wordleGrid = new WordleResponse[maximumRows][columnCount];
        for (int row = 0; row < wordleGrid.length; row++) {
            for (int column = 0; column < wordleGrid[row].length; column++) {
                wordleGrid[row][column] = null;
            }
        }
        return wordleGrid;
    }

    // Sets the word list from an external source, providing a list of valid words for the game
    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }

    // Selects a new random word from the word list as the current word to guess
    public void setCurrentWord() {
        int index = getRandomIndex();
        currentWord = wordList.get(index).toCharArray();
    }

    // Adds a guessed character to the grid at the current column and updates the grid
    public void setCurrentColumn(char c) {
        currentColumn++;
        currentColumn = Math.min(currentColumn, (columnCount - 1));
        guess[currentColumn] = c;
        wordleGrid[currentRow][currentColumn] = new WordleResponse(c, Color.WHITE, Color.BLACK);
    }

    // Removes the last guessed character from the grid (backspace functionality)
    public void backspace() {
        if (currentColumn >= 0) {
            wordleGrid[currentRow][currentColumn] = null;
            guess[currentColumn] = ' ';
            currentColumn--;
        }
    }

    // Returns the current row as an array of WordleResponse objects
    public WordleResponse[] getCurrentRow() {
        return wordleGrid[getCurrentRowNumber()];
    }

    // Returns the index of the current row
    public int getCurrentRowNumber() {
        return currentRow - 1;
    }

    // Processes the current guess, compares it to the current word, and updates the grid with feedback
    // Returns false if the guess is invalid or true if the guess is processed successfully
    public boolean setCurrentRow() {
    	if (!validGuess()) {
            System.out.println("Invalid guess");
            return true;
        }
    	
    	// Step 0: Count occurrences of each letter in the target word
        Map<Character, Integer> letterCounts = new HashMap<>();
        for (char c : currentWord) {
            letterCounts.put(c, letterCounts.getOrDefault(c, 0) + 1);
        }

        boolean[] matchedGuess = new boolean[currentWord.length];

        // Step 1: First pass to mark correct positions (green)
        for (int column = 0; column < guess.length; column++) {
            char guessedLetter = guess[column];
            
            if (guessedLetter == currentWord[column]) {
                // Correct position, mark as green
                wordleGrid[currentRow][column] = new WordleResponse(guessedLetter, AppColors.GREEN, Color.WHITE);
                matchedGuess[column] = true;
                letterCounts.put(guessedLetter, letterCounts.get(guessedLetter) - 1); // Decrement count for green
                updateLetterState(guessedLetter, AppColors.GREEN); // Mark letter as green in letterState
            }
        }

        // Step 2: Second pass to mark wrong positions (yellow)
        for (int column = 0; column < guess.length; column++) {
            char guessedLetter = guess[column];

            // Skip if already marked as green
            if (wordleGrid[currentRow][column] != null && wordleGrid[currentRow][column].getBackgroundColor() == AppColors.GREEN) {
                continue;
            }

            // Check if the letter is in the target word in a different position and has remaining occurrences
            if (letterCounts.getOrDefault(guessedLetter, 0) > 0 && contains(currentWord, guess, column)) {
                wordleGrid[currentRow][column] = new WordleResponse(guessedLetter, AppColors.YELLOW, Color.BLACK);
                letterCounts.put(guessedLetter, letterCounts.get(guessedLetter) - 1);  // Decrement count for yellow
                updateLetterState(guessedLetter, AppColors.YELLOW); // Mark letter as yellow in letterState
            } else {
                // If not in the target word or no remaining occurrences, mark as gray
                wordleGrid[currentRow][column] = new WordleResponse(guessedLetter, AppColors.GRAY, Color.WHITE);
                updateLetterState(guessedLetter, AppColors.GRAY); // Mark letter as gray in letterState
            }
        }

        // Reset currentColumn to 0 for the next guess rather than -1
        currentColumn = 0;
        currentRow++;
        guess = new char[columnCount];  // Reset guess for the next row

        // Return whether there are more rows available
        return currentRow < maximumRows;
    }

    // Checks if the guessed word is in the word list
    public boolean isWordInList(String guess) {
        if (!wordListLoaded) {
            try {
                wordListFuture.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                return false;
            }
        }
        return wordList.contains(guess.toUpperCase());
    }

    // Checks if the guessed letter is present in the current word but at a different position
    private boolean contains(char[] currentWord, char[] guess, int column) {
        for (int index = 0; index < currentWord.length; index++) {
            if (index != column && guess[column] == currentWord[index]) {
                return true;
            }
        }
        return false;
    }

    // Returns the current state of the game board (grid)
    public WordleResponse[][] getWordleGrid() {
        return wordleGrid;
    }

    public int getMaximumRows() {
        return maximumRows;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

 // Returns the total count of words in the word list
    public int getTotalWordCount() {
        try {
            // Ensure the word list has been loaded
            if (!isWordListLoaded()) {
                wordListFuture.join(); // Wait for the word list to load if not loaded yet
            }

            // Check if the word list is not null before returning the size
            if (wordList != null) {
                return wordList.size();
            } else {
                System.err.println("Error: Word list is null.");
                return 0; // Or throw an exception if you'd prefer to handle it elsewhere
            }
        } catch (Exception e) {
            // Handle any exceptions that might occur during loading
            e.printStackTrace();
            System.err.println("Error: Unable to retrieve word list size.");
            return 0; // Return 0 or another error code to signify failure
        }
    }



    // Returns the statistics object for tracking game statistics
    public Statistics getStatistics() {
        return statistics;
    }

    // Checks if the game is currently running
    public Boolean isRunning() {
        return true;
    }

    // Returns the current guess as a string
    public String getCurrentGuess() {
        return new String(guess).trim();
    }

    // Records a win and updates the streak and games played
    public void recordWin(int rowNumber) {
        statistics.incrementTotalGamesPlayed();
        statistics.addWordsGuessed(rowNumber);
        statistics.setCurrentStreak(statistics.getCurrentStreak() + 1);
    }

    // Records a loss and resets the streak
    public void recordLoss() {
        statistics.incrementTotalGamesPlayed();
        statistics.setCurrentStreak(0);
    }

    // Checks if the current row is complete (all columns filled)
    public boolean isRowComplete() {
        return currentColumn >= (columnCount - 1);
    }

    // Returns the processed row with color feedback for each letter
    public WordleResponse[] getProcessedRow() {
        WordleResponse[] rowResponse = new WordleResponse[columnCount];
        Map<Character, Integer> letterCount = new HashMap<>();

        for (char c : currentWord) {
            letterCount.put(c, letterCount.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < columnCount; i++) {
            char guessedChar = guess[i];
            if (guessedChar == currentWord[i]) {
                rowResponse[i] = new WordleResponse(guessedChar, AppColors.GREEN, Color.WHITE);
                letterCount.put(guessedChar, letterCount.get(guessedChar) - 1);
            } else {
                rowResponse[i] = new WordleResponse(guessedChar, AppColors.GRAY, Color.WHITE);
            }
        }

        for (int i = 0; i < columnCount; i++) {
            char guessedChar = guess[i];
            if (rowResponse[i].getBackgroundColor() == AppColors.GRAY && letterCount.getOrDefault(guessedChar, 0) > 0) {
                rowResponse[i] = new WordleResponse(guessedChar, AppColors.YELLOW, Color.WHITE);
                letterCount.put(guessedChar, letterCount.get(guessedChar) - 1);
            }
        }

        return rowResponse;
    }
    
    private boolean validGuess() {
        // Convert guess to a trimmed, lowercase string
        String guessWord = new String(guess).trim().toLowerCase();
        
        // Check if the guessWord exists in the word list
        return wordList.contains(guessWord);
    }
    
    private void updateLetterState(char letter, Color newColor) {
        Color currentColor = letterState.getOrDefault(letter, AppColors.GRAY);

        // Only upgrade colors: green > yellow > gray
        if (currentColor == AppColors.GREEN) {
            return; // Already green, don’t downgrade
        } else if (currentColor == AppColors.YELLOW && newColor == AppColors.GRAY) {
            return; // Already yellow, don’t downgrade to gray
        }

        // Update the color in letterState if the newColor is a higher priority
        letterState.put(letter, newColor);
    }
    
    public void incrementTotalGamesPlayed() {
        statistics.incrementTotalGamesPlayed();
    }

    public void addWordsGuessed(int rowNumber) {
        statistics.addWordsGuessed(rowNumber);
    }

    public int getCurrentStreak() {
        return statistics.getCurrentStreak();
    }

    public void setCurrentStreak(int streak) {
        statistics.setCurrentStreak(streak);
    }
}

