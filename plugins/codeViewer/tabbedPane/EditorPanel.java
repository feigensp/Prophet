package plugins.codeViewer.tabbedPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPluginList;
import util.QuestionTreeNode;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel {
	private File file;
	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;

	/**
	 * Create the panel.
	 */
	public EditorPanel(File f, QuestionTreeNode selected) {
		file=f;
		if (selected==null) {
			selected=new QuestionTreeNode();
		}

		textArea = new RSyntaxTextArea();
		textArea.setFont(new Font("monospaced", Font.PLAIN, 12));
		try {
			byte[] buffer = new byte[(int) file.length()];
		    FileInputStream fileStream = new FileInputStream(file);
		    fileStream.read(buffer);
		    textArea.setText(new String(buffer));
		    textArea.setCaretPosition(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		scrollPane = new RTextScrollPane(textArea);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		
		for (CodeViewerPlugin plugin : CodeViewerPluginList.getPlugins()) {
			plugin.onEditorPanelCreate(selected, this);
		}
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
}
