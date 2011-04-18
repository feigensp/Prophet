package experimentGUI.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTab;
import experimentGUI.util.ModifiedRSyntaxTextArea;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

@SuppressWarnings("serial")
public class NoteEditorPanel extends ExperimentEditorTab {
	private HashMap<QuestionTreeNode,JScrollPane> scrollPanes;
	private HashMap<QuestionTreeNode, RSyntaxTextArea> editAreas = new HashMap<QuestionTreeNode,RSyntaxTextArea>();
	private QuestionTreeNode selected;
	public final static String KEY_NOTES = "notes";

	public NoteEditorPanel() {
		setLayout(new BorderLayout());
		this.setOpaque(false);
		scrollPanes = new HashMap<QuestionTreeNode,JScrollPane>();
	}
	
	public void activate(final QuestionTreeNode s) {
		selected=s;
		this.removeAll();
		this.updateUI();
		if (s!=null) {
			JScrollPane scrollPane = scrollPanes.get(selected);
			if (scrollPane==null) {
				JPanel editPanel = new JPanel();
				editPanel.setLayout(new BorderLayout());
				
				RSyntaxDocument doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE);
				try {
					doc.insertString(0, s.getAddAttribute(KEY_NOTES).getValue(), null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				final RSyntaxTextArea editArea = new ModifiedRSyntaxTextArea(doc);
				editAreas.put(s, editArea);
//				editArea.getDocument().addDocumentListener(new DocumentListener() {
//					public void myChange() {
//						if (selected!=null) {
//							QuestionTreeNode node = selected.getAddAttribute(KEY_NOTES);
//							node.setValue(editArea.getText());
//						}
//					}
//					@Override
//					public void changedUpdate(DocumentEvent arg0) {
//						myChange();		
//					}
//					
//					@Override
//					public void insertUpdate(DocumentEvent arg0) {
//						myChange();		
//					}
//					
//					@Override
//					public void removeUpdate(DocumentEvent arg0) {
//						myChange();
//					}
//				});
				editArea.setLineWrap(true);
				editPanel.add(editArea, BorderLayout.CENTER);
				
				scrollPane = new JScrollPane(editPanel);
				scrollPanes.put(s, scrollPane);
			}
			add(scrollPane, BorderLayout.CENTER);
		}
	}

	@Override
	public void save() {
		if (selected!=null) {
			RSyntaxTextArea editArea = editAreas.get(selected);
			if (editArea!=null) {
				QuestionTreeNode node = selected.getAddAttribute(KEY_NOTES);
				node.setValue(editArea.getText());
			}
		}
	}
}
