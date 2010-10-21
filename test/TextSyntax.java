package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import EditorTabbedPane.LineNumbers;

import com.Ostermiller.Syntax.HighlightedDocument;

public class TextSyntax extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TextSyntax frame = new TextSyntax();
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
	public TextSyntax() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		HighlightedDocument document = new HighlightedDocument();
		document.setHighlightStyle(HighlightedDocument.JAVA_STYLE);
		JTextPane textPane = new JTextPane(document);
		textPane.setFont(new Font("monospaced", Font.PLAIN, 12));
		textPane.setText("public static void main()");
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("bla");
			}
		});
		textPane.setEditable(true);

		LineNumbers lineNumbers = new LineNumbers(textPane);
		JScrollPane scrollPane = new JScrollPane(textPane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setRowHeaderView(lineNumbers);
		contentPane.add(scrollPane, BorderLayout.CENTER);
	}

}
