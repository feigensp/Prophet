package experimentEditor.tabbedPane.contentEditor;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import util.QuestionTreeNode;
import experimentEditor.tabbedPane.ExperimentEditorTab;
import experimentEditor.tabbedPane.contentEditor.ToolBar.ContentEditorToolBar;

@SuppressWarnings("serial")
public class ContentEditorPanel extends ExperimentEditorTab {
	private ContentEditorToolBar toolBar;
	private EditArea editArea;
	private QuestionTreeNode selected;
	
	public ContentEditorPanel() {
		setLayout(new BorderLayout());
		editArea = new EditArea();
		editArea.getDocument().addDocumentListener(new DocumentListener() {
			public void myChange() {
				if (selected!=null) {
					selected.setValue(editArea.getText());
				}
			}
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				myChange();		
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				myChange();		
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				myChange();
			}
		});
		
		editArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.isShiftDown() && ke.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						editArea.getDocument().insertString(editArea.getCaretPosition(), "<br>\n", null);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		toolBar = new ContentEditorToolBar(editArea);
		add(toolBar, BorderLayout.NORTH);
		add(new JScrollPane(editArea), BorderLayout.CENTER);
	}
	
	public void activate(QuestionTreeNode selected) {
		this.selected=selected;
		if (selected!=null) {
			editArea.setText(selected.getValue());
			editArea.setEditable(true);
			toolBar.setEnabled(true);
		} else {
			editArea.setText("");
			editArea.setEditable(false);
			toolBar.setEnabled(false);
		}
	}
}
