package edu.wm.cs.cs301.f2024.wordle.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Statistics {
	
	/**
	 * Defines variables that track streak and games played
	 */
	private int currentStreak, longestStreak, totalGamesPlayed;
	
	/**
	 * A list of the number of words guessed in a game
	 */
	private List<Integer> wordsGuessed;
	
	/**
	 * Path holds a file location or directory as a string and log stores a log message or information
	 */
	private String path, log;
	
	private List<Boolean> gamesWon; // True if the game is won, false otherwise.
	
	private List<Integer> guessesPerGame; // Positive value for wins, negative or 0 for losses

	
	/**
	 * This method sets up the environment for tracking and storing game statistics
	 */
	public Statistics() {
		this.wordsGuessed = new ArrayList<>();
		String fileSeparator = System.getProperty("file.separator");
		this.path = System.getProperty("user.home") + fileSeparator + "Wordle";
		this.log = fileSeparator + "statistics.log";
		readStatistics();
	}
	
	/**
	 *  This method reads previously saved game statistics from a file and loads them into the program
	 */
	void readStatistics() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + log));
			this.currentStreak = Integer.valueOf(br.readLine().trim());
			this.longestStreak = Integer.valueOf(br.readLine().trim());
			this.totalGamesPlayed = Integer.valueOf(br.readLine().trim());
			int totalWordsGuessed = Integer.valueOf(br.readLine().trim());
			
			for (int index = 0; index < totalWordsGuessed; index++) {
				wordsGuessed.add(Integer.valueOf(br.readLine().trim()));
			}
			br.close();
		} catch (FileNotFoundException e) {
			this.currentStreak = 0;
			this.longestStreak = 0;
			this.totalGamesPlayed = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * method saves the game statistics to a file so that the data can be accessed later
	 */
	public void writeStatistics() {
		try {
			File file = new File(path);
			file.mkdir();
			file = new File(path + log);
			file.createNewFile();

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(Integer.toString(currentStreak));
			bw.write(System.lineSeparator());
			bw.write(Integer.toString(longestStreak));
			bw.write(System.lineSeparator());
			bw.write(Integer.toString(totalGamesPlayed));
			bw.write(System.lineSeparator());
			bw.write(Integer.toString(wordsGuessed.size()));
			bw.write(System.lineSeparator());
			
			for (Integer value : wordsGuessed) {
				bw.write(Integer.toString(value));
				bw.write(System.lineSeparator());
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sees how many wins, or other streaks, the player has in the game
	 * @return returns the player's current streak
	 */
	public int getCurrentStreak() {
		return currentStreak;
	}

	/**
	 *  Updates the value of the current streak, and sees if it is the highest streak
	 * @param currentStreak Value for the player's current streak
	 */
	public void setCurrentStreak(int currentStreak) {
		this.currentStreak = currentStreak;
		if (currentStreak > longestStreak) {
			this.longestStreak = currentStreak;
		}
	}

	/**
	 * Gets the player's longest streak in the game
	 * @return Return's player's longest streak
	 */
	public int getLongestStreak() {
		return longestStreak;
	}

	/**
	 * Tracks number of games player has played
	 * @return Returns player's total games played
	 */
	public int getTotalGamesPlayed() {
		return totalGamesPlayed;
	}

	/**
	 * Increases the total number of games played
	 */
	public void incrementTotalGamesPlayed() {
		this.totalGamesPlayed++;
	}

	/**
	 * Keeps a list of the words the player has guessed
	 * @return returns the words that were guessed
	 */
	public List<Integer> getWordsGuessed() {
		return wordsGuessed;
	}

	/**
	 * Adds a number to a list that tracks how many words were guessed across different games.
	 * @param wordCount Number of words guessed in a single game
	 */
	public void addWordsGuessed(int wordCount) {
		this.wordsGuessed.add(wordCount);
	}

	public void setFile(String path2) {
		// TODO Auto-generated method stub
		
	}
	
	public int getTotalGamesWon() {
	    int totalWins = 0;
	    for (boolean isWin : gamesWon) {
	        if (isWin) {
	            totalWins++;
	        }
	    }
	    return totalWins;
	}

}
