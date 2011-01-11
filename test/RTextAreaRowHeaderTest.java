package test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class RTextAreaRowHeaderTest extends JFrame {

	private JPanel contentPane;
	private JPanel drawPanel;
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
		RTextArea textArea = new RTextArea();
		RTextScrollPane scrollPane = new RTextScrollPane(textArea);

		Component lineNumbers = scrollPane.getRowHeader().getComponent(0);
		drawPanel = new JPanel();
		JPanel rowHeader = new JPanel();
		rowHeader.setLayout(new BorderLayout());
		rowHeader.add(lineNumbers, BorderLayout.WEST);
		rowHeader.add(drawPanel, BorderLayout.CENTER);
		
		lastX = textArea.getFontMetrics(textArea.getFont()).getHeight();
		System.out.println(scrollPane.getRowHeader().getAlignmentY());
		System.out.println(scrollPane.getRowHeader().getBaseline(0, 0));
		System.out.println(scrollPane.getRowHeader().getY());
		System.out.println(scrollPane.getRowHeader().getViewPosition());
		
		scrollPane.setRowHeaderView(rowHeader);

		JButton button = new JButton("press me");

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Graphics g = drawPanel.getGraphics();
				g.drawLine(0, lastX*51, 10, lastX*51);
				g.drawLine(0, lastX*50, 10, lastX*50);
			}
		});
		contentPane.setLayout(new BorderLayout());
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(button, BorderLayout.SOUTH);
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) drawPanel.getGraphics();
		g2.drawRect(5, 5, 10, 10);
	}

}
