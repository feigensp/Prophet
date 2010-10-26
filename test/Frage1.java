package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;

public class Frage1 {

	private JFrame frame;
	private JPanel panel;
	private JLabel lblIchBinNun;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frage1 window = new Frage1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frage1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 659, 482);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		frame.getContentPane().add(panel_1, BorderLayout.NORTH);
		
		panel = new JPanel();
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.setMaximumSize(new Dimension(100, 20000));
		panel_1.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		String text = "Ziel dieser Aufgabe soll es sein, einen grunds\u00E4tzlichen \u00DCberblick \u00FCber die Programmiermethoden zu erhalten.";
		String[] textsplit = text.split(" ");
		for (String t : textsplit) {
			JLabel lblT = new JLabel(t+" ");
			panel.add(lblT);
		}
		frame.pack();
	}
}
