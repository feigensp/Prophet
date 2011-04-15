package experimentGUI.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTab;
import experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar.ContentEditorToolBar;
import experimentGUI.util.ModifiedRSyntaxTextArea;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.searchBar.SearchBar;
import experimentGUI.util.searchBar.SearchBarListener;

@SuppressWarnings("serial")
public class ContentEditorPanel extends ExperimentEditorTab {
	private HashMap<QuestionTreeNode, JPanel> editPanels = new HashMap<QuestionTreeNode,JPanel>();
	
	
	public ContentEditorPanel() {
		setLayout(new BorderLayout());
		this.setOpaque(false);
	}
	
	public void activate(final QuestionTreeNode selected) {
		this.removeAll();
		this.updateUI();
		if (selected!=null) {
			JPanel editPanel = editPanels.get(selected);
			if (editPanel==null) {
				editPanel = new JPanel();
				editPanel.setLayout(new BorderLayout());
				
				RSyntaxDocument doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_HTML);
				try {
					doc.insertString(0, selected.getValue(), null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				final RSyntaxTextArea editArea = new ModifiedRSyntaxTextArea(doc);
				
				editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
				editArea.getDocument().addDocumentListener(new DocumentListener() {
					public void myChange() {
						selected.setValue(editArea.getText());
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
							editArea.replaceSelection("<br>");
						}
					}
				});
				
				ContentEditorToolBar toolBar = new ContentEditorToolBar(editArea);		
				editPanel.add(toolBar, BorderLayout.NORTH);
				RTextScrollPane scrollPane = new RTextScrollPane(editArea);
				editArea.setLineWrap(true);
				editPanel.add(scrollPane, BorderLayout.CENTER);
				SearchBar searchBar = new SearchBar(editArea);
				searchBar.setVisible(false);
				editArea.addKeyListener(new SearchBarListener(searchBar));
				editPanel.add(searchBar, BorderLayout.SOUTH);				
				ExperimentEditorTabbedPane.recursiveSetOpaque(editPanel);
				editPanels.put(selected, editPanel);
			}
			add(editPanel, BorderLayout.CENTER);
		}
	}
}
