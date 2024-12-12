package edu.wm.cs.cs301.f2024.wordle.model;
import java.util.Random;
import java.util.logging.Logger;

public class SwitchRandomly implements SwitchStrategy {
	private static final Logger logger = Logger.getLogger(SwitchRandomly.class.getName());

	
	private final Random random;
    private final int switchChance; // A chance to switch each turn, represented by percentage 1 to 100

    // Constructor: Accepts a probability of switching (1–100)
    public SwitchRandomly(int switchChance) {
        if (switchChance < 1 || switchChance > 100) {
            throw new IllegalArgumentException("Switch chance must be between 1 and 100");
        }
        this.switchChance = switchChance;
        this.random = new Random();
    }

    // Method to determine if the strategy should switch
    @Override
    public boolean shouldSwitch(int currentRow, int wordListSize) {
        // Generate a random number between 1 and 100
        int randomValue = random.nextInt(100) + 1;
        
     // Log the random value and the decision process
        logger.info("Current row: " + currentRow + ", Word list size: " + wordListSize);
        logger.info("Generated random value: " + randomValue + ", Switch chance: " + switchChance);

        boolean shouldSwitch = randomValue <= switchChance;
        logger.info("Decision to switch: " + (shouldSwitch ? "Yes" : "No"));


        // Return true if randomValue is less than or equal to the switch chance
        return randomValue <= switchChance;
    }
}