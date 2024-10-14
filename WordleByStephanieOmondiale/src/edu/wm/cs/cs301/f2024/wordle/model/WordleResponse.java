package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;

public class WordleResponse {
	
	/**
	 * Character guessed by player
	 */
	private final char c;
	
	/**
	 * Responsible for color of grid column depending on correctness
	 * of player's guess
	 */
	private final ColorResponse colorResponse;

	/**
	 * Sets up player's guessed letter and it's associated background
	 * and foreground colors
	 * @param c Character guessed by player
	 * @param backgroundColor background color representing correctness of players guess
	 * @param foregroundColor Color of character itself
	 */
	public WordleResponse(char c, Color backgroundColor, Color foregroundColor) {
		this.c = c;
		this.colorResponse = new ColorResponse(backgroundColor, foregroundColor);
	}

	/**
	 * Retrieves the value of the character player guesses
	 * @return Returns the player's guessed letter
	 */
	public char getChar() {
		return c;
	}

	/**
	 * Returns the background color to indicate correctness of player's guess
	 * @return Returns background color
	 */
	public Color getBackgroundColor() {
		return colorResponse.getBackgroundColor();
	}

	/**
	 * Returns the foreground color (color of character)
	 * @return Returns the color of the foreground (character)
	 */
	public Color getForegroundColor() {
		return colorResponse.getForegroundColor();
	}
	
}
