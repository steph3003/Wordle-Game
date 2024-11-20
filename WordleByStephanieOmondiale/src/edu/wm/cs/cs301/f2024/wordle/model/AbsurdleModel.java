package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

public class AbsurdleModel extends Model {
	
	
	//Declares possible words
	private List<String> possibleWords;  
	
	// Declare remainingWords
	private List<String> remainingWords; 
	
	//Declares grid
	private String[][] grid;
	
	//verifies word list is loaded
<<<<<<< HEAD
	private boolean wordListLoaded = false;
	
	//Player's current guess
	public int currentGuess;
	
	//Set loading flag
	boolean isLoading = true; 
	
	//Correct word Guess
	public String correctWord;
	
	//Stores current word. For debugging
	private String currentWord;
	   
	//logger
	private static final Logger logger = Logger.getLogger(AbsurdleModel.class.getName());
	
	// Constructor: Initializes the game model
    public AbsurdleModel() {
        initialize();
        createWordList();
        getStatistics();
    }
    
    

    // Getter for the game grid
    public String[][] getGrid() {
        return grid;
    }

 // Returns the loaded word list for remaining words
    public synchronized List<String> getRemainingWords() {
        return remainingWords;
    }

	
	public AbsurdleModel(List<String> wordList) {
	       this.possibleWords = new ArrayList<>(wordList);
	       this.remainingWords = new ArrayList<>(wordList);
	   }
	   
	   // Reset the game to start with the full list
	   public void resetGame() {
	       remainingWords = new ArrayList<>(possibleWords);
	   }

	   public String guessWord(String guess) {
	       Map<String, List<String>> patternToWords = new HashMap<>();

	       // Generate patterns and map them to subsets of possible words
	       for (String word : remainingWords) {
	           String pattern = generatePattern(guess, word);
	           patternToWords.computeIfAbsent(pattern, k -> new ArrayList<>()).add(word);
	       }

	       // Select the largest subset of words
	       String chosenPattern = "";
	       int maxSubsetSize = 0;
	       for (Map.Entry<String, List<String>> entry : patternToWords.entrySet()) {
	           if (entry.getValue().size() > maxSubsetSize) {
	               maxSubsetSize = entry.getValue().size();
	               chosenPattern = entry.getKey();
	               remainingWords = entry.getValue();
	           }
	       }

	       // Log the guess and remaining words count
	       logger.info("Guess: " + guess + ", Pattern: " + chosenPattern + ", Remaining words: " + remainingWords.size());

	       return chosenPattern;
	   }

	   private String generatePattern(String guess, String word) {
	       StringBuilder pattern = new StringBuilder();
	       for (int i = 0; i < guess.length(); i++) {
	           if (guess.charAt(i) == word.charAt(i)) {
	               pattern.append("G"); // G for correct position (green)
	           } else if (word.contains(Character.toString(guess.charAt(i)))) {
	               pattern.append("Y"); // Y for wrong position (yellow)
	           } else {
	               pattern.append("B"); // B for no match (black)
	           }
	       }
	       return pattern.toString();
	   }

	   public int getRemainingWordsSize() {
	       return remainingWords.size();
	   }
=======
    public boolean wordListLoaded;
    
    //logger
    private static final Logger logger = Logger.getLogger(AbsurdleModel.class.getName());
    
    public AbsurdleModel(List<String> wordList) {
        this.possibleWords = new ArrayList<>(wordList);
        this.remainingWords = new ArrayList<>(wordList);
    }

    // Reset the game to start with the full list
    public void resetGame() {
        remainingWords = new ArrayList<>(possibleWords);
    }

    public String guessWord(String guess) {
        Map<String, List<String>> patternToWords = new HashMap<>();

        // Generate patterns and map them to subsets of possible words
        for (String word : remainingWords) {
            String pattern = generatePattern(guess, word);
            patternToWords.computeIfAbsent(pattern, k -> new ArrayList<>()).add(word);
        }

        // Select the largest subset of words
        String chosenPattern = "";
        int maxSubsetSize = 0;
        for (Map.Entry<String, List<String>> entry : patternToWords.entrySet()) {
            if (entry.getValue().size() > maxSubsetSize) {
                maxSubsetSize = entry.getValue().size();
                chosenPattern = entry.getKey();
                remainingWords = entry.getValue();
            }
        }

        // Log the guess and remaining words count
        logger.info("Guess: " + guess + ", Pattern: " + chosenPattern + ", Remaining words: " + remainingWords.size());

        return chosenPattern;
    }

    private String generatePattern(String guess, String word) {
        StringBuilder pattern = new StringBuilder();
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == word.charAt(i)) {
                pattern.append("G"); // G for correct position (green)
            } else if (word.contains(Character.toString(guess.charAt(i)))) {
                pattern.append("Y"); // Y for wrong position (yellow)
            } else {
                pattern.append("B"); // B for no match (black)
            }
        }
        return pattern.toString();
    }

    public int getRemainingWordsSize() {
        return remainingWords.size();
    }
>>>>>>> 848bf3a2d0d796431264496ded6de55279209a75



	// Checks if the word list has finished loading
	   public boolean isWordListLoaded() {
		   return (possibleWords != null && !possibleWords.isEmpty());
	   }
	   

	   // Loads the word list asynchronously by starting a background thread
	   public void createWordList() {
		   Thread loaderThread = new Thread(() -> {
	            try {
	                // Load the word list from a file
	                List<String> wordList = loadWordsFromFile("wordlist.txt");

	                // Safely update the word lists
	                synchronized (this) {
	                    possibleWords = new ArrayList<>(wordList);
	                    remainingWords = new ArrayList<>(wordList);
	                }

	                // Mark loading as complete
	                isLoading = false;
	            } catch (IOException e) {
	                e.printStackTrace();
	                isLoading = false; // Handle loading failure
	            }
	        });

	        loaderThread.start(); // Start the background thread
	    }

	    // Reads the word list from a file
	    private List<String> loadWordsFromFile(String filePath) throws IOException {
	        List<String> wordList = new ArrayList<>();
	        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String word;
	            while ((word = reader.readLine()) != null) {
	                word = word.trim();
	                if (!word.isEmpty()) {
	                    wordList.add(word); // Add valid words only
	                }
	            }
	        }
	        return wordList;
	    }

	    // Checks if the word list is still loading
	    public boolean isWordListLoading() {
	        return isLoading;
	    }

	    // Returns the loaded word list for possible words
	    public synchronized List<String> getPossibleWords() {
	        return possibleWords;
	    }


	    

	// Initializes the grid and generates a new current word for the player to guess
	   public void initialize() {
		// Ensure the word list is loaded
		    if (remainingWords == null || remainingWords.isEmpty()) {
		        throw new IllegalStateException("Word list is not loaded. Cannot initialize the game.");
		    }

		    //Set up the grid for guesses and feedback
		    initializeGrid();

		    //Reset remaining words (entire word list for Absurdle)
		    remainingWords = new ArrayList<>(possibleWords);

		    //Reset game state variables
		    currentGuess = 0;  // Reset guess count
		    isGameOver(); // Reset game-over flag

		    // Log initialization state (optional)
		    System.out.println("Game initialized with " + remainingWords.size() + " possible words.");
		}

		// Initializes the grid for guesses and feedback
		private void initializeGrid() {
		    grid = new String[maxGuesses][2]; // Grid for storing guesses and feedback
		    for (int i = 0; i < maxGuesses; i++) {
		        grid[i][0] = ""; // Placeholder for the player's guess
		        grid[i][1] = ""; // Placeholder for feedback
		    }
	   }
	   
	   
	// Randomly selects a word from the word list and stores it as the current word to guess
	   public void generateCurrentWord() {
	       //Randomly choose a word from the list and set it as the current target.
	   }
	   
	   
	   public String getCurrentWord() {
	    //Return the current word set for the player to guess
	       return null;
	   }
	   
	// Sets the word list from an external source, providing a list of valid words for the game
	   public void setWordList(List<String> wordList) {
	       //Store the given list as the word list for the game
	   }

	   // Selects a new random word from the word list as the current word to guess
	   public void setCurrentWord() {
		// Ensure the remaining words list is valid
		    if (remainingWords == null || remainingWords.isEmpty()) {
		        throw new IllegalStateException("Word list is not initialized or is empty. Cannot set current word.");
		    }

		    // Select a random word from the remaining words
		    Random random = new Random();
		    currentWord = remainingWords.get(random.nextInt(remainingWords.size()));

	   }

	   // Adds a guessed character to the grid at the current column and updates the grid
	   public void setCurrentColumn(char c) {
	      //Place character in grid and update position
	   }

<<<<<<< HEAD
	   // Removes the last guessed character from the grid (backspace functionality)
	   public void backspace() {
	       //Remove character from current column and adjust index
	   }

	   // Returns the current row as an array of WordleResponse objects
	   public WordleResponse[] getCurrentRow() {
	    //Retrieve the data for the current row in grid.
	       return null; //placeholder
	   }

	   // Returns the index of the current row
	   public int getCurrentRowNumber() {
	    //Return the row index
	       return 0;
	   }

	   // Processes the current guess, compares it to the current word, and updates the grid with feedback
	   // Returns false if the guess is invalid or true if the guess is processed successfully
	   public boolean setCurrentRow() {
	    //Validate guess against current word and update feedback in grid
	           return false; //placeholder
	   }

	   // Checks if the guessed word is in the word list
	   public boolean isWordInList(String guess) {
	    //Check if the word exists in the list
	      return false; //placeholder
	   }
	   
	// Returns the current state of the game board (grid)
	   public WordleResponse[][] getWordleGrid() {
	    //Return the grid as a 2D array of responses
	       return null; //placeholder
	   }

	   // returns the max number of rows
	   public int getMaximumRows() {
	    //Return the maximum rows limit
	       return 0; //placeholder
	   }

	   //returns the number of columns
	   public int getColumnCount() {
	    //Return the number of columns
	       return 0;//placeholder
	   }

	   //returns the current column
	   public int getCurrentColumn() {
	    //return the letter in current column
	       return 0; //placeholder
	   }

	// Returns the total count of words in the word list
	   public int getTotalWordCount() {
	    //Return the size of the word list
	    return 0; //placeholder
	   }
	   
	// Returns the statistics object for tracking game statistics
	   public Statistics getStatistics() {
	    //Return the statistics handler
	       return null; //placeholder
	   }

	   // Checks if the game is currently running
	   public Boolean isRunning() {
	    //Return the game running state
	       return false; //placeholder
	   }

	   // Returns the current guess as a string
	   public String getCurrentGuess() {
	    //Return the player's current guess
	       return null; //placeholder
	   }

	   // Records a win and updates the streak and games played
	   public void recordWin(int rowNumber) {
	       //Log win and update streak
	   }

	   // Records a loss and resets the streak
	   public void recordLoss() {
	    //Reset streak and log loss
	   }
	       

	   // Checks if the current row is complete (all columns filled)
	   public boolean isRowComplete() {
	    //Return true if all columns in the row are filled
	       return false; //placeholder
	   }

	   // Returns the processed row with color feedback for each letter
	   public WordleResponse[] getProcessedRow() {
	    //Return row data with feedback
	       return null; //placeholder
	   }
	   
	   //increases the total number of games played
	   public void incrementTotalGamesPlayed() {
	       //Increment the games played counter
	   }

	   //update the count of correctly guessed words based on rowNumber
	   public void addWordsGuessed(int rowNumber) {
	       //Log the correct guess in stats
	   }

	   //Returns the players current streak
	   public int getCurrentStreak() {
	    //Return the win streak
	       return 0; //placeholder
	   }

	   //set the player's current win streak to the specified value
	   public void setCurrentStreak(int streak) {
	       //set the streak value
	   }

	@Override
	public String processGuess(String guess) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isGameOver() {
		// Check if the correct word has been guessed
	    if (currentGuess > 0 && grid[currentGuess - 1][0].equals(correctWord)) {
	        return true; // Game over: Player guessed the correct word
	    }

	    // Check if maximum guesses have been reached
	    if (currentGuess >= maxGuesses) {
	        return true; // Game over: Player has used all guesses
	    }

	    // Otherwise, the game is not over
	    return false;
    }
	
=======
    //set the player's current win streak to the specified value
    public void setCurrentStreak(int streak) {
        //set the streak value
    }
    
>>>>>>> 848bf3a2d0d796431264496ded6de55279209a75
}
