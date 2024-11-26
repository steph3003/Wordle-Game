package edu.wm.cs.cs301.f2024.wordle.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import edu.wm.cs.cs301.f2024.wordle.controller.KeyboardButtonAction;
import edu.wm.cs.cs301.f2024.wordle.model.AppColors;
import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;

public class KeyboardPanel {

	private int buttonIndex, buttonCount;

	private final JButton[] buttons;

	private final JPanel panel;

	private final KeyboardButtonAction action;

	private final WordleModel model;
	
	private JButton onceButton;
	
	private JButton twiceButton;
	
	private JButton thriceButton;

	public KeyboardPanel(WordleFrame view, WordleModel model) {
		this.model = model;
		this.buttonIndex = 0;
		this.buttonCount = firstRow().length + secondRow().length
				+ thirdRow().length;
		this.buttons = new JButton[buttonCount];
		this.action = new KeyboardButtonAction(view, model);
		this.panel = createMainPanel();
		
		//initialize the help buttons
		this.onceButton = new JButton("Once");
		this.twiceButton = new JButton("Twice");
		this.thriceButton = new JButton("Thrice");
		
		//
		// Add action listeners to the buttons
		onceButton.addActionListener(e -> handleOnceButtonClick());
		twiceButton.addActionListener(e -> handleTwiceButtonClick());
		thriceButton.addActionListener(e -> handleThriceButtonClick());
	}
	
	//Helper method to check if a letter has been guessed
	private boolean isLetterGuessed(char letter, char[] guessArray) {
	    for (char c : guessArray) {
	        if (c == letter) {
	            return true; // Letter has already been guessed
	        }
	    }
	    return false;
	}
	
	private void handleOnceButtonClick() {
		// Ensure the model and current row are valid
	    if (model == null || model.getCurrentWord() == null) {
	        System.out.println("Error: Word or model is not initialized.");
	        return;
	    }
	    
	 // Get the current word to guess (as a char array)
	    char[] currentWord = model.getCurrentWord();
	    int currentRow = model.getCurrentRowNumber();
	    
	 // Get the current guess as a char array (or create an empty guess)
	    String currentGuess = model.getCurrentGuess();
	    char[] guessArray = currentGuess != null ? currentGuess.toCharArray() : new char[currentWord.length];
	    
	 // Find the next empty position in the current guess
	    for (int i = 0; i < currentWord.length; i++) {
	        if (guessArray[i] == '\0') {
	            // Fill the empty position with the correct letter
	            guessArray[i] = currentWord[i];
	            model.setCurrentGuess(new String(guessArray)); // Update the guess in the model

	            // Highlight the correct letter on the keyboard
	            char correctLetter = currentWord[i];
	            setColor(String.valueOf(correctLetter), AppColors.GREEN, AppColors.GREEN);

	            System.out.println("Once button selected: " + correctLetter);
	            return; // Exit after handling one position
	        }
	    }

	    // If no empty positions were found, do nothing
	    System.out.println("No empty positions to fill.");

	}

	private void handleTwiceButtonClick() {
		// Ensure the model and current row are valid
	    if (model == null || model.getCurrentWord() == null) {
	        System.out.println("Error: Word or model is not initialized.");
	        return;
	    }

	    // Get the current word to guess and current guess
	    char[] currentWord = model.getCurrentWord(); // Target word
	    String currentGuess = model.getCurrentGuess();
	    char[] guessArray = currentGuess != null ? currentGuess.toCharArray() : new char[currentWord.length];

	    // Identify unguessed letters in the target word
	    List<Character> unguessedLetters = new ArrayList<>();
	    for (char c : currentWord) {
	        if (!isLetterGuessed(c, guessArray)) {
	            unguessedLetters.add(c);
	        }
	    }

	    // Check if there are any unguessed letters
	    if (!unguessedLetters.isEmpty()) {
	        // Pick the first unguessed letter
	        char selectedLetter = unguessedLetters.get(0);

	        // Fill the selected letter in the first empty position
	        for (int i = 0; i < guessArray.length; i++) {
	            if (guessArray[i] == '\0') { // Find the first empty position
	                guessArray[i] = selectedLetter;
	                model.setCurrentGuess(new String(guessArray)); // Update the guess in the model

	                // Highlight the selected letter on the keyboard in yellow
	                setColor(String.valueOf(selectedLetter), AppColors.YELLOW, AppColors.YELLOW);

	                System.out.println("Twice button selected: " + selectedLetter);
	                return;
	            }
	        }
	    } else {
	        System.out.println("No remaining unguessed letters.");
	    }
	}

	private void handleThriceButtonClick() {
		// Ensure the model and current word are valid
	    if (model == null || model.getCurrentWord() == null || model.getWordList() == null) {
	        System.out.println("Error: Model, target word, or word list is not initialized.");
	        return;
	    }

	    // Get the current word, current guess, and word list
	    char[] currentWord = model.getCurrentWord(); // The target word
	    String currentGuess = model.getCurrentGuess();
	    List<String> wordList = model.getWordList();

	    // Get all unique letters in the current word
	    Set<Character> wordLetters = new HashSet<>();
	    for (char c : currentWord) {
	        wordLetters.add(c);
	    }

	    // Get all guessed letters
	    Set<Character> guessedLetters = new HashSet<>();
	    if (currentGuess != null) {
	        for (char c : currentGuess.toCharArray()) {
	            guessedLetters.add(c);
	        }
	    }

	    // Find the first letter in the word list that:
	    // - Is not in the target word
	    // - Has not been guessed yet
	    for (String word : wordList) {
	        for (char letter : word.toCharArray()) {
	            if (!wordLetters.contains(letter) && !guessedLetters.contains(letter)) {
	                // Highlight the letter in gray and notify the user
	                setColor(String.valueOf(letter), AppColors.GRAY, AppColors.GRAY);

	                System.out.println("Thrice button selected: " + letter);
	                return; // Exit after selecting the first valid letter
	            }
	        }
	    }

	    // If no valid letter is found
	    System.out.println("No unguessed letters outside the target word are available.");
	}
	
	private JPanel createHelpButtonsPanel() {
	    JPanel helpPanel = new JPanel(new FlowLayout());
	    helpPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

	    // Add buttons to the help panel
	    helpPanel.add(onceButton);
	    helpPanel.add(twiceButton);
	    helpPanel.add(thriceButton);

	    return helpPanel;
	}

	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1, 0, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));

		panel.add(createQPanel());
		panel.add(createAPanel());
		panel.add(createZPanel());
		panel.add(createTotalPanel());
		panel.add(createHelpButtonsPanel()); // Add the help buttons

		return panel;
	}

	private JPanel createQPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Font textfont = AppFonts.getTextFont();

		String[] letters = firstRow();

		for (int index = 0; index < letters.length; index++) {
			JButton button = new JButton(letters[index]);
			setKeyBinding(button, letters[index]);
			button.addActionListener(action);
			button.setFont(textfont);
			buttons[buttonIndex++] = button;
			panel.add(button);
		}

		return panel;
	}

	private String[] firstRow() {
		String[] letters = { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
				"Backspace" };
		return letters;
	}

	private JPanel createAPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Font textfont = AppFonts.getTextFont();

		String[] letters = secondRow();

		for (int index = 0; index < letters.length; index++) {
			JButton button = new JButton(letters[index]);
			setKeyBinding(button, letters[index]);
			button.addActionListener(action);
			button.setFont(textfont);
			buttons[buttonIndex++] = button;
			panel.add(button);
		}

		return panel;
	}

	private String[] secondRow() {
		String[] letters = { "A", "S", "D", "F", "G", "H", "J", "K", "L",
				"Enter" };
		return letters;
	}

	private JPanel createZPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Font textfont = AppFonts.getTextFont();

		String[] letters = thirdRow();

		for (int index = 0; index < letters.length; index++) {
			JButton button = new JButton(letters[index]);
			setKeyBinding(button, letters[index]);
			button.addActionListener(action);
			button.setFont(textfont);
			buttons[buttonIndex++] = button;
			panel.add(button);
		}

		return panel;
	}

	private String[] thirdRow() {
		String[] letters = { "Z", "X", "C", "V", "B", "N", "M" };
		return letters;
	}

	private void setKeyBinding(JButton button, String text) {
		InputMap inputMap = button.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW);
		if (text.equalsIgnoreCase("Backspace")) {
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
					"action");
		} else {
			inputMap.put(KeyStroke.getKeyStroke(text.toUpperCase()), "action");
		}
		ActionMap actionMap = button.getActionMap();
		actionMap.put("action", action);
	}

	private JPanel createTotalPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Font footerFont = AppFonts.getFooterFont();

		String text = String.format("%,d", model.getTotalWordCount());
		text += " possible " + model.getColumnCount() + "-letter words!";
		JLabel label = new JLabel(text);
		label.setFont(footerFont);
		panel.add(label);

		return panel;
	}

	public void setColor(String letter, Color backgroundColor,
			Color foregroundColor) {
		for (JButton button : buttons) {
			if (button.getActionCommand().equals(letter)) {
				Color color = button.getBackground();
				if (color.equals(AppColors.GREEN)) {
					// Do nothing
				} else if (color.equals(AppColors.YELLOW)
						&& backgroundColor.equals(AppColors.GREEN)) {
					button.setBackground(backgroundColor);
					button.setForeground(foregroundColor);
				} else {
					button.setBackground(backgroundColor);
					button.setForeground(foregroundColor);
				}
				break;
			}
		}
	}

	public void resetDefaultColors() {
		for (JButton button : buttons) {
			button.setBackground(null);
			button.setForeground(null);
		}
	}

	public JPanel getPanel() {
		return panel;
	}

}
