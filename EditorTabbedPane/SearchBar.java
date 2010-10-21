/**
 * Diese Klasse erbt von JPanel - in diesem sind dann alle Elemente einer Suchleiste enthalten.
 * Es kann ihr im Konstruktor ein JTextArea oder JTextPane übergeben werden.
 * In diesem Kann dann gesucht werden - Treffer werden dabei in dem übergebenen Objekt Farbig markiert.
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package EditorTabbedPane;

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

@SuppressWarnings("serial")
public class SearchBar extends JPanel {
	JTextPane textPane = null;
	private Vector<Integer> positions = new Vector<Integer>();
	private int wordLength = 0;
	private boolean hasPositions = false;	

	static final Color HILIT_COLOR_GRAY = Color.LIGHT_GRAY;
	static final Color HILIT_COLOR_YELLOW = Color.YELLOW;
	static final boolean SEARCH_FORWARD = true;
	static final boolean SEARCH_BACKWARD = false;

	Highlighter hilit = new DefaultHighlighter();;
	Highlighter.HighlightPainter painterGray = new DefaultHighlighter.DefaultHighlightPainter(
			HILIT_COLOR_GRAY);
	Highlighter.HighlightPainter painterYellow= new DefaultHighlighter.DefaultHighlightPainter(
			HILIT_COLOR_YELLOW);

	private JTextField searchField = new JTextField();
	private JButton forwardButton = new JButton("Abw\u00E4rts");
	private JButton backwardButton = new JButton("Aufw\u00E4rts");
	private JLabel hideButton = new JLabel("X");
	private JLabel infoLabel = new JLabel("");
	private JCheckBox caseSensitivityCheckbox = new JCheckBox("Gro\u00DF-/Kleinschreibung");

	/**
	 * Konstruktor
	 */
	public SearchBar(JTextPane textPane) {
		this.textPane = textPane;
		
		JLabel searchFieldLabel = new JLabel("Suchen:");
		searchField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
					find(SEARCH_FORWARD);
				} else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}
			}
		});
		
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			public void inputChanged() {
				cancelSearch();
				find(SEARCH_FORWARD);
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				inputChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				inputChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				inputChanged();
			}
		});
		searchField.setColumns(10);
		
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

		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		hideButton.setHorizontalAlignment(SwingConstants.CENTER);
		hideButton.setPreferredSize(new Dimension(20, 20));
		hideButton.setMinimumSize(new Dimension(20, 20));
		hideButton.setMaximumSize(new Dimension(20, 20));
		hideButton.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		hideButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				setVisible(false);
			}
		});
		
		add(hideButton);
		add(searchFieldLabel);
		add(searchField);
		add(forwardButton);
		add(backwardButton);
		add(caseSensitivityCheckbox);
		add(infoLabel);

		textPane.setHighlighter(hilit);
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
	public void find(boolean forward) {
		// liste aller vorkommen aufbauen (nur wenn nötig)
		if (!hasPositions) {
			String text;
			String word;

			// Unterscheidung Groß- und Kleinschreibung
			try {
				Document doc = textPane.getDocument();
				text = caseSensitivityCheckbox.isSelected() ? doc.getText(0,
						doc.getLength()) : doc.getText(0, doc.getLength())
						.toLowerCase();
			} catch (BadLocationException e) {
				e.printStackTrace();
				return;
			}
			word = caseSensitivityCheckbox.isSelected() ? searchField.getText() : searchField.getText().toLowerCase();
			wordLength = word.length();
			if (wordLength==0) {
				return;
			}
			positions.clear();
			int pos = text.indexOf(word);
			while (pos != -1) {
				positions.add(pos);
				pos = text.indexOf(word, pos + 1);
			}
			hasPositions=true;
		}
		if (positions.isEmpty()) {
			searchField.setForeground(Color.WHITE);
			searchField.setBackground(Color.RED);
		} else {
			int pos;
			// Vorwärtseinstellung
			if (forward) {
				int i=0;
				while (i<positions.size()) {
					if (positions.get(i)>textPane.getCaretPosition()) {
						break;
					}
					i++;
				}
				if (i==positions.size()) {
					infoLabel.setText("Textende erreicht. Die Suche wurde vom Textanfang neu gestartet.");
					i=0;
				} else {
					infoLabel.setText("");
				}
				pos = positions.get(i);
			} else {
				int i=positions.size()-1;
				while (i>=0) {
					if (positions.get(i)<textPane.getCaretPosition()) {
						break;
					}
					i--;
				}
				if (i==-1) {
					infoLabel.setText("Textanfang erreicht. Die Suche wurde vom Textende neu gestartet.");
					i=positions.size()-1;
				} else {
					infoLabel.setText("");
				}
				pos = positions.get(i);
			}
			// Ergebnisse einfärben und Cursor setzen
			showHighlights(positions, pos, wordLength);
			textPane.setCaretPosition(pos);
		}
	}

	/**
	 * Lässt alle Suchergebnissfarbhinterlegungen im Farbschema anzeigen
	 */
	public void showHighlights(Vector<Integer> positions, int currPos, int length) {
		hilit.removeAllHighlights();
		for (int i = 0; i < positions.size(); i++) {
			try {
				int pos = positions.get(i);
				if (pos==currPos) {
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
	 * Führt alle nötigen Vorgänge aus um eine Suche abzubrechen
	 */
	public void cancelSearch() {
		searchField.setForeground(Color.BLACK);
		searchField.setBackground(Color.WHITE);
		hilit.removeAllHighlights();
		
		positions.clear();
		hasPositions = false;
		wordLength=0;
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
			find(SEARCH_FORWARD);
		} else {
			cancelSearch();
			textPane.grabFocus();
		}
	}

	/**
	 * Gibt dem Suchfeld den Fokus
	 */
	public void grabFocus() {
		searchField.grabFocus();
	}
}
