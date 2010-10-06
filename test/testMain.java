package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class testMain implements ActionListener{
	Search s;

	public testMain() {
		JFrame frame = new JFrame("test");
		JTextArea textarea = new JTextArea();
		textarea.setText("hallo\ndu\nda\nd\nu\nda\nda\ndudu\nda\nDu\nda");
		s = new Search(textarea);
		frame.getContentPane();
		
		JButton button = new JButton("Zeige!");
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		frame.add(textarea, BorderLayout.CENTER);
		frame.add(s, BorderLayout.NORTH);
		
		frame.setSize(800, 800);
		button.addActionListener(this);
		
		

		frame.setVisible(true);
		
	}

	public static void main(String args[]) {
		testMain test = new testMain();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		s.visible();
		
	}

}
