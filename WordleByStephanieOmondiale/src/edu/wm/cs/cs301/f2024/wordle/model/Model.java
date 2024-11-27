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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class Model {
	
	// List of words that the player can guess from, populated from an external source
	protected List<String> wordList;
	
	//Max number of guesses player can make
    protected int maxGuesses = 6;
        
    //Processes a player's guess by depending on the current game state, updating the game
    public abstract String processGuess(String guess);
    
    //Determined is the game is over or not
    public abstract boolean isGameOver();
    
    //Character array for the current word and current guess
    protected char[] currentWord, guess;
    
    // Constants for the game grid: 5 letters per word, 6 guesses
       protected final int columnCount = 5;
       protected final int maximumRows = 6;
       
       // Index of current column and row
       // Initialized to -1 for column and 0 for row to indicate starting positions
       protected int currentColumn, currentRow;
      
       
       // Random object to select a random word from the word list
       protected final Random random = new Random();

       // Statistics object to track game statistics, such as streak and total games played
       protected final Statistics statistics = new Statistics();
       
       // Grid representing the game board, used to store feedback for each guessed letter
       protected WordleResponse[][] wordleGrid;
       
       // Flag to check if word list is loaded
       protected boolean wordListLoaded = false; 
       
       //Aids in word list loading 
       protected CompletableFuture<Void> wordListFuture;
       
       private String currentGuess; // The player's current guess
     
       //Green feedback for hard mode
       private Map<Integer, Character> greenMatches; 
       
       //Yellow feedback for hard mode
       private Map<Integer, Character> yellowMatches; 
       
       //Gray feedback for hard mode
       private List<Character> greyMatches; 
       
       // Getter and Setter for green matches in hard mode
       public Map<Integer, Character> getGreenMatches() {
           return greenMatches;
       }

       public void setGreenMatches(Map<Integer, Character> greenMatches) {
           this.greenMatches = greenMatches;
       }

       // Getter and Setter for yellow matches in hard mode
       public Map<Integer, Character> getYellowMatches() {
           return yellowMatches;
       }

       public void setYellowMatches(Map<Integer, Character> yellowMatches) {
           this.yellowMatches = yellowMatches;
       }

       // Getter and Setter for grey matches in hard mode
       public List<Character> getGreyMatches() {
           return greyMatches;
       }

       public void setGreyMatches(List<Character> greyMatches) {
           this.greyMatches = greyMatches;
       }

       
       // Checks if the word list has finished loading
       public boolean isWordListLoaded() {
           return wordListLoaded;
       }
    
       
       // Getter for the word list
       public List<String> getWordList() {
           return wordList;
       }

       // Setter for the word list
       protected void setWordList(List<String> wordList) {
           this.wordList = wordList;
       }
       
       // Tracks the color state of each letter for the visual keyboard
       protected Map<Character, Color> letterState = new HashMap<>();
       
       // Loads the word list asynchronously by starting a background thread
       protected void createWordList() {
           Thread backgroundThread = new Thread(() -> wordListLoaded = true);
           backgroundThread.start();
       }
       
       // Initializes the game grid, which is a 2D array of WordleResponse objects
       protected WordleResponse[][] initializeWordleGrid() {
           WordleResponse[][] wordleGrid = new WordleResponse[maximumRows][columnCount];
           for (int row = 0; row < wordleGrid.length; row++) {
               for (int column = 0; column < wordleGrid[row].length; column++) {
                   wordleGrid[row][column] = null;
               }
           }
           return wordleGrid;
       }
       
       /// Initialize currentRow to 0 (start of the game)
       public Model() {
           this.currentRow = 0; 
       }
       
    
    // Adds a guessed character to the grid at the current column and updates the grid
       protected void setCurrentColumn(char c) {
           currentColumn++;
           currentColumn = Math.min(currentColumn, (columnCount - 1));
           guess[currentColumn] = c;
           wordleGrid[currentRow][currentColumn] = new WordleResponse(c, AppColors.WHITE, AppColors.BLACK);
       }
       
    // Returns the index of the current row
       protected int getCurrentRowNumber() {
           return currentRow - 1;
       }

       // Processes the current guess, compares it to the current word, and updates the grid with feedback
       // Returns false if the guess is invalid or true if the guess is processed successfully
       protected boolean setCurrentRow() {
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
                   wordleGrid[currentRow][column] = new WordleResponse(guessedLetter, AppColors.GREEN, AppColors.WHITE);
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
                   wordleGrid[currentRow][column] = new WordleResponse(guessedLetter, AppColors.YELLOW, AppColors.BLACK);
                   letterCounts.put(guessedLetter, letterCounts.get(guessedLetter) - 1);  // Decrement count for yellow
                   updateLetterState(guessedLetter, AppColors.YELLOW); // Mark letter as yellow in letterState
               } else {
                   // If not in the target word or no remaining occurrences, mark as gray
                   wordleGrid[currentRow][column] = new WordleResponse(guessedLetter, AppColors.GRAY, AppColors.WHITE);
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

       // Removes the last guessed character from the grid (backspace functionality)
       protected void backspace() {
           if (currentColumn >= 0) {
               wordleGrid[currentRow][currentColumn] = null;
               guess[currentColumn] = ' ';
               currentColumn--;}
           }
           
        // Checks if the guessed word is in the word list
           boolean isWordInList(String guess) {
               if (!wordListLoaded) {
                   try {
                       wordListFuture.get(5, TimeUnit.SECONDS);
                   } catch (Exception e) {
                       return false;
                   }
               }
               return wordList.contains(guess.toUpperCase());
       }
           
           
        // Returns the current state of the game board (grid)
           protected WordleResponse[][] getWordleGrid() {
               return wordleGrid;
           }
           
           protected int getMaximumRows() {
               return maximumRows;
           }

           protected int getColumnCount() {
               return columnCount;
           }

           protected int getCurrentColumn() {
               return currentColumn;
           }
           
        // Returns the total count of words in the word list
           protected int getTotalWordCount() {
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
           protected Statistics getStatistics() {
               return statistics;
           }
           
        // Checks if the game is currently running
           protected Boolean isRunning() {
               return true;
           }
           
        // Returns the current guess as a string
           public String getCurrentGuess() {
               return new String(guess).trim();
           }
           
           public String getTheCurrentGuess() {
               return currentGuess;
           }
           
           public void setCurrentGuess(String currentGuess) {
               this.currentGuess = currentGuess;
           }
           
        // Records a win and updates the streak and games played
           protected void recordWin(int rowNumber) {
               statistics.incrementTotalGamesPlayed();
               statistics.addWordsGuessed(rowNumber);
               statistics.setCurrentStreak(statistics.getCurrentStreak() + 1);
           }
           
        // Records a loss and resets the streak
           protected void recordLoss() {
               statistics.incrementTotalGamesPlayed();
               statistics.setCurrentStreak(0);
           }
           
           // Checks if the current row is complete (all columns filled)
           protected boolean isRowComplete() {
               return currentColumn >= (columnCount - 1);
           }
           
           protected boolean validGuess() {
               // Convert guess to a trimmed, lowercase string
               String guessWord = new String(guess).trim().toLowerCase();
               
               // Check if the guessWord exists in the word list
               return wordList.contains(guessWord);
           }
           
           protected void updateLetterState(char letter, Color newColor) {
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
           
        // Checks if the guessed letter is present in the current word but at a different position
           protected boolean contains(char[] currentWord, char[] guess, int column) {
               for (int index = 0; index < currentWord.length; index++) {
                   if (index != column && guess[column] == currentWord[index]) {
                       return true;
                   }
               }
               return false;
           }
           
           protected void incrementTotalGamesPlayed() {
               statistics.incrementTotalGamesPlayed();
           }

           protected void addWordsGuessed(int rowNumber) {
               statistics.addWordsGuessed(rowNumber);
           }

           protected int getCurrentStreak() {
               return statistics.getCurrentStreak();
           }

           protected void setCurrentStreak(int streak) {
               statistics.setCurrentStreak(streak);
           }
           
           //Clears row for invalid guesses
           protected void clearRow(String[][] grid, int rowIndex) {
        	    for (int i = 0; i < grid[rowIndex].length; i++) {
        	        grid[rowIndex][i] = ""; // Clear each cell in the row
        	    }
        	}
           
           //Helper method to load words from file
           protected List<String> loadWordsFromFile(String filePath) throws IOException {
        	    List<String> words = new ArrayList<>();
        	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        	        String line;
        	        while ((line = reader.readLine()) != null) {
        	            line = line.trim();
        	            if (!line.isEmpty()) {
        	                words.add(line); // Add valid lines to the list
        	            }
        	        }
        	    }
        	    return words;
        	}
           
           public int getCurrentRow() {
       	    return currentRow;
       	}

           
           public void setCurrentWord(String word) {
               if (word != null) {
                   this.currentWord = word.toUpperCase().toCharArray(); // Convert to uppercase and then to char array
               } else {
                   this.currentWord = null; // Handle null case
               }
           }
           
           public char[] getCurrentWord() {
        	    return currentWord; // Return the currentWord character array
        	}
           
		
}

