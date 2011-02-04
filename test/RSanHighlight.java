package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaHighlighter;

public class RSanHighlight extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private RSyntaxTextArea textPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RSanHighlight frame = new RSanHighlight();
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
	public RSanHighlight() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		textPane = new RSyntaxTextArea();
		contentPane.add(textPane, BorderLayout.CENTER);
		
		JButton button = new JButton("New button");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RSyntaxTextAreaHighlighter hilit = new RSyntaxTextAreaHighlighter();
//				DefaultHighlightPainter painterGray = new DefaultHighlighter.DefaultHighlightPainter(
//						Color.GRAY);
				DefaultHighlightPainter painterYellow = new DefaultHighlighter.DefaultHighlightPainter(
						Color.YELLOW);
				textPane.setHighlighter(hilit);
				textPane.setText("                       ");
				try {
					hilit.addHighlight(1, 2,
							painterYellow);
					hilit.addHighlight(4, 5,
							painterYellow);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button, BorderLayout.SOUTH);
	}

}
