package edu.wm.cs.cs301.f2024.wordle.model;

public class GameStrategy {
	public static void main(String[] args) {
        // Default to "random" strategy if no argument is provided
        String strategy = args.length > 0 ? args[0].toLowerCase() : "random";
        
        // Instantiate the model based on the chosen strategy
        AbsurdleModel model;
        if (strategy.equals("absurdle")) {
            model = new AbsurdleModel();
            System.out.println("Starting game with Absurdle strategy.");
        } else {
            model = new AbsurdleModel();
            System.out.println("Starting game with Random strategy (default).");
        }

        // Initialize the model and start the game
        model.initialize();
        // Continue with game setup or UI invocation here
    }
}
