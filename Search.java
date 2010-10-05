
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;


public class Search extends JTextField implements KeyListener{
	private String text;
	private String word;
	int caret;
	int lastSearchPosition;
	JTextArea textarea = null;
	
	final static Color  HILIT_COLOR = Color.LIGHT_GRAY;
	
	Highlighter hilit;
	Highlighter.HighlightPainter painter;
	
	
	/*
	 * Konstruktoren für mehrere Komponenten möglich - nur extra erstellen und in Methoden dann überprüfen
	 * 
	 */
	public Search(JTextArea textarea) {
		super();
		addKeyListener(this);
		this.textarea = textarea;
		this.word = "";
		lastSearchPosition = -1;
		
		hilit = new DefaultHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
		textarea.setHighlighter(hilit);
	}
	
	public Search(JTextArea textarea, int col) {
		super(col);
		addKeyListener(this);
		this.textarea = textarea;
		this.word = "";
		lastSearchPosition = -1;
		
		hilit = new DefaultHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
		textarea.setHighlighter(hilit);		
	}

	@Override
	public void keyReleased(KeyEvent ke) {	
		if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println("enter");
			getNextPos();
		} else if(ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.out.println("escape");
			setText("");
			cancelSearch();
		} else {
			System.out.println("sonstiges");
			cancelSearch();
			setWord(this.getText());
			getNextPos();
		}		
	}
	
	/*
	 * Gibt true zurück wenn es derzeit kein andere Suchwort gab, false wenn eins überschrieben wurde
	 */
	public boolean setWord(String word) {
		this.word = word;
		return word.equals("") ? true : false;
	}
	
	public boolean getNextPos() {
		text = textarea.getText();
		caret = textarea.getCaretPosition();
		boolean ret = false;
		
		hilit.removeAllHighlights();
		
		//hinter dem Caret anfangen zu suchen, dem letzten Suchresultat, oder am Anfang	
		int pos = text.indexOf(word, Math.max(Math.max(caret, lastSearchPosition), 0));
		if(pos != -1) {
			ret = true;
			lastSearchPosition = pos+1;
			try {
				textarea.setCaretPosition(pos);
				hilit.addHighlight(pos, pos+word.length(), painter);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		//wenn dort nicht vorhanden nochmal den Rest durchsuchen
		} else {
			pos = text.indexOf(word);
			if(pos != -1) {
				ret = true;
				lastSearchPosition = pos+1;
				try {
					textarea.setCaretPosition(pos);
					hilit.addHighlight(pos, pos+word.length(), painter);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(!ret) {
			setBackground(Color.red);
		} else {
			setBackground(Color.white);
		}
		return ret;
	}
	
	public void cancelSearch() {
		setBackground(Color.white);
		hilit.removeAllHighlights();
		lastSearchPosition = -1;
		word = "";
	}
	

	
	@Override
	public void keyPressed(KeyEvent ke) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
}
