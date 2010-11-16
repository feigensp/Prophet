package plugins.codeViewer.tabbedPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPluginList;
import plugins.codeViewer.codeViewerPlugins.LineNumbers.LineNumbers;
import plugins.codeViewer.codeViewerPlugins.SearchBar.SearchBar;
import util.QuestionTreeNode;

import com.Ostermiller.Syntax.HighlightedDocument;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel {
	private File file;
	private JScrollPane scrollPane;
	private JTextPane textPane;

	/**
	 * Create the panel.
	 */
	public EditorPanel(File f, QuestionTreeNode selected) {
		file=f;
		if (selected==null) {
			selected=new QuestionTreeNode();
		}

		HighlightedDocument document = null;
		document = new HighlightedDocument();
		document.setHighlightStyle(HighlightedDocument.JAVA_STYLE);
		textPane = new JTextPane(document);
		textPane.setFont(new Font("monospaced", Font.PLAIN, 12));
		try {
			byte[] buffer = new byte[(int) file.length()];
		    FileInputStream fileStream = new FileInputStream(file);
		    fileStream.read(buffer);
		    textPane.setText(new String(buffer));
		    textPane.setCaretPosition(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.add(textPane);
		scrollPane = new JScrollPane(textPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(textPane.getFontMetrics(textPane.getFont()).getHeight());
		
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
		textPane.grabFocus();
	}	
	public JTextPane getTextPane() {
		return textPane;
	}
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
}
