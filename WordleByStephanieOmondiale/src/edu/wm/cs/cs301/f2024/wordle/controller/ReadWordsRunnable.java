package edu.wm.cs.cs301.f2024.wordle.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;

public class ReadWordsRunnable implements Runnable {

	/**
	 * Creates a logger that can be used to log messages to help with debugging, tracking the game flow, or reporting errors
	 */
	private final static Logger LOGGER =
			Logger.getLogger(ReadWordsRunnable.class.getName());

	/**
	 * References the game's model
	 */
	private final WordleModel model;

	/**
	 * Constructor prepares the game to read in words
	 * @param model Gives access to the current game's state
	 */
	public ReadWordsRunnable(WordleModel model) {
		LOGGER.setLevel(Level.INFO);

		try {
			FileHandler fileTxt = new FileHandler("./logging.txt");
			LOGGER.addHandler(fileTxt);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.model = model;
	}

	/**
	 * This method creates a word list for the game and sets it in the model.
	 *  It also handles logging information and manages exceptions if something goes wrong during the 
	 *  word list creation
	 */
	@Override
	public void run() {
		List<String> wordlist;

		try {
			wordlist = createWordList();
			LOGGER.info("Created word list of " + wordlist.size() + " words.");
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			wordlist = new ArrayList<>();
		}

		model.setWordList(wordlist);
		model.generateCurrentWord();
	}

	/**
	 * Opens an input stream for the usa.txt file
	 * @return Returns the stream so that other parts of the program can read from the file
	 */
	private InputStream deliverInputStream() {
		String text = "/resources/usa.txt";
		// Original code
		/*
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream stream = loader.getResourceAsStream(text);
		*/
		// https://stackoverflow.com/questions/68314700/why-java-cannot-read-same-resource-file-when-module-info-is-added
		
		InputStream stream = Wordle.class.getResourceAsStream(text);
		
		if (null == stream) {
			System.out.println("Failed to open stream with " + text);
			System.exit(0);
		}
		else 
			System.out.println("Successfully opened inputstream for " + text);
		
		return stream;
	}
	
	/**
	 * Reads a list of words, checks each word to see if it matches the required length, and then stores the valid words in a list
	 * @return returns this list of valid words
	 * @throws IOException
	 */
	private List<String> createWordList() throws IOException {
		int minimum = model.getColumnCount();

		List<String> wordlist = new ArrayList<>();

		
		InputStream stream = deliverInputStream();

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		String line = reader.readLine();
		while (line != null) {
			line = line.trim();
			if (line.length() == minimum) {
				wordlist.add(line);
			}
			line = reader.readLine();
		}
		reader.close();

		return wordlist;
	}
	
}
