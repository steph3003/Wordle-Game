package edu.wm.cs.cs301.f2024.wordle.view;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SettingsDialog extends JDialog {
    
	private static final long serialVersionUID = 1L;

	public SettingsDialog() {
        setTitle("Settings");
        setSize(500, 400);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Load HTML content from settings.htm
        try {
            JEditorPane editorPane = new JEditorPane();
            editorPane.setEditable(false);
            editorPane.setContentType("text/html");
            editorPane.setPage(getClass().getResource("/resources/settings.htm"));

            JScrollPane scrollPane = new JScrollPane(editorPane);
            add(scrollPane, BorderLayout.CENTER);
        } catch (IOException e) {
            JLabel errorLabel = new JLabel("Error loading settings page.");
            add(errorLabel, BorderLayout.CENTER);
        }
    }
}

