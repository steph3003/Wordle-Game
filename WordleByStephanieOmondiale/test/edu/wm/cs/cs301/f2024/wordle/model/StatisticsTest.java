package edu.wm.cs.cs301.f2024.wordle.model;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StatisticsTest {
	
	/**
	 * Tests to see if file interacts with WordleModel
	 */
	@Test
	public void testGameFlowWithStatistics() {
	    WordleModel model = new WordleModel();
	    Statistics stats = model.getStatistics();

	    // Simulate some game events
	    stats.incrementTotalGamesPlayed();
	    stats.setCurrentStreak(3);
	    stats.writeStatistics();

	    // Create a new stats instance and read data
	    Statistics newStats = new Statistics();
	    newStats.readStatistics();

	    // Validate that the data matches the previous game session
	    Assert.assertEquals(3, newStats.getCurrentStreak());
	    Assert.assertEquals(1, newStats.getTotalGamesPlayed());
	}
    
    /**
     * Tests to see if current streak is initialized properly. Should return
     * default (zero) of current streak
     */
    @Test
    public void testGetCurrentStreakInitialValue() {
        Statistics stats = new Statistics();
        assertEquals(0, stats.getCurrentStreak());
    }
    
    private Statistics stats;

    @Before
    public void setUp() {
        stats = new Statistics();
    }
    
    /**
     * Tests to see if total games played is increased by 1. Should have total games 
     * played one number higher than previous number
     */
    @Test
    public void testIncrementTotalGamesPlayed() {
        // Set Up
        int initialGamesPlayed = stats.getTotalGamesPlayed();
        
        // Check functionality
        stats.incrementTotalGamesPlayed();
        
        int incrementedGamesPlayed = stats.getTotalGamesPlayed();
        
        // Check if Outcome is Correct
        assertEquals(initialGamesPlayed + 1, incrementedGamesPlayed);
    }
    
    /**
     * Tests if adding a single word to the words guessed list works correctly.
     * Only one word should be added to list
     */
    @Test
    public void testAddWordsGuessedSingleWordCount() {
        // Adding a single word count to the list
        stats.addWordsGuessed(5);
        
        // Verify the size of the list
        assertEquals(1, stats.getWordsGuessed().size());
        
        // Verify the value in the list
        assertEquals(5, (int) stats.getWordsGuessed().get(0));
    }
    
    /**
     * Tests that the initial longest streak is 0. Should show a 0.
     */
    @Before
    public void SetUp() {
        stats = new Statistics();
    }
    @Test
    public void testGetLongestStreak_initialValue() {
        // Verify that the initial longest streak is 0
        assertEquals(0, stats.getLongestStreak());
    }


    

}
