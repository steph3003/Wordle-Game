package edu.wm.cs.cs301.f2024.wordle.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
//import java.util.concurrent.TimeUnit;


import java.util.Collections;
import java.util.List;
import java.awt.Color;
import java.util.Arrays;

import org.junit.Test;
public class AbsurdleModelTest {
	 AbsurdleModel model = new AbsurdleModel();
	 
	 /*
		 *Checks to see if setting an empty list results in an empty word list. 
		 *Word list should be empty when an empty list is provided
		 */
		
		@Test
	    public void testSetEmptyWordList() {
	        // Set Up
			AbsurdleModel model = new AbsurdleModel();
	        // Checks Functionality
	        model.setWordList(Collections.emptyList());

	        // Checks if Outcome is Correct
	        assertEquals(0, model.getTotalWordCount(), "The word list should be empty when an empty list is provided.");
	    }
		
		/**
		 *Checks to see if setting a one word list results in a one word list. 
		 *Word list should contain one word when one word is provided
		 */
	    @Test
	    public void testSetWordListWithOneWord() {
	        // Set Up
	    	AbsurdleModel model = new AbsurdleModel();
	        List<String> singleWord = Collections.singletonList("apple");

	        // Checks Functionality
	        model.setWordList(singleWord);

	        // Checks if Outcome is Correct
	        assertEquals(1, model.getTotalWordCount(), "The word list should contain one word.");
	    }
		
		/**
		 * This test should verify that only 5 letter words are in the list. Note: This test should fail
		 */
		@Test
		public void testSetWordListWithInvalidLengths() {
	        // Set Up
			AbsurdleModel model = new AbsurdleModel();
	        List<String> words = Arrays.asList("apple", "grape", "cat", "toolongword");

	        // Checks Functionality
	        model.setWordList(words);

	        // Checks if Outcome is Correct
	        assertEquals(2, model.getTotalWordCount(), "Only 5-letter words should be added to the list.");
	    }
		
		/**
		 *  Test with words having specific repeated characters. Multiple consecutively repeated character
		 *  words should not be accepted. Note: This test should fail
		 */
	    @Test
	    public void testSetWordListWithRepeatedCharacterWords() {
	        // Set Up
	    	AbsurdleModel model = new AbsurdleModel();
	        List<String> wordList = Arrays.asList("aaaaa", "aaaab", "apple");

	        // Check Functionality
	        model.setWordList(wordList);

	        // Check if outcome is correct
	        assertEquals(1, model.getTotalWordCount(), "The word list should contain three words.");
	        model.generateCurrentWord(); // Assuming this picks a word from the word list
	        String currentWord = new String(model.getCurrentWord());
	        assertTrue(wordList.contains(currentWord), "The generated current word should be one of the words in the word list.");
	    }
	    
	    /**
	     * Tests to see if the Wordle grid is initialized correctly. Grid should initialize
	     * as expected (example, with correct dimensions)
	     */
	    @Test
	    public void testWordGridIsInitializedProperly() {
	        // Set Up
	    	AbsurdleModel model = new AbsurdleModel();

	        // Check Functionality
	        model.initializeWordleGrid();

	        // Assert
	        WordleResponse[][] wordleGrid = model.getWordleGrid();
	        assertNotNull(wordleGrid, "The word grid should be initialized.");
	        assertEquals(model.getMaximumRows(), wordleGrid.length, "Word grid should have the correct number of rows.");
	        assertEquals(model.getColumnCount(), wordleGrid[0].length, "Word grid should have the correct number of columns.");
	    }
	    
	    /**
	     * This test should test if the current word selected is initialized correctly. Game should run smoothly
	     * if word is initialized correctly.
	     */
	    @Test
	    public void testCurrentWordIsSelectedAndInitialized() {
	        // Set Up
	    	AbsurdleModel model = new AbsurdleModel();
	        List<String> wordList = Arrays.asList("apple", "berry", "cherry");
	        model.setWordList(wordList);

	        // Check Functionality
	        model.setCurrentWord();

	        // Check if outcome is correct
	        String currentWord = model.getCurrentWord();
	        assertNotNull(currentWord, "Current word should be initialized after calling setCurrentWord().");
	        assertEquals(5, currentWord.length(), "Current word should have the length of 5.");
	    }
	    
	    /**
	     * Tests to see if current column is updated with the correct letter. Letter inputed should appear in column
	     */
	    @Test
	    public void testCurrentColumnIsUpdated() {
	        // Set Up
	    	AbsurdleModel model = new AbsurdleModel();
	        model.initializeWordleGrid();
	        int initialColumn = model.getCurrentColumn();
	        
	        // Check Functionality
	        model.setCurrentColumn('A');
	        
	        // Check if outcome is correct
	        assertEquals(initialColumn + 1, model.getCurrentColumn(), "Current column should be incremented by 1.");
	    }
	    
	    /** 
	     * Tests to see the functionality of the backspace button when at the first position on grid.
	     * Upon pressing back space, we should stay in position 9, and not go below that.
	     */
	    @Test
	    public void testBackspaceWhenCurrentColumnIsZero() {
	        // Set Up
	    	AbsurdleModel model = new AbsurdleModel();
	        model.initializeWordleGrid();
	        model.setCurrentColumn('A'); // Assume we add one character and set currentColumn to 0
	        model.backspace(); // This should set currentColumn to 0

	        // Check Functionality
	        model.backspace(); // Calling backspace again when currentColumn is zero

	        // Check if outcome is correct
	        assertEquals(0, model.getCurrentColumn(), "currentColumn should not go below zero.");
	    }
	    
	    /**
	     * Description: tests to make sure that all letters in the guess are green 
	     * when the guess matches the current word. Background should be green for each letter guessed correctly.
	     */
	    
	    @Test
	    public void testCorrectGuessAllGreen() {
	        // Set Up
	    	AbsurdleModel model = new AbsurdleModel();
	        model.initializeWordleGrid();
	        model.setWordList(List.of("APPLE"));
	        model.setCurrentWord(); // Sets current word to "APPLE"
	        model.setCurrentColumn('A');
	        model.setCurrentColumn('P');
	        model.setCurrentColumn('P');
	        model.setCurrentColumn('L');
	        model.setCurrentColumn('E');

	        // Check Functionality
	        boolean hasMoreGuesses = model.setCurrentRow();

	        // Check if Outcome is Correct
	        WordleResponse[] responses = model.getCurrentGuess();
	        for (WordleResponse response : responses) {
	            assertEquals(AppColors.GREEN, response.getBackgroundColor(), "All letters should be marked green for a correct guess.");
	        }
	        assertTrue(hasMoreGuesses, "The game should allow more guesses if maximum rows are not reached.");
	    }
	    
	    /**
	     * Provide a guess where some letters are correct in both value and position (green), 
	     * and some letters are present but in a different position (yellow). The correct letters 
	     * in the correct positions should have a green background and the correct letters in incorrect positions
	     *  should have a yellow background
	     */
	    
	    @Test
	    public void testPartiallyCorrectGuessMixedColors() {
	        // Set Up
	    	AbsurdleModel model = new AbsurdleModel();
	        model.initializeWordleGrid();
	        model.setWordList(List.of("APPLE"));
	        model.setCurrentWord(); // Sets current word to "RAIDS"
	        model.setCurrentColumn('M');
	        model.setCurrentColumn('A');
	        model.setCurrentColumn('I');
	        model.setCurrentColumn('S');
	        model.setCurrentColumn('D');

	        // Check Functionality
	        model.setCurrentRow();

	        // Check if Outcome is Correct5
	        WordleResponse[] responses = model.getCurrentGuess();
	        assertEquals(AppColors.GREEN, responses[1].getBackgroundColor(), "Letter A should be green.");
	        assertEquals(AppColors.GRAY, responses[0].getBackgroundColor(), "Letter M should be gray.");
	        assertEquals(AppColors.YELLOW, responses[3].getBackgroundColor(), "Letter S should be yellow.");
	    }
	    
	/**
	 * Test checks of a case where a guess has none of the letters present in the target word. 
	 * All letters should have gray as their background color.
	 */
	    @Test
	    public void testIncorrectGuessAllGray() {
	        // Set Up
	    	AbsurdleModel model = new AbsurdleModel();
	        model.initializeWordleGrid();
	        model.setWordList(List.of("APPLE"));
	        model.setCurrentWord(); // Sets current word to "APPLE"
	        model.setCurrentColumn('B');
	        model.setCurrentColumn('C');
	        model.setCurrentColumn('D');
	        model.setCurrentColumn('F');
	        model.setCurrentColumn('G');

	        // Check Functionality
	        model.setCurrentRow();

	        // Check if Outcome is Correct
	        WordleResponse[] responses = model.getCurrentGuess();
	        for (WordleResponse response : responses) {
	            assertEquals(AppColors.GRAY, response.getBackgroundColor(), "All letters should be marked gray for an incorrect guess.");
	        }
	    }
	    
	    /**
	     * Tests checks when after calling the initialize() method on the WordleModel, 
	     * if getStatistics() initializes statistics to original state. This should have statistics 
	     * contain the correct initial values.
	     */
	    
	    @Test
	    public void testGetStatisticsAfterInitialization() {
	        // Set Up
	    	AbsurdleModel model = new AbsurdleModel();
	        model.initializeWordleGrid();

	        // Check Functionality
	        Statistics stats = model.getStatistics();

	        // Check if Outcome is Correct
	        assertNotNull(stats, "The statistics object should still not be null after re-initialization.");
	        assertEquals(0, stats.getTotalGamesPlayed(), "The total games played should be reset to 0 after initialization.");
	    }
	    
	    @Test
	    public void testBug1ThreadSynchronization_GamestartsBeforeWordListLoads() {
	    	 // Create game
	    	AbsurdleModel model = new AbsurdleModel();
	        model.initializeWordleGrid();

	        // Check that game UI responds immediately
	        assertTrue("Game is running", model.isRunning());

	        // Try to make a guess before the word list is fully loaded
	        model.setCurrentColumn('A');
	        assertEquals('A', model.getCurrentGuess().charAt(0));  // Gets first value in column

	        // Check if the guess is stored even if word list is not loaded
	        long startTime = System.currentTimeMillis();
	        while (!model.isWordListLoaded() && (System.currentTimeMillis() - startTime) < 5000) {
	            try {
	                Thread.sleep(100);  // Wait in small intervals
	            } catch (InterruptedException e) {
	                fail("testFailed due to interruption");
	            }
	        }

	        // Ensure word list eventually loads
	        assertTrue("Word list should load eventually", model.isWordListLoaded());
	    }
	    
	    @Test
	    public void testBug2Coloring_YellowColorForSingleLetterOccurance() {
	    	model.setWordList(Arrays.asList("BLURB", "HELLO", "APPLE"));
	    	
	    	//make sure word list is loaded
	        model.wordListLoaded = true;

	        model.generateCurrentWord();

	        model.setCurrentColumn('H');
	        model.setCurrentColumn('E');
	        model.setCurrentColumn('L');
	        model.setCurrentColumn('L');
	        model.setCurrentColumn('O');

	        WordleResponse[] response = model.getProcessedRow();

	        int yellowCount = 0;
	        for (WordleResponse wr : response) {
	            if (wr.getBackgroundColor().equals(AppColors.YELLOW)){
	                yellowCount++;
	            }
	        }
	        assertEquals("There should be only one yellow 'L'", 1, yellowCount);
	    }
	    
	    
	    @Test
	    public void testBug3MustGuessRealWords_InvalidWords() {
	    	model.setWordList(Arrays.asList("BLURB", "HELLO", "APPLE"));
	    	
	    	//make sure the word list is loaded
	    	model.wordListLoaded = true;
	    	
	    	//test with valid word
	    	assertTrue("The guess 'HELLO' should be valid", model.isWordInList("HELLO"));
	    	
	    	//test with an invalid word
	    	assertFalse("The guess 'ABABA' should be invalid", model.isWordInList("HELLO"));
	    	
	    	//Guess invalid word
	    	 model.setCurrentColumn('H');
	         model.setCurrentColumn('E');
	         model.setCurrentColumn('L');
	         model.setCurrentColumn('L');
	         model.setCurrentColumn('O');
	         boolean result = model.setCurrentRow();
	         
	         //Make sure invalid guess is not processed
	         assertFalse("The guess 'ABABA' should not be processed", result);
	     	
	    }

	    
	    @Test
	    public void testBug4Backspace() {
	    	model.setCurrentColumn('A');
	        model.setCurrentColumn('P');
	        model.setCurrentColumn('P');
	        model.setCurrentColumn('L');
	        model.setCurrentColumn('E');
	        
	        //confirm set up
	        assertEquals(4, model.getCurrentColumn(), "Current column should be at position 4 after typing 5 letters");
	        assertEquals('A', model.getWordleGrid()[0][0].getChar(), "1st letter should be A");
	        assertEquals('E', model.getWordleGrid()[0][4].getChar(), " 5th letter should be E");
	        
	        //Do backspace and verify changes
	        model.backspace();
	        assertEquals(3, model.getCurrentColumn(), "Current column should dcecrement to 3 after one backspace");
	        assertNull(model.getWordleGrid()[0][4], "The 5th positon shouldl be null after backspace");
	        
	        model.backspace();
	        assertEquals(2,model.getCurrentColumn(), "Current column should dcecrement to 2");
	        assertNull(model.getWordleGrid()[0][3],"The 4th positon shouldl be null after backspace");
	        
	        model.backspace();
	        assertEquals(1,model.getCurrentColumn(), "Current column should dcecrement to 1");
	        assertNull(model.getWordleGrid()[0][2], "The 3th positon shouldl be null after backspace");
	        
	        model.backspace();
	        assertEquals(0,model.getCurrentColumn(), "Current column should dcecrement to 0");
	        assertNull(model.getWordleGrid()[0][1], "The 2nd positon shouldl be null after backspace");
	        
	        model.backspace();
	        assertEquals(-1, model.getCurrentColumn(), "Current column should be -1 after clearing all letters");
	        assertNull(model.getWordleGrid()[0][0], "The 1st positon shouldl be null after backspace");
	        
	    
	    }
	    
	    @Test
	    public void testBug5KeyboardColors() {
	    	// Set up game model with "BLURB" as the target word
	        model.setWordList(Arrays.asList("BLURB"));
	        model.wordListLoaded = true;
	        model.generateCurrentWord();  // Sets "BLURB" as the target word

	        // Make the first guess "HELLO"
	        model.setCurrentColumn('H');
	        model.setCurrentColumn('E');
	        model.setCurrentColumn('L');
	        model.setCurrentColumn('L');
	        model.setCurrentColumn('O');
	        model.setCurrentRow();  // Finalize the guess

	        // Retrieve the row from wordleGrid to check colors for the guess "HELLO"
	        WordleResponse[] responseRow = model.getCurrentGuess();  // Assume this retrieves the latest row in the grid

	        // Check expected colors for each letter
	        assertEquals("H should be gray (incorrect)", AppColors.GRAY, responseRow[0].getBackgroundColor());
	        assertEquals("E should be gray (incorrect)", AppColors.GRAY, responseRow[1].getBackgroundColor());
	        assertEquals("First L should be yellow (wrong position)", AppColors.YELLOW, responseRow[2].getBackgroundColor());
	        assertEquals("Second L should be gray (no second L in BLURB)", AppColors.GRAY, responseRow[3].getBackgroundColor());
	        assertEquals("O should be gray (incorrect)", AppColors.GRAY, responseRow[4].getBackgroundColor());

	        // Make a second guess "BLUSH" to see if previously used letters are updated correctly
	        model.setCurrentColumn('B');
	        model.setCurrentColumn('L');
	        model.setCurrentColumn('U');
	        model.setCurrentColumn('S');
	        model.setCurrentColumn('H');
	        model.setCurrentRow();  // Finalize the guess

	        // Retrieve the next row from wordleGrid to check colors for the guess "BLUSH"
	        WordleResponse[] responseRow2 = model.getCurrentRow();

	        // Check expected colors for each letter in the second guess
	        assertEquals("B should be green (correct position)", AppColors.GREEN, responseRow2[0].getBackgroundColor());
	        assertEquals("L should be green (correct position)", AppColors.GREEN, responseRow2[1].getBackgroundColor());
	        assertEquals("U should be green (correct position)", AppColors.GREEN, responseRow2[2].getBackgroundColor());
	        assertEquals("S should be gray (incorrect)", AppColors.GRAY, responseRow2[3].getBackgroundColor());
	        assertEquals("H should be gray (incorrect)", AppColors.GRAY, responseRow2[4].getBackgroundColor());

	    }
	    
	    @Test
	    public void testRecordWin() {
	        // Arrange: Initialize the AbsurdleModel and set up any necessary values
	        AbsurdleModel model = new AbsurdleModel();
	        model.initializeWordleGrid();
	        int initialTotalGames = model.getStatistics().getTotalGamesPlayed();
	        int initialWinStreak = model.getCurrentStreak();

	        // Act: Record a win
	        model.recordWin(3);  // Assuming row number 3 is where the player won

	        // Assert: Check that total games played and win streak have incremented correctly
	        assertEquals("Total games played should increment by 1", initialTotalGames + 1, model.getStatistics().getTotalGamesPlayed());
	        assertEquals("Win streak should increment by 1", initialWinStreak + 1, model.getCurrentStreak());
	    }
	    
	    @Test
	    public void testRecordLoss() {
	        // Arrange: Initialize the AbsurdleModel and set up any necessary values
	        AbsurdleModel model = new AbsurdleModel();
	        model.initializeWordleGrid();

	        // Simulate a winning streak to ensure it's reset by the loss
	        model.recordWin(1);
	        model.recordWin(2);
	        int initialTotalGames = model.getStatistics().getTotalGamesPlayed();
	        int winStreakBeforeLoss = model.getCurrentStreak();

	        // Verify the streak is greater than zero before the loss
	        assertTrue("Win streak should be greater than zero before recording a loss", winStreakBeforeLoss > 0);

	        // Act: Record a loss
	        model.recordLoss();

	        // Assert: Check that total games played has incremented and win streak is reset to zero
	        assertEquals("Total games played should increment by 1", initialTotalGames + 1, model.getStatistics().getTotalGamesPlayed());
	        assertEquals("Win streak should reset to zero after a loss", 0, model.getCurrentStreak());
	    }
}
