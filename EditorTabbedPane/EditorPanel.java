package EditorTabbedPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
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

    private HighlightedDocument document;

	/**
	 * Create the panel.
	 */
	public EditorPanel(File f, boolean searchable, boolean editable) {
		document = new HighlightedDocument();
		document.setHighlightStyle(HighlightedDocument.JAVA_STYLE);
		textPane = new JTextPane(document);
		textPane.setFont(new Font("monospaced", Font.PLAIN, 12));
		if(searchable) {
			textPane.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_F)) {
						searchBar.setVisible(true);
						searchBar.grabFocus();
					}
				}
			});
		}
		textPane.setEditable(editable);	
		initialise(f);
	}
	
	public EditorPanel(File f) {
		document = new HighlightedDocument();
		document.setHighlightStyle(HighlightedDocument.JAVA_STYLE);
		textPane = new JTextPane(document);
		textPane.setFont(new Font("monospaced", Font.PLAIN, 12));
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_F)) {
					searchBar.setVisible(true);
					searchBar.grabFocus();
				}
			}
		});
		textPane.setEditable(false);
		initialise(f);
	}
	
	public void initialise(File f) {
		file = f;
		setLayout(new BorderLayout());
		try {
			FileReader fr = new FileReader(file);
			textPane.read(fr, null);
			fr.close();
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
}
