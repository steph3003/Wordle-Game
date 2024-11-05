package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import edu.wm.cs.cs301.f2024.wordle.controller.ReadWordsRunnable;

public class WordleModel {
	
	/**
	 * Array holds the characters of the word player is trying to guess
	 */
	private char[] currentWord, guess;
	
	/**
	 * These 4 variables keeps track of direction in which the player is on the game board
	 */
	private final int columnCount, maximumRows;
	private int currentColumn, currentRow;
	
	/**
	 * 
	 * This keep in a list all of the possible words to be used in the game
	 */
	private List<String> wordList;
	
	/**
	 * Picks a random word from the word list to be guessed
	 */
	private final Random random;
	
	/**
	 * Tracks game statistics
	 */
	private final Statistics statistics;
	
	// check if word list is loaded
		boolean wordListLoaded = false;
		private CompletableFuture<Void> wordListFuture; 
		
		
		public boolean isWordListLoaded(){
			return wordListLoaded;
		}
	
	/**
	 * Tracks the players guesses and the game's progress
	 */
	private WordleResponse[][] wordleGrid;
	
	public WordleModel() {
		this.currentColumn = -1;
		this.currentRow = 0;
		this.columnCount = 5;
		this.maximumRows = 6;
		this.random = new Random();
		
		createWordList();
		
		this.wordleGrid = initializeWordleGrid();
		this.guess = new char[columnCount];
		this.statistics = new Statistics();
	}
	
	 // Loads the word list asynchronously by starting a background thread
    public void createWordList() {
        Thread backgroundThread = new Thread(new Runnable() {
        	@Override
        	public void run() {
        		wordListLoaded = true;
        	}
        });
        backgroundThread.start();
    }
	
	/**
	 * Loads the word list in a background thread
	 */
	/*
	 * private void createWordList() {
		Thread backgroundThread = new Thread(new Runnable() {
	        @Override
	        public void run() {
	            // After loading completes, update the flag
	            wordListLoaded = true;
	        }
	    });
	    backgroundThread.start();
	}
	
	
	 */
	
	/**
	 * Sets up or resets the game state
	 */
	public void initialize() {
		this.wordleGrid = initializeWordleGrid();
		this.currentColumn = -1;
		this.currentRow = 0;
		generateCurrentWord();
		this.guess = new char[columnCount];
	}

	/**
	 * Generates the word that the player will guess
	 */
	public void generateCurrentWord() {
		// Wait for word list to load if it's not ready
	    if (!wordListLoaded) {
	        try {
	            wordListFuture.join();  // Ensures word list is loaded before proceeding
	            wordListLoaded = true;  // Mark as loaded after join
	        } catch (Exception e) {
	            e.printStackTrace();
	            return;  // Exit if loading fails
	        }
	    }
	    // Ensure wordList is not null or empty to avoid errors
	    if (wordList != null && !wordList.isEmpty()) {
	    	String word = getCurrentWord();
	        this.currentWord = word.toUpperCase().toCharArray();
	    } else {
	        System.err.println("Error: Word list is empty or failed to load.");
	    }
	}

	public String getCurrentWord() {
		return wordList.get(getRandomIndex());
	}

	private int getRandomIndex() {
		int size = wordList.size();
		return random.nextInt(size);
	}
	
	private WordleResponse[][] initializeWordleGrid() {
		WordleResponse[][] wordleGrid = new WordleResponse[maximumRows][columnCount];

		for (int row = 0; row < wordleGrid.length; row++) {
			for (int column = 0; column < wordleGrid[row].length; column++) {
				wordleGrid[row][column] = null;
			}
		}

		return wordleGrid;
	}
	
	/**
	 * Updates the word list with new words
	 * @param wordList List of the new word list
	 */
	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}
	
	/**
	 * Selects a word from a list of words and converts
	 * it into a character array to become the word the 
	 * player has to guess
	 */
	public void setCurrentWord() {
		int index = getRandomIndex();
		currentWord = wordList.get(index).toCharArray();
	}
	
	/**
	 * Updates the current column by adding character that player
	 * entered to the grid and the guess array
	 * @param c The letter the player types
	 */
	public void setCurrentColumn(char c) {
		currentColumn++;
		currentColumn = Math.min(currentColumn, (columnCount - 1));
		guess[currentColumn] = c;
		wordleGrid[currentRow][currentColumn] = new WordleResponse(c,
				Color.WHITE, Color.BLACK);
	}
	
	/**
	 * Deletes the most recent entered letter of a player's guess 
	 * when backspace is pressed 
	 */
	public void backspace() {
		if (currentColumn >= 0) {
            wordleGrid[currentRow][currentColumn] = null;
            guess[currentColumn] = ' ';
            currentColumn--;
        }
	}
	
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
	
	/**
	 * Returns the current row, the part of the grid storing the player's guess
	 * @return Return's the player's guess
	 */
	public WordleResponse[] getCurrentRow() {
		return wordleGrid[getCurrentRowNumber()];
	}
	
	/**
	 * Returns current row index
	 * @return Returns index where player is making their guess
	 */
	public int getCurrentRowNumber() {
		return currentRow -1;
	}
	
	/**
	 * Processes the players guess and updates the grid correspondingly,
	 * (green for correct guess, yellow for correct but wrong position, etc.)
	 * @return Checks if player has more guesses left by comparing current row
	 * to max allowed rows
	 * returns false if max number of attempts have been reached. 
	 */
	public boolean setCurrentRow() {
		
		String currentGuess = new String(guess).trim().toUpperCase();
		 if (!isWordInList(currentGuess)) {
	            System.out.println("Invalid guess. Try a different word.");
	            return false;
	        }
		 for (int column = 0; column < guess.length; column++) {
	            Color backgroundColor = AppColors.GRAY;
	            Color foregroundColor = Color.WHITE;
	            if (guess[column] == currentWord[column]) {
	                backgroundColor = AppColors.GREEN;
	            } else if (contains(currentWord, guess, column)) {
	                backgroundColor = AppColors.YELLOW;
	            }
	            wordleGrid[currentRow][column] = new WordleResponse(guess[column], backgroundColor, foregroundColor);
	        }

	        currentColumn = -1;
	        currentRow++;
	        guess = new char[columnCount];

	        return currentRow < maximumRows;
	    
	}
	

	/**
	 * Returns the current game board with players inputs
	 * @return Returns game board
	 */
	public WordleResponse[][] getWordleGrid() {
		return wordleGrid;
	}
	
	/**
	 * Returns the max amount of rows (guesses)
	 * @return Returns value of the max amount of rows
	 */
	public int getMaximumRows() {
		return maximumRows;
	}

	/**
	 * Returns the number of columns (letters in the word)
	 * @return Returns the number of columns (letters) for a word
	 */
	public int getColumnCount() {
		return columnCount;
	}
	
	/**
	 * Returns the value of the current column (current position player is
	 * typing their guess in)
	 * @return Current position player is typing their guess
	 */
	public int getCurrentColumn() {
		return currentColumn;
	}

	/**
	 * Returns the total number of words in the word list
	 * @return Returns the number of possible words that can be guessed
	 */
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

	/**
	 * Returns game statistics
	 * @return Returns player's game statistics
	 */
	public Statistics getStatistics() {
		return statistics;
	}
	
	public Boolean isRunning(){
		return true;
	}
	
	public String getCurrentGuess(){
		return new String(guess).trim();
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
	
	boolean validGuess() {
		String guessWord = new String(guess).trim().toLowerCase();
		System.out.println("Guess Word " + guessWord);
		boolean valid = wordList.contains(guessWord);
		System.out.println(wordList.get(0));
		return wordList.contains(guessWord);
	}
	
	
	/**
	 * checks if the word contains the letter. Checks if the current guess contains the guessed letter
	 * but in the wrong place
	 * @param currentWord The correct word
	 * @param guess The players guess
	 * @param column The column being checked
	 * @param matchedGuess the player's guess matches the current word
	 * @return Returns true if the letter is in the word but not in the right column. return false otherwise
	 */
	// Checks if the guessed letter is present in the current word but at a different position
    private boolean contains(char[] currentWord, char[] guess, int column) {
        for (int index = 0; index < currentWord.length; index++) {
            if (index != column && guess[column] == currentWord[index]) {
                return true;
            }
        }
        return false;
    }
	
	
}
