package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class RTextAreaRowHeaderTest extends JFrame {

	private JPanel contentPane;
	private JPanel drawPanel;
	private RTextArea textArea;
	int lastX=0;
	int lastY=0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RTextAreaRowHeaderTest frame = new RTextAreaRowHeaderTest();
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
	public RTextAreaRowHeaderTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		textArea = new RTextArea();
		RTextScrollPane scrollPane = new RTextScrollPane(textArea);

		Component lineNumbers = scrollPane.getRowHeader().getComponent(0);
		drawPanel = new JPanel();
		JPanel rowHeader = new JPanel();
		rowHeader.setLayout(new BorderLayout());
		rowHeader.add(lineNumbers, BorderLayout.WEST);
		rowHeader.add(drawPanel, BorderLayout.CENTER);
		lastX = textArea.getFontMetrics(textArea.getFont()).getHeight();
		rowHeader.setPreferredSize(new Dimension(50, rowHeader.getHeight()));
		scrollPane.setRowHeaderView(rowHeader);

		JButton button = new JButton("press me");

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					drawRect(textArea, 0, 1, Color.RED);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				Graphics g = drawPanel.getGraphics();
//				g.drawLine(0, lastX*51, 10, lastX*51);
//				g.drawLine(0, lastX*50, 10, lastX*50);
			}
		});
		contentPane.setLayout(new BorderLayout());
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(button, BorderLayout.SOUTH);
	}

	private void drawRect(RTextArea textArea, int offset, int length, Color color) throws BadLocationException {
		final int rectWidth = 5;
		int startLineNumber = textArea.getLineOfOffset(offset);
		int endLineNumber = textArea.getLineOfOffset(offset + length);
		int rowHeight = textArea.getFontMetrics(textArea.getFont()).getHeight();
		Graphics g = drawPanel.getGraphics();
		g.setColor(color);
		g.fillRect(10, rowHeight*startLineNumber, rectWidth, rowHeight*(endLineNumber-startLineNumber+1));
	}

}
