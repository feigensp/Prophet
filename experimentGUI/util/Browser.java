package experimentGUI.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class Browser extends JFrame implements HyperlinkListener {

	private JEditorPane htmlPane;

	public Browser(String initialURL) {
		super("Simple Browser");

		try {
			htmlPane = new JEditorPane(initialURL);
			htmlPane.setEditable(false);
			htmlPane.addHyperlinkListener(this);
			JScrollPane scrollPane = new JScrollPane(htmlPane);
			getContentPane().add(scrollPane, BorderLayout.CENTER);
		} catch (IOException ioe) {
			warnUser("Can't build HTML pane for " + initialURL + ": " + ioe);
		}

		Dimension screenSize = getToolkit().getScreenSize();
		int width = screenSize.width * 8 / 10;
		int height = screenSize.height * 8 / 10;
		setBounds(width / 8, height / 8, width, height);
		setVisible(true);
	}

	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				htmlPane.setPage(event.getURL());
			} catch (IOException ioe) {
				warnUser("Can't follow link to " + event.getURL().toExternalForm() + ": " + ioe);
			}
		}
	}

	private void warnUser(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
