package EditorTabbedPane;

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

import com.Ostermiller.Syntax.HighlightedDocument;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel {
	private File file;
	private JScrollPane scrollPane;
	private JTextPane textPane;
	private SearchBar searchBar;
	
	boolean searchable;

	/**
	 * Create the panel.
	 */
	public EditorPanel(File f) {
		file=f;
		this.searchable=false;
		
		HighlightedDocument document = null;
		document = new HighlightedDocument();
		document.setHighlightStyle(HighlightedDocument.JAVA_STYLE);
		textPane = new JTextPane(document);
		textPane.setFont(new Font("monospaced", Font.PLAIN, 12));
		
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_F)) {
					searchBar.setVisible(searchable);
					searchBar.grabFocus();
				}
			}
		});
		setLayout(new BorderLayout());
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
		add(scrollPane, BorderLayout.CENTER);

		LineNumbers lineNumbers = new LineNumbers(textPane);
		scrollPane.setRowHeaderView(lineNumbers);

		searchBar = new SearchBar(textPane);
		add(searchBar, BorderLayout.SOUTH);
		searchBar.setVisible(false);	
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
	
	public void setEditable(boolean editable) {
		textPane.setEditable(editable);
	}
	public boolean isEditable() {
		return textPane.isEditable();
	}
	public void setSearchable(boolean s) {
		searchable=s;
	}
}
