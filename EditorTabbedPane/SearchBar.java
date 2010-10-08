/**
 * Diese Klasse erbt von JPanel - in diesem sind dann alle Elemente einer Suchleiste enthalten.
 * Es kann ihr im Konstruktor ein JTextArea oder JTextPane übergeben werden.
 * In diesem Kann dann gesucht werden - Treffer werden dabei in dem übergebenen Objekt Farbig markiert.
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package EditorTabbedPane;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.event.MouseAdapter;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.border.TitledBorder;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class SearchBar extends JPanel {
	private String text;	//Text der Suchsucht wird
	private String word;	//Wort das gesucht wird
	JTextArea textarea = null;
	JTextPane textpane = null;
	private Vector<Integer> v; // Vector für die Positionen der Vorkommen
	private int lastPos; // Position des aktuellen Vorkommen

	static final Color HILIT_COLOR_GRAY = Color.LIGHT_GRAY;
	static final Color HILIT_COLOR_YELLOW = Color.YELLOW;
	static final boolean SEARCH_FORWARD = true;
	static final boolean SEARCH_BACKWARD = false;

	Highlighter hilit;
	Highlighter.HighlightPainter painterGray;
	Highlighter.HighlightPainter painterYellow;

	private JTextField searchField;
	private JButton forwardButton;
	private JButton backwardButton;
	private JLabel hideButton;
	private JLabel infoLabel;
	private JCheckBox caseSensitivityCheckbox;

	/**
	 * Konstruktor wenn eine JTextPane durchsucht werden soll
	 * @wbp.parser.constructor
	 */
	public SearchBar(JTextPane textpane) {
		this.textpane = textpane;
		initialise();
		textpane.setHighlighter(hilit);
	}

	/**
	 * Konstruktor wenn eine JTextArea durchsucht werden soll
	 */
	public SearchBar(JTextArea textarea) {
		this.textarea = textarea;
		initialise();
		textarea.setHighlighter(hilit);
	}

	/**
	 * Variablen-/ und Objektinitialisierung, sowie setzen des Layouts
	 */
	public void initialise() {
		this.word = "";
		lastPos = -1;
		v = new Vector<Integer>();
		JLabel searchFieldLabel = new JLabel("Suchen:");
		searchField = new JTextField();
		searchField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
					if (setWord(searchField.getText())) {
						getPos(SEARCH_FORWARD);
					}
				}
				if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}
			}
		});
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			public void searchwordChanged() {
				cancelSearch();
				if (setWord(searchField.getText())) {
					getPos(SEARCH_FORWARD);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				searchwordChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				searchwordChanged();
			}
		});
		searchField.setColumns(10);
		forwardButton = new JButton("Abw\u00E4rts");
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				getPos(SEARCH_FORWARD);
			}
		});
		backwardButton = new JButton("Aufw\u00E4rts");
		backwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				getPos(SEARCH_BACKWARD);
			}
		});
		infoLabel = new JLabel("");
		caseSensitivityCheckbox = new JCheckBox("Gro\u00DF-/Kleinschreibung");
		caseSensitivityCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cancelSearch();
				setWord(searchField.getText());
				getPos(SEARCH_FORWARD);
			}
		});

		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
				hideButton = new JLabel("X");
				hideButton.setHorizontalAlignment(SwingConstants.CENTER);
				hideButton.setPreferredSize(new Dimension(20, 20));
				hideButton.setMinimumSize(new Dimension(20, 20));
				hideButton.setMaximumSize(new Dimension(20, 20));
				add(hideButton);
				hideButton.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				hideButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent arg0) {
						setVisible(false);
					}
				});
		add(searchFieldLabel);
		add(searchField);
		add(forwardButton);
		add(backwardButton);
		add(caseSensitivityCheckbox);
		add(infoLabel);

		hilit = new DefaultHighlighter();
		painterGray = new DefaultHighlighter.DefaultHighlightPainter(
				HILIT_COLOR_GRAY);
		painterYellow = new DefaultHighlighter.DefaultHighlightPainter(
				HILIT_COLOR_YELLOW);
	}

	/**
	 * Allgemeine Suchmethode Baut einen Vector auf, der als Index der
	 * gefundenen Werte dient. Alle Ergebnisse werden grau Hinterlegt
	 * eingefärbt, das aktuelle gelb. LastSearchPosition gibt dabei anfangs das
	 * letzte aktuelle Ergebnis an und muss zum Ende aktualisiert werden, damit
	 * es das aktuelle angibt
	 * 
	 * @param mod boolean der angibt ob vorwärts (true) oder rückwärts (false)
	 * gesucht werden soll
	 * 
	 * @return true wenn was gefunden, sonst false
	 */
	public boolean getPos(boolean forward) {
		if (word.equals("")) {
			cancelSearch();
			return false;
		}
		// Unterscheidung Groß- und Kleinschreibung
		if (textpane == null) {
			text = caseSensitivityCheckbox.isSelected() ? textarea.getText()
					: textarea.getText().toLowerCase();
		} else {
			try {
				Document doc = textpane.getDocument();
				text = caseSensitivityCheckbox.isSelected() ? doc.getText(0,
						doc.getLength()) : doc.getText(0, doc.getLength())
						.toLowerCase();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		word = caseSensitivityCheckbox.isSelected() ? word : word.toLowerCase();
		// liste aller vorkommen aufbauen (nur wenn nötig)
		if (lastPos == -1) {
			v.removeAllElements();
			int pos = text.indexOf(word);
			int i = 0;
			while (pos != -1) {
				i++;
				v.add(pos);
				pos = text.indexOf(word, pos + 1);
			}
		}
		if (!v.isEmpty()) {
			// Vorwärtseinstellung
			if (forward) {
				if (lastPos >= v.size() - 1) {
					infoLabel
							.setText("Textende erreicht. Die Suche wurde vom Textanfang neu gestartet.");
				} else {
					infoLabel.setText("");
				}
				lastPos = (lastPos >= v.size() - 1) ? 0 : lastPos + 1;
			}
			// Rückwärtseinstellung
			if (!forward) {
				if (lastPos <= 0) {
					infoLabel
							.setText("Textanfang erreicht. Die Suche wurde vom Textende neu gestartet.");
				} else {
					infoLabel.setText("");
				}
				lastPos = lastPos <= 0 ? v.size() - 1 : lastPos - 1;
			}
			// Ergebnisse einfärben und Cursor setzen
			showHighlights();
			searchField.setForeground(Color.BLACK);
			searchField.setBackground(Color.WHITE);
			if (textpane == null) {
				textarea.setCaretPosition(v.get(lastPos));
			} else {
				textpane.setCaretPosition(v.get(lastPos));
			}
			return true;
		}
		searchField.setForeground(Color.WHITE);
		searchField.setBackground(Color.RED);
		return false;
	}

	/**
	 * Lässt alle Suchergebnissfarbhinterlegungen im Farbschema anzeigen
	 */
	public void showHighlights() {
		hilit.removeAllHighlights();
		for (int j = 0; j < v.size(); j++) {
			try {
				if (j == lastPos) {
					hilit.addHighlight(v.get(j), v.get(j) + word.length(),
							painterYellow);
				} else {
					hilit.addHighlight(v.get(j), v.get(j) + word.length(),
							painterGray);
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Lässt alle Suchergebnissfarbhinterlegungen verschwinden
	 */
	public void hideHighlights() {
		hilit.removeAllHighlights();
	}

	/**
	 * Setzt das derzeitige Suchwort
	 * 
	 * @param word Das Wort was nun gesucht werden soll
	 * 
	 * @return true wenn es ein echtes Wort ist, false wenn es ein Leerstring
	 * ist
	 */
	public boolean setWord(String word) {
		this.word = word;
		return word.equals("") ? false : true;
	}

	/**
	 * Führt alle nötigen Vorgänge aus um eine Suche abzubrechen
	 */
	public void cancelSearch() {
		searchField.setForeground(Color.BLACK);
		searchField.setBackground(Color.WHITE);
		hideHighlights();
		setWord("");
		lastPos = -1;
	}

	/**
	 * Zeigt diese Klasse optisch an
	 */
	@Override
	public void setVisible(boolean show) {
		if (show == this.isVisible()) {
			return;
		}
		super.setVisible(show);
		if (show) {
			lastPos = 0;
			infoLabel.setText("");
			showHighlights();
		} else {
			hideHighlights();
			if (textarea == null) {
				textpane.grabFocus();
			} else {
				textarea.grabFocus();
			}
		}
	}

	/**
	 * Gibt dem Suchfeld den Fokus
	 */
	public void grabFocus() {
		searchField.grabFocus();
	}
}
