package test;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class Search extends JPanel implements KeyListener, ActionListener, ChangeListener {
	private String text;
	private String word;
	int caret;
	int lastSearchPosition;
	JTextArea textarea = null;
	private Vector<Integer> v; //Vector für die Positionen der Vorkommen
	private int lastPos; //Position des aktuellen Vorkommen
	private boolean checked;

	final Color HILIT_COLOR_GRAY = Color.LIGHT_GRAY;
	final Color HILIT_COLOR_YELLOW = Color.YELLOW;

	Highlighter hilit;
	Highlighter.HighlightPainter painterGray;
	Highlighter.HighlightPainter painterYellow;

	private JTextField search_textfield_word;
	private JButton search_btn_down;
	private JButton search_btn_up;
	private JLabel search_lbl_close;
	private JLabel search_lbl_text;
	private JCheckBox search_chkbx_upperlower;

	/*
	 * Konstruktoren für mehrere Komponenten möglich - nur extra erstellen und
	 * in Methoden dann überprüfen
	 */
	public Search(JTextArea textarea) {
		super();
		this.textarea = textarea;
		this.word = "";
		lastPos = -1;
		v = new Vector<Integer>();
		checked = false;

		search_lbl_close = new JLabel("x");
		JLabel search_lbl_suchen = new JLabel("Suchen:");
		search_textfield_word = new JTextField();
		search_textfield_word.setColumns(10);
		search_btn_down = new JButton("Abw\u00E4rts");
		search_btn_up = new JButton("Aufw\u00E4rts");
		search_lbl_text = new JLabel("");
		search_chkbx_upperlower = new JCheckBox("Gro\u00DF-/Kleinschreibung");

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(search_lbl_close)
					.addGap(18)
					.addComponent(search_lbl_suchen)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(search_textfield_word, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(search_btn_down)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(search_btn_up)
					.addGap(6)
					.addComponent(search_chkbx_upperlower)
					.addGap(6)
					.addComponent(search_lbl_text)
					.addContainerGap(55, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(search_lbl_close))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(7)
							.addComponent(search_btn_down))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(7)
							.addComponent(search_btn_up))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(search_chkbx_upperlower))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(search_lbl_text))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(8)
							.addComponent(search_textfield_word, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(search_lbl_suchen)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

		search_textfield_word.addKeyListener(this);
		search_btn_up.addActionListener(this);
		search_btn_down.addActionListener(this);
		search_chkbx_upperlower.addChangeListener(this);

		lastSearchPosition = -1;
		hilit = new DefaultHighlighter();
		painterGray = new DefaultHighlighter.DefaultHighlightPainter(
				HILIT_COLOR_GRAY);
		painterYellow = new DefaultHighlighter.DefaultHighlightPainter(
				HILIT_COLOR_YELLOW);
		textarea.setHighlighter(hilit);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() != KeyEvent.VK_ENTER) {
			cancelSearch();
			setWord(search_textfield_word.getText());
		}
		getPrevPos(10);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// Vorwärtssuche auslösen
		if (ae.getSource() == search_btn_down) {
			getPrevPos(10);
		}
		// Rückwärtssuche auslösen
		if (ae.getSource() == search_btn_up) {
			getPrevPos(20);
		}
	}	

	@Override
	public void stateChanged(ChangeEvent arg0) {
		checked = search_chkbx_upperlower.isSelected();		
	}

	/*
	 * Gibt true zurück wenn es derzeit kein andere Suchwort gab, false wenn
	 * eins überschrieben wurde
	 */
	public boolean setWord(String word) {
		this.word = word;
		return word.equals("") ? true : false;
	}

	/*
	 * Allgemeine Suchmethode Baut einen Vector auf, der als Index der
	 * gefundenen Werte dient. Alle Ergebnisse werden grau Hinterlegt
	 * eingefärbt, das aktuelle gelb. LastSearchPosition gibt dabei anfangs das
	 * letzte aktuelle Ergebnis an und muss zum Ende aktualisiert werden, damit
	 * es das aktuelle angibt
	 * 
	 * @param mod Integer, welcher den Modus angibt. Die erste Zahl gib an ob
	 * Vorwärts oder Rückwärts gesucht wird (1/2) und die Zweite ob Groß-
	 * Kleinschreibung vernachlässigt oder berücksichtigt wird (1/2)
	 * 
	 * @return true wenn was gefunden, sonst false
	 */
	public boolean getPrevPos(int mod) {		
		// liste aller vorkommen aufbauen (nur wenn nötig)
		if (word.equals("")) {
			cancelSearch();
			return false;
		}
		text = textarea.getText();
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
			//Vorwärtseinstellung
			if (mod < 20) {
				if(lastPos >= v.size()-1) {
					search_lbl_text.setText("Textende erreicht. Die Suche wurde vom Textanfang neu gestartet.");
				} else {
					search_lbl_text.setText("");
				}
				lastPos = (lastPos >= v.size()-1) ? 0 : lastPos+1;
			}
			//Rückwärtseinstellung
			if (mod >= 20) {
				if(lastPos <= 0) {
					search_lbl_text.setText("Textanfang erreicht. Die Suche wurde vom Textende neu gestartet.");
				} else {
					search_lbl_text.setText("");
				}
				lastPos = lastPos <= 0 ? v.size()-1 : lastPos-1;
			}
			// Ergebnisse einfärben und Cursor setzen
			hilit.removeAllHighlights();
			for (int j = 0; j < v.size(); j++) {
				try {
					if (j == lastPos) {
						hilit.addHighlight(v.get(j), v.get(j)
								+ word.length(), painterYellow);
					} else {
						hilit.addHighlight(v.get(j), v.get(j) + word.length(),
								painterGray);
					}
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			search_textfield_word.setForeground(Color.BLACK);
			search_textfield_word.setBackground(Color.WHITE);
			textarea.setCaretPosition(v.get(lastPos));
			return true;
		}
		search_textfield_word.setForeground(Color.WHITE);
		search_textfield_word.setBackground(Color.RED);
		return false;
	}


	public void cancelSearch() {
		search_textfield_word.setForeground(Color.BLACK);
		search_textfield_word.setBackground(Color.WHITE);
		hilit.removeAllHighlights();
		word = "";
		lastPos=-1;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
