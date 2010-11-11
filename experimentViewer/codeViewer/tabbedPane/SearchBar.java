package experimentViewer.codeViewer.tabbedPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

/**
 * This class adds a JTextPane to a searchbar which is created. With this
 * searchBar the user can search through the text in the JTextPane User
 * Settings: - case sensitive - search backward/forward
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

@SuppressWarnings("serial")
public class SearchBar extends JPanel {

	static final Color HILIT_COLOR_GRAY = Color.LIGHT_GRAY;
	static final Color HILIT_COLOR_YELLOW = Color.YELLOW;
	static final boolean SEARCH_FORWARD = true;
	static final boolean SEARCH_BACKWARD = false;

	// JTextPane for the SearchBar
	JTextPane textPane = null;
	// Vector with startPositions of the word in the JTextPane
	private Vector<Integer> positions = new Vector<Integer>();
	private int wordLength = 0;
	// is the positions-vector fully build up?
	private boolean hasPositions = false;

	Highlighter hilit = new DefaultHighlighter();;
	Highlighter.HighlightPainter painterGray = new DefaultHighlighter.DefaultHighlightPainter(
			HILIT_COLOR_GRAY);
	Highlighter.HighlightPainter painterYellow = new DefaultHighlighter.DefaultHighlightPainter(
			HILIT_COLOR_YELLOW);

	private JTextField searchField = new JTextField();
	private JButton forwardButton = new JButton("Abw\u00E4rts");
	private JButton backwardButton = new JButton("Aufw\u00E4rts");
	private JLabel hideButton = new JLabel("X");
	private JLabel infoLabel = new JLabel("");
	private JCheckBox caseSensitivityCheckbox = new JCheckBox(
			"Gro\u00DF-/Kleinschreibung");

	/**
	 * creates GUI ands sets Listener
	 * 
	 * @param textPane
	 *            textPane for the SearchBar
	 */
	public SearchBar(JTextPane textPane) {
		this.textPane = textPane;
		// create gui
		JLabel searchFieldLabel = new JLabel("Suchen:");
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		hideButton.setHorizontalAlignment(SwingConstants.CENTER);
		hideButton.setPreferredSize(new Dimension(20, 20));
		hideButton.setMinimumSize(new Dimension(20, 20));
		hideButton.setMaximumSize(new Dimension(20, 20));
		hideButton.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		searchField.setColumns(10);
		add(hideButton);
		add(searchFieldLabel);
		add(searchField);
		add(forwardButton);
		add(backwardButton);
		add(caseSensitivityCheckbox);
		add(infoLabel);
		// set Listener
		hideButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				setVisible(false);
			}
		});
		searchField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
					find(SEARCH_FORWARD);
				} else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}
			}
		});
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			private void inputChanged() {
				cancelSearch();
//				find(SEARCH_FORWARD);
			}

			public void changedUpdate(DocumentEvent arg0) {
				inputChanged();
			}

			public void insertUpdate(DocumentEvent arg0) {
				inputChanged();
			}

			public void removeUpdate(DocumentEvent arg0) {
				inputChanged();
			}
		});
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			private void inputChanged() {
				cancelSearch();
				find(SEARCH_FORWARD);
			}

			public void changedUpdate(DocumentEvent arg0) {
				inputChanged();
			}

			public void insertUpdate(DocumentEvent arg0) {
				inputChanged();
			}

			public void removeUpdate(DocumentEvent arg0) {
				inputChanged();
			}
		});
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				find(SEARCH_FORWARD);
			}
		});
		backwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				find(SEARCH_BACKWARD);
			}
		});
		caseSensitivityCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cancelSearch();
				find(SEARCH_FORWARD);
			}
		});
		// various settings
		textPane.setHighlighter(hilit);
	}

	/**
	 * This method searchs through the text if necessary it build up the vector
	 * with the positions of the word it could search for- oder backward
	 * regulated by the caret position
	 * 
	 * @param forward
	 *            true if it search forward, backward if false
	 */
	public void find(boolean forward) {
		//build up positions vector if necessary
		if (!hasPositions) {
			String text;
			String word;
			//case sensitivity
			try {
				Document doc = textPane.getDocument();
				text = caseSensitivityCheckbox.isSelected() ? doc.getText(0,
						doc.getLength()) : doc.getText(0, doc.getLength())
						.toLowerCase();
			} catch (BadLocationException e) {
				e.printStackTrace();
				return;
			}
			word = caseSensitivityCheckbox.isSelected() ? searchField.getText()
					: searchField.getText().toLowerCase();
			wordLength = word.length();
			if (wordLength == 0) {
				return;
			}
			//build up
			positions.clear();
			int pos = text.indexOf(word);
			while (pos != -1) {
				positions.add(pos);
				pos = text.indexOf(word, pos + 1);
			}
			if (!positions.isEmpty()) {
				textPane.setCaretPosition(0);
			}
			hasPositions = true;
		}
		if (positions.isEmpty()) {
			searchField.setForeground(Color.WHITE);
			searchField.setBackground(Color.RED);
		} else {
			int pos;
			if (forward) {
				int i = 0;
				while (i < positions.size()) {
					if (positions.get(i) > textPane.getCaretPosition()) {
						break;
					}
					i++;
				}
				if (i == positions.size()) {
					infoLabel
							.setText("Textende erreicht. Die Suche wurde vom Textanfang neu gestartet.");
					i = 0;
				} else {
					infoLabel.setText("");
				}
				pos = positions.get(i);
			} else {
				int i = positions.size() - 1;
				while (i >= 0) {
					if (positions.get(i) < textPane.getCaretPosition()) {
						break;
					}
					i--;
				}
				if (i == -1) {
					infoLabel
							.setText("Textanfang erreicht. Die Suche wurde vom Textende neu gestartet.");
					i = positions.size() - 1;
				} else {
					infoLabel.setText("");
				}
				pos = positions.get(i);
			}
			//ink words, set cursor
			showHighlights(positions, pos, wordLength);
			textPane.setCaretPosition(pos);
		}
	}

	/**
	 * shows the highlights in the color scheme
	 * @param positions positions of the words
	 * @param currPos current found search word
	 * @param length  length of the words
	 */
	public void showHighlights(Vector<Integer> positions, int currPos,
			int length) {
		hilit.removeAllHighlights();
		for (int i = 0; i < positions.size(); i++) {
			try {
				int pos = positions.get(i);
				if (pos == currPos) {
					hilit.addHighlight(pos, pos + length, painterYellow);
				} else {
					hilit.addHighlight(pos, pos + length, painterGray);
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * settings to abort the search
	 */
	public void cancelSearch() {
		searchField.setForeground(Color.BLACK);
		searchField.setBackground(Color.WHITE);
		hilit.removeAllHighlights();
		positions.clear();
		hasPositions = false;
		wordLength = 0;
	}

	/**
	 * shows the searchbar
	 */
	@Override
	public void setVisible(boolean show) {
		if (show == this.isVisible()) {
			return;
		}
		super.setVisible(show);
		if (show) {
			find(SEARCH_FORWARD);
		} else {
			cancelSearch();
			textPane.grabFocus();
		}
	}

	/**
	 * Grabs the focus
	 */
	public void grabFocus() {
		searchField.grabFocus();
	}
}
