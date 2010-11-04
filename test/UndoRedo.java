package test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UndoRedo implements DocumentListener, KeyListener{

	JTextPane textPane;
	ArrayList<Change> changes;
	String lastText;
	
	int pos;
	boolean undoRedoChange;
	
	public UndoRedo(JTextPane textPane) {
		changes = new ArrayList<Change>();
		pos = 0;
		undoRedoChange = false;
		this.textPane = textPane;		
		textPane.getDocument().addDocumentListener(this);
		textPane.addKeyListener(this);
		
		lastText = textPane.getText();
	}
	
	public void undo() {
		if(pos-1 >= 0) {
			undoRedoChange = true;
			pos--;
			Change change = changes.get(pos);
			String text = textPane.getText();
			switch(change.getType()) {
			case Change.INSERT:
				text = text.substring(0, change.getOffset()) + text.substring(change.getOffset()+change.getChange().length());
				break;
			case Change.REMOVE:
				text = text.substring(0, change.getOffset()) + change.getChange() + text.substring(change.getOffset());
				break;
			}
			textPane.setText(text);
			undoRedoChange = false;
		}
	}
	
	public void redo() {
		if(pos+1 <= changes.size()) {
			undoRedoChange = true;
			Change change = changes.get(pos++);
			String text = textPane.getText();
			switch(change.getType()) {
			case Change.INSERT:
				text = text.substring(0, change.getOffset()) + change.getChange() + text.substring(change.getOffset());
				break;
			case Change.REMOVE:
				text = text.substring(0, change.getOffset()) + text.substring(change.getOffset() + change.getChange().length());
				break;
			}
			textPane.setText(text);	
			undoRedoChange = false;			
		}
	}	
	
	@Override
	public void changedUpdate(DocumentEvent de) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void insertUpdate(DocumentEvent de) {
		if(!undoRedoChange) {
			Change change = new Change();
			change.setOffset(de.getOffset());
			change.setChange(textPane.getText().substring(de.getOffset(), de.getOffset()+de.getLength()));
			change.setType(Change.INSERT);
			pos++;
			if(pos >= changes.size()) {
				for(int i=pos; i < changes.size(); i++) {
					changes.remove(i);				
				}
			}
			changes.add(change);
			
			lastText = textPane.getText();
		}
	}
	@Override
	public void removeUpdate(DocumentEvent de) {
		if(!undoRedoChange) {
			Change change = new Change();
			change.setOffset(de.getOffset());
			change.setChange(lastText.substring(de.getOffset(), de.getOffset()+de.getLength()));
			change.setType(Change.REMOVE);
			pos++;
			if(pos >= changes.size()) {
				for(int i=pos; i < changes.size(); i++) {
					changes.remove(i);				
				}
			}
			changes.add(change);
			
			lastText = textPane.getText();
		}
	}


	@Override
	public void keyPressed(KeyEvent e) {
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
			undo();
		} else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y) {
			redo();
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
