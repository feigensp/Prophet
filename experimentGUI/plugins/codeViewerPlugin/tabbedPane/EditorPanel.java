package experimentGUI.plugins.codeViewerPlugin.tabbedPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginInterface;
import experimentGUI.plugins.codeViewerPlugin.CodeViewerPluginList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public class EditorPanel extends JPanel {
	private File file;
	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;
	private boolean changed;

	/**
	 * Create the panel.
	 */
	public EditorPanel(File f, QuestionTreeNode selected) {
		changed = false;
		file=f;
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
		

		textArea.getDocument().addDocumentListener(new DocumentListener() {			
			private void changeOccured() {
				changed = true;
				textArea.getDocument().removeDocumentListener(this);
				System.out.println("changed");
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
	
	public EditorPanel(File f) {
		this(f, null);
	}

	public File getFile() {
		return file;
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
	public boolean isChanged() {
		return changed;
	}
}
