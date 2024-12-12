package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.logging.Logger;

public class MixedModel extends GameStrategy {
	
	private static final Logger logger = Logger.getLogger(MixedModel.class.getName());

	
	private final GameStrategy absurdleModel;  // Absurdle strategy instance
    private final GameStrategy wordleModel;   // Wordle strategy instance
    private final SwitchStrategy switchStrategy; // Strategy to decide when to switch
    private GameStrategy currentModel;        // The currently active strategy

    
    public MixedModel(GameStrategy absurdleModel, GameStrategy wordleModel, SwitchStrategy switchStrategy) {
        this.absurdleModel = absurdleModel;
        this.wordleModel = wordleModel;
        this.switchStrategy = switchStrategy;
        this.currentModel = absurdleModel; // Start with Absurdle, default
    }

    
    @Override
    public void processGuess(String guess, int currentRow, int wordListSize) {
    	 logger.info("Processing guess: " + guess);
    	    logger.info("Candidate set size: " + wordListSize);

    	    if (switchStrategy.shouldSwitch(currentRow, wordListSize)) {
    	        currentModel = (currentModel == absurdleModel) ? wordleModel : absurdleModel;
    	        logger.info("Switch occurred at row: " + currentRow);
    	        logger.info("Switched to strategy: " + (currentModel == wordleModel ? "Wordle" : "Absurdle"));
    	    }

    	    currentModel.processGuess(guess, currentRow, wordListSize); 
    }
}
