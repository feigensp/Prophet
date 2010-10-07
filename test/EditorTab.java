package test;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class EditorTab extends JPanel {
	
	File file;
	JScrollPane scrollPane;
	JTextPane textPane;
	Search searchField;
	
	/**
	 * Create the panel.
	 */
	public EditorTab(File f) {
		file=f;
		textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane);
		searchField = new Search(textPane);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		add(searchField, BorderLayout.SOUTH);
		searchField.setVisible(false);
	}

}
