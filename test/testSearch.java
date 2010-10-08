package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class testSearch extends JFrame {

	private JPanel contentPane;
    //private HighlightedDocument document = new HighlightedDocument();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testSearch frame = new testSearch();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public testSearch() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		//document.setHighlightStyle(HighlightedDocument.JAVA_STYLE);
		JTextPane textPane = new JTextPane(/*document*/);

		JScrollPane sp = new JScrollPane(textPane);
		contentPane.add(sp, BorderLayout.CENTER);
		Search s = new Search(textPane);
		contentPane.add(s, BorderLayout.SOUTH);
	}

}
