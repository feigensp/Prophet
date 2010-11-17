package test;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

import jsyntaxpane.DefaultSyntaxKit;

public class EclipseHighlightTest {

	public static void main(String[] args) {
		new EclipseHighlightTest();
	}

	public EclipseHighlightTest() {
		JFrame f = new JFrame("bla");
		f.setLayout(new BorderLayout());
		DefaultSyntaxKit.initKit();
		JTextPane codeEditor = new JTextPane(new SyntaxDocument());
		JScrollPane scrPane = new JScrollPane(codeEditor);
		f.add(scrPane, BorderLayout.CENTER);

		f.setSize(800, 600);
		f.setVisible(true);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}
}