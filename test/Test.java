package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Test extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	
	QuestionView qv;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test frame = new Test();
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
	public Test() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 646, 353);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnReihe = new JButton("reihe");
		panel.add(btnReihe);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(5);
		
		JButton btnLabel = new JButton("Label");
		panel.add(btnLabel);
		
		JButton btnRadio = new JButton("Radio");
		panel.add(btnRadio);
		
		JButton btnText = new JButton("Text");
		panel.add(btnText);
		
		JButton btnTable = new JButton("Table");
		panel.add(btnTable);
		
		JButton btnStrut = new JButton("strut");
		panel.add(btnStrut);
		
		qv = new QuestionView();
		contentPane.add(new JScrollPane(qv), BorderLayout.CENTER);

		//Table
		btnTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Table t = new Table();
				t.addComponent(new JLabel("2, 2"), 2, 2);
				t.addComponent(new JLabel("1, 1"), 1, 1);
				qv.getRow(Integer.parseInt(textField.getText())).addTable(t);
			}
		});
		//TextField
		btnText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				qv.getRow(Integer.parseInt(textField.getText())).addTextField(7);
			}
		});
		//Radio
		btnRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = "hallo du da";
				qv.getRow(Integer.parseInt(textField.getText())).addRadioButtons(text.split(" "), false);
			}
		});
		//Reihe
		btnReihe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				qv.addRow();
			}
		});
		//Label
		btnLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				qv.getRow(Integer.parseInt(textField.getText())).addLabel("TestLabel - Reihe: " + textField.getText());
			}
		});
		//Strut
		btnStrut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				qv.getRow(Integer.parseInt(textField.getText())).addDivider(20, false);
			}
		});
	}

}
