package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class testMain{
	
	
	public testMain() {
		JFrame frame = new JFrame("test");
		JTextArea textarea = new JTextArea();
		Search s = new Search(textarea);
		frame.getContentPane();
		frame.add(textarea, BorderLayout.CENTER);
		frame.add(s, BorderLayout.NORTH);
		
		frame.setSize(400, 400);		
		
		

		frame.setVisible(true);
		
	}
	
	public static void main(String args[]) {
		testMain test = new testMain();
	}

}
