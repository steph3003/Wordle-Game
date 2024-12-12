package edu.wm.cs.cs301.f2024.wordle.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class MixedModelTest {
	 @Test
	    public void testSwitchAfterNGuesses() {
	        SwitchAfterNGuesses strategy = new SwitchAfterNGuesses(3);
	        assertFalse(strategy.shouldSwitch(1, 100));
	        assertTrue(strategy.shouldSwitch(3, 100));
	        assertTrue(strategy.shouldSwitch(4, 100));
	    }

	    @Test
	    public void testSwitchWhenWordListIsBelowThreshold() {
	        SwitchWhenWordListIsBelowThreshold strategy = new SwitchWhenWordListIsBelowThreshold(50);
	        assertFalse(strategy.shouldSwitch(1, 100));
	        assertTrue(strategy.shouldSwitch(2, 50));
	        assertTrue(strategy.shouldSwitch(3, 25));
	    }

	    @Test
	    public void testSwitchRandomly() {
	        SwitchRandomly strategy = new SwitchRandomly(50); // 50% chance
	        int switchCount = 0;
	        for (int i = 0; i < 1000; i++) {
	            if (strategy.shouldSwitch(1, 100)) {
	                switchCount++;
	            }
	        }
	        assertTrue(switchCount > 400 && switchCount < 600); // Roughly 50%
	    }
}
