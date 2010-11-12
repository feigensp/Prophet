package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import plugins.codeViewer.plugins.UndoRedo.UndoRedo;

public class UndoRedoTest extends JFrame {

	private JPanel contentPane;
	private static JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UndoRedoTest frame = new UndoRedoTest();
					frame.setVisible(true);
					setSettings();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UndoRedoTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		textPane = new JTextPane();
		contentPane.add(textPane, BorderLayout.CENTER);
	}
	
	private static void setSettings() {
		UndoRedo ur = new UndoRedo(textPane);
	}

}
