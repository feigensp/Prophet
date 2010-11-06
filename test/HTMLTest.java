package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class HTMLTest extends JFrame {

	private JPanel contentPane;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HTMLTest frame = new HTMLTest();
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
	public HTMLTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JButton button = new JButton("New button");
		contentPane.add(button, BorderLayout.SOUTH);
		
		textPane = new JTextPane();
		textPane.setEditorKit(new HTMLEditorKit());
		textPane.setEditable(false);
		textPane.setText("<form>" + 
				"frage 1 test<br><input id=\"vorname\" type=\"text\" size=\"5\">" +
				"<br><br><input name ='nextQuestion' type='submit' value='Weiter'>" + 
				"<form>");
		contentPane.add(textPane, BorderLayout.CENTER);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				HTMLDocument doc = (HTMLDocument) textPane.getDocument();
				Element ele = doc.getElement("vorname");
				AttributeSet attributes = ele.getAttributes();
				Enumeration attname = attributes.getAttributeNames();
				while(attname.hasMoreElements()) {
					Object e = attname.nextElement();
					System.out.println(e + ":" + attributes.getAttribute(e));
				}
				try {
					doc.setOuterHTML(ele, "<input id=\"vorname\" type=\"text\" size=\"60\" value=\"hey\">");
				} catch (BadLocationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
