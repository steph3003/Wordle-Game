package edu.wm.cs.cs301.f2024.wordle.model;

public class MixedModel extends GameStrategy {
	
	private final GameStrategy absurdleModel;  // Absurdle strategy instance
    private final GameStrategy wordleModel;   // Wordle strategy instance
    private final SwitchStrategy switchStrategy; // Strategy to decide when to switch
    private GameStrategy currentModel;        // The currently active strategy

    
    public MixedModel(GameStrategy absurdleModel, GameStrategy wordleModel, SwitchStrategy switchStrategy) {
        this.absurdleModel = absurdleModel;
        this.wordleModel = wordleModel;
        this.switchStrategy = switchStrategy;
        this.currentModel = absurdleModel; // Start with Absurdle by default
    }

    
    @Override
    public void processGuess(String guess, int currentRow, int wordListSize) {
        // Check if the strategy should switch
        if (switchStrategy.shouldSwitch(currentRow, wordListSize)) {
            currentModel = (currentModel == absurdleModel) ? wordleModel : absurdleModel;
            System.out.println("Switching strategy to: " + (currentModel == wordleModel ? "Wordle" : "Absurdle"));
        }

        // Delegate the guess processing to the currently active strategy
        currentModel.processGuess(guess, currentRow, wordListSize);
    }

    
}
