package experimentGUI.experimentEditor.tabbedPane;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import experimentGUI.experimentEditor.tabbedPane.contentEditorToolBar.ContentEditorToolBar;
import experimentGUI.util.ModifiedRSyntaxTextArea;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.searchBar.SearchBar;
import experimentGUI.util.searchBar.SearchBarListener;

@SuppressWarnings("serial")
public class ContentEditorPanel extends ExperimentEditorTab {
	private ContentEditorToolBar toolBar;
	private SearchBar searchBar;
	private HashMap<QuestionTreeNode, RSyntaxTextArea> editAreas = new HashMap<QuestionTreeNode,RSyntaxTextArea>();
	private RSyntaxTextArea editArea;
	private RTextScrollPane scrollPane;
	private QuestionTreeNode selected;
	
	private DocumentListener myDocumentListener = new DocumentListener() {
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
	};
	private KeyListener myKeyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent ke) {
			if (ke.isShiftDown() && ke.getKeyCode() == KeyEvent.VK_ENTER) {
				editArea.replaceSelection("<br>");
			}
		}
	};
	
	public ContentEditorPanel() {
		setLayout(new BorderLayout());
	}
	
	public void activate(QuestionTreeNode selected) {
		this.selected=selected;
		this.removeAll();
		this.updateUI();
		if (selected!=null) {
			editArea = editAreas.get(selected);
			if (editArea==null) {
				editArea = new ModifiedRSyntaxTextArea();
				editArea.setText(selected.getValue());
				editArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
				editArea.getDocument().addDocumentListener(myDocumentListener);			
				editArea.addKeyListener(myKeyListener);
				editAreas.put(selected, editArea);
			}
			toolBar = new ContentEditorToolBar(editArea);		
			add(toolBar, BorderLayout.NORTH);
			scrollPane = new RTextScrollPane(editArea);
			add(scrollPane, BorderLayout.CENTER);
			searchBar = new SearchBar(editArea);
			searchBar.setVisible(false);
			editArea.addKeyListener(new SearchBarListener(searchBar));
			add(searchBar, BorderLayout.SOUTH);
		}
	}
}
