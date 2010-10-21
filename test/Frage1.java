package test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Frage1 {

	private JFrame frame;
	private JTextArea myTextArea;
	private JLabel myLabel;
	private JSplitPane splitPane_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JButton btnGibInhaltAus;

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
		
		splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.5);
		frame.getContentPane().add(splitPane_1, BorderLayout.CENTER);
		
		panel_2 = new JPanel();
		splitPane_1.setLeftComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		panel_3 = new JPanel();
		splitPane_1.setRightComponent(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		myLabel = new JLabel();
		myLabel.setVerticalAlignment(SwingConstants.TOP);
		myLabel.setText("<html><form>\r\nDies soll ein Testtext sein, welcher zu Testzwecken Texte ausf\u00FChrt.<br>\r\nWenn dies ein Texttest w\u00E4re, so w\u00FCrde er zu Textzwecken Teste ausf\u00FChren. Da Texte aber keine Teste ausf\u00FChren und Texte Testtexte nicht ausgef\u00FChrt werden k\u00F6nnen ist ein Testtext, sowie Texttest \u00FCberfl\u00FCssig...<br>\r\n<br>\r\n<table>\r\n<tr><td>Name:</td><td><input name=nameTextfield></td></tr>\r\n<tr><td>Kontonummer:</td><td><input name=kontoTextfield></td></tr>\r\n</table><br>\r\n<br>\r\nDanke f\u00FCr Ihre Kontonummer, ihr Geld wird so schnell wie m\u00F6glich abgehoben.<br>\r\n<br>\r\n<input type=\"submit\" value=\"Abschicken\">\r\n</form>\r\n</html>");
		panel_3.add(myLabel);
		
		myTextArea = new JTextArea();
		myTextArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				myLabel.setText(myTextArea.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				myLabel.setText(myTextArea.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				myLabel.setText(myTextArea.getText());
			}
		});
		JScrollPane scrollPane = new JScrollPane(myTextArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel_2.add(scrollPane);
		myTextArea.setText("<html><form>\nDies soll ein Testtext sein, welcher zu Testzwecken Texte ausf\u00FChrt.<br>\nWenn dies ein Texttest w\u00E4re, so w\u00FCrde er zu Textzwecken Teste ausf\u00FChren. Da Texte aber keine Teste ausf\u00FChren und Texte Testtexte nicht ausgef\u00FChrt werden k\u00F6nnen ist ein Testtext, sowie Texttest \u00FCberfl\u00FCssig...<br>\n<br>\n<table>\n<tr><td>Name:</td><td><input name=nameTextfield></td></tr>\n<tr><td>Kontonummer:</td><td><input name=kontoTextfield></td></tr>\n</table><br>\n<br>\nDanke f\u00FCr Ihre Kontonummer, ihr Geld wird so schnell wie m\u00F6glich abgehoben.<br>\n<br>\n<input type=\"submit\" value=\"Abschicken\">\n</form>\n</html>");
		
		btnGibInhaltAus = new JButton("Gib Inhalt aus.");
		btnGibInhaltAus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0; i<myLabel.getComponentCount(); i++) {
					System.out.println(myLabel.getComponent(i).getBounds());
				}
				System.out.println();
			}
		});
		frame.getContentPane().add(btnGibInhaltAus, BorderLayout.NORTH);
		
//		String text = "Ziel dieser Aufgabe soll es sein, einen grunds\u00E4tzlichen \u00DCberblick \u00FCber die Programmiermethoden zu erhalten.";
//		String[] textsplit = text.split(" ");
//		for (String t : textsplit) {
//			JLabel lblT = new JLabel(t+" ");
//			frame.getContentPane().add(lblT);
//		}
	}
}
