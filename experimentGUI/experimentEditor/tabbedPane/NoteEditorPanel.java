package experimentGUI.experimentEditor.tabbedPane;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;

@SuppressWarnings("serial")
public class NoteEditorPanel extends ExperimentEditorTab {
	private HashMap<QuestionTreeNode,RSyntaxTextArea> editAreas = new HashMap<QuestionTreeNode,RSyntaxTextArea>();
	RSyntaxTextArea editArea;
	RTextScrollPane scrollPane;
	private QuestionTreeNode selected;
	
	private DocumentListener myDocumentListener = new DocumentListener() {
		public void myChange() {
			if (selected!=null) {
				QuestionTreeNode node = selected.getAddAttribute("notes");
				node.setValue(editArea.getText());
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
	};
	
	public NoteEditorPanel() {
		setLayout(new BorderLayout());
	}
	
	public void activate(QuestionTreeNode selected) {
		this.selected=selected;
		this.removeAll();
		this.updateUI();
		if (selected!=null) {
			editArea = editAreas.get(selected);
			if (editArea==null) {
				editArea = new RSyntaxTextArea();
				editArea.setText(selected.getAddAttribute("notes").getValue());
				editArea.getDocument().addDocumentListener(myDocumentListener);
				editAreas.put(selected, editArea);
			}
			scrollPane = new RTextScrollPane(editArea);
			add(scrollPane, BorderLayout.CENTER);
		}
	}
}
