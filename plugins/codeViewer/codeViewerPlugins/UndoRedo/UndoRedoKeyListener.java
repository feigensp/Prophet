package plugins.codeViewer.codeViewerPlugins.UndoRedo;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.undo.UndoManager;

public class UndoRedoKeyListener extends KeyAdapter {
	UndoManager undoManager;
	
	public UndoRedoKeyListener(UndoManager undoManager) {
		this.undoManager=undoManager;
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_Z) {
			if (undoManager.canUndo()) {
				undoManager.undo();
			}
		} else if(arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_Y) {
			if (undoManager.canRedo()) {
				undoManager.redo();
			}
		}
	}
}
