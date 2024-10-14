package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;

public class ColorResponse {
	
	/**
	 * Initializes a background and foreground color
	 */
	private final Color backgroundColor, foregroundColor;

	/**
	 * Assigns background and foreground parameters to "this" instance variable
	 * @param backgroundColor Allows for a specific background color 
	 * @param foregroundColor Allows for a specific foreground color
	 */
	public ColorResponse(Color backgroundColor, Color foregroundColor) {
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
	}

	/**
	 * Allows for access to the values for backgroundColor
	 * @return returns whatever the background color is set to
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Allows for access to the values for foregroundColor
	 * @return returns whatever the foreground color is set to
	 */
	public Color getForegroundColor() {
		return foregroundColor;
	}

}
