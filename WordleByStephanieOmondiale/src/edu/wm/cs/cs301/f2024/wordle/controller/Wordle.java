package edu.wm.cs.cs301.f2024.wordle.controller;

import javax.swing.SwingUtilities;

import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;
import edu.wm.cs.cs301.f2024.wordle.view.WordleFrame;

public class Wordle implements Runnable {
	
	/**
	 * This method starts the program by initializing the game
	 * @param args Array of strings that holds any arguments passed to the program
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Wordle());
	}

	/**
	 * Starts up a new game
	 */
	@Override
	public void run() {
		new WordleFrame(new WordleModel());
	}

}

