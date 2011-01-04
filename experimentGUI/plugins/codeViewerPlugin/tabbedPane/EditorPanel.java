package experimentGUI.plugins.codeViewerPlugin.tabbedPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public class EditorPanel extends JPanel {
	private String filePath;
	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;
	
	private boolean changed = false;

	/**
	 * Create the panel.
	 */
	public EditorPanel(File file, String path, QuestionTreeNode selected) {
		this.filePath=path;
		if (selected==null) {
			selected=new QuestionTreeNode();
		}
		RSyntaxDocument doc = new RSyntaxDocument("text/plain");
		try {
			byte[] buffer = new byte[(int) file.length()];
		    FileInputStream fileStream = new FileInputStream(file);
		    fileStream.read(buffer);
		    doc.insertString(0, new String(buffer), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		textArea = new RSyntaxTextArea(doc);
		textArea.setFont(new Font("monospaced", Font.PLAIN, 12));
		scrollPane = new RTextScrollPane(textArea);		
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);	
		
		for (CodeViewerPluginInterface plugin : CodeViewerPluginList.getPlugins()) {
			plugin.onEditorPanelCreate(this);
		}
		
		boolean editable = Boolean.parseBoolean(selected.getAttributeValue(CodeViewer.KEY_EDITABLE));
		textArea.setEditable(editable);
		if (editable) {
			textArea.getDocument().addDocumentListener(new DocumentListener() {			
				private void changeOccured() {
					changed=true;
				}
				public void changedUpdate(DocumentEvent arg0) {
					changeOccured();
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					changeOccured();
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					changeOccured();
				}			
			});
		}
	}	
	public void grabFocus() {
		textArea.grabFocus();
	}	
	public RSyntaxTextArea getTextArea() {
		return textArea;
	}
	public RTextScrollPane getScrollPane() {
		return scrollPane;
	}
	public String getFilePath() {
		return filePath;
	}
	public boolean isChanged() {
		return changed;
	}
}
