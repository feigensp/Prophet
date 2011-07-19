package experimentGUI.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JPanel;
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

@SuppressWarnings("serial")
public class ContentEditorPanel extends ExperimentEditorTab {
	private HashMap<QuestionTreeNode, JPanel> editPanels = new HashMap<QuestionTreeNode,JPanel>();
	private HashMap<QuestionTreeNode, RSyntaxTextArea> editAreas = new HashMap<QuestionTreeNode,RSyntaxTextArea>();
	private HashMap<QuestionTreeNode, SearchBar> searchBars = new HashMap<QuestionTreeNode,SearchBar>();
	private QuestionTreeNode selected;
	
	public ContentEditorPanel() {
		setLayout(new BorderLayout());
		this.setOpaque(false);
	}
	
	public void activate(QuestionTreeNode s) {
		selected=s;
		this.removeAll();
		this.updateUI();
		if (selected!=null) {
			JPanel editPanel = editPanels.get(s);
			if (editPanel==null) {
				editPanel = new JPanel();
				editPanel.setLayout(new BorderLayout());
				
				RSyntaxDocument doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_HTML);
				try {
					doc.insertString(0, s.getValue(), null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				final RSyntaxTextArea editArea = new ModifiedRSyntaxTextArea(doc);
				editAreas.put(s, editArea);
				
				editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);		
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
				searchBars.put(selected, searchBar);
				
				editPanel.add(searchBar, BorderLayout.SOUTH);				
				ExperimentEditorTabbedPane.recursiveSetOpaque(editPanel);
				editPanels.put(s, editPanel);
			}
			add(editPanel, BorderLayout.CENTER);
		}
	}

	@Override
	public void save() {
		if (selected!=null) {
			RSyntaxTextArea editArea = editAreas.get(selected);
			if (editArea!=null) {
				selected.setValue(editArea.getText());
			}
		}
	}
	public void search() {
		SearchBar searchBar = searchBars.get(selected);
		if (searchBar!=null) {
			searchBar.setVisible(true);
			searchBar.grabFocus();
		}
	}
}
