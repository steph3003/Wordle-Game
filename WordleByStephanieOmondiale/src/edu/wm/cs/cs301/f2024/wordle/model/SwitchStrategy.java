package edu.wm.cs.cs301.f2024.wordle.model;

public interface SwitchStrategy {
	boolean shouldSwitch(int currentRow, int wordListSize);
}
