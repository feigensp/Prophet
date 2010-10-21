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
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import java.awt.Dimension;

public class Test extends JFrame {

	private JPanel contentPane;
	
	QuestionView qv;
	private JTextField zeileTextField;
	private JTextField spalteTextField;
	private JTextField labelTextField;
	private JTextField textFieldTextField;
	private JEditorPane radioButtonTextPane;
	private JCheckBox chckbxHorizontal;
	private JTextPane tableTextPane;
	private JTextField teilerTextField;
	private JCheckBox chckbxVertikal;

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
		setBounds(100, 100, 1073, 433);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		qv = new QuestionView();
		contentPane.add(new JScrollPane(qv), BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		panel_1.add(panel);
		
		JLabel lblZeile = new JLabel("Zeile");
		panel.add(lblZeile);
		
		zeileTextField = new JTextField();
		panel.add(zeileTextField);
		zeileTextField.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		
		JLabel lblSpalte = new JLabel("Spalte:");
		panel_2.add(lblSpalte);
		
		spalteTextField = new JTextField();
		panel_2.add(spalteTextField);
		spalteTextField.setColumns(10);
		
		JButton btnElementLschen = new JButton("Element l\u00F6schen");
		panel_2.add(btnElementLschen);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		
		JLabel lblLabel = new JLabel("Label");
		panel_3.add(lblLabel);
		
		labelTextField = new JTextField();
		panel_3.add(labelTextField);
		labelTextField.setColumns(10);
		
		JButton createLabel = new JButton("Neu");
		panel_3.add(createLabel);
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		
		JLabel lblTextfeld = new JLabel("Textfeld");
		panel_4.add(lblTextfeld);
		
		textFieldTextField = new JTextField();
		panel_4.add(textFieldTextField);
		textFieldTextField.setColumns(10);
		
		JButton createTextField = new JButton("Neu");
		panel_4.add(createTextField);
		
		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);
		
		JLabel RadioButton = new JLabel("RadioButton");
		panel_5.add(RadioButton);
		
		radioButtonTextPane = new JEditorPane();
		panel_5.add(radioButtonTextPane);
		
		chckbxHorizontal = new JCheckBox("Vertikal");
		panel_5.add(chckbxHorizontal);
		
		JButton createRadioButton = new JButton("Neu");
		panel_5.add(createRadioButton);
		
		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6);
		
		JLabel Tabelle = new JLabel("Tabelle:");
		panel_6.add(Tabelle);
		
		tableTextPane = new JTextPane();
		tableTextPane.setPreferredSize(new Dimension(150, 80));
		panel_6.add(new JScrollPane(tableTextPane));
		
		JButton createTable = new JButton("Neu");
		panel_6.add(createTable);
		
		JPanel panel_7 = new JPanel();
		panel_1.add(panel_7);
		
		JLabel lblTeiler = new JLabel("Teiler:");
		panel_7.add(lblTeiler);
		
		teilerTextField = new JTextField();
		panel_7.add(teilerTextField);
		teilerTextField.setColumns(10);
		
		chckbxVertikal = new JCheckBox("Vertikal");
		panel_7.add(chckbxVertikal);
		
		JButton createTeiler = new JButton("Neu");
		panel_7.add(createTeiler);
		
		//Neus Label
		createLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int zeile = Integer.parseInt(zeileTextField.getText());
				while(qv.getRowCount()<=zeile) {
					qv.addRow();					
				}
				qv.getRow(zeile).addLabel(labelTextField.getText());				
			}
		});
		//Neues TextField
		createTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int zeile = Integer.parseInt(zeileTextField.getText());
				while(qv.getRowCount()<=zeile) {
					qv.addRow();					
				}
				qv.getRow(zeile).addTextField(Integer.parseInt(textFieldTextField.getText()));
			}
		});
		//neuer RadioButton
		createRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int zeile = Integer.parseInt(zeileTextField.getText());
				while(qv.getRowCount()<=zeile) {
					qv.addRow();					
				}
				boolean checked = chckbxHorizontal.getSelectedObjects() == null;
				qv.getRow(zeile).addRadioButtons(radioButtonTextPane.getText().split("\n"), checked);
			}
		});
		//neue Tabelle
		createTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int zeile = Integer.parseInt(zeileTextField.getText());
				while(qv.getRowCount()<=zeile) {
					qv.addRow();					
				}
				String[] zeilen = tableTextPane.getText().split("\n");
				String[] reihe;
				Table t = new Table(8, 8);
				for(int i = 0; i < zeilen.length; i++) {
					reihe = zeilen[i].split(" ");
					for(int j=0; j<reihe.length;j++) {
						System.out.println("hinzufügen");
						t.addComponent(new JLabel(reihe[j]), j, i);
					}
				}
				qv.getRow(zeile).addTable(t);
			}
		});
		//Teiler
		createTeiler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int zeile = Integer.parseInt(zeileTextField.getText());
				while(qv.getRowCount()<=zeile) {
					qv.addRow();					
				}
				boolean checked = chckbxVertikal.getSelectedObjects() == null;
				qv.getRow(zeile).addDivider(20, checked);
			}
		});
		//Componente löschen
		btnElementLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int zeile = Integer.parseInt(zeileTextField.getText());
				if(zeile < qv.getRowCount()) {
					System.out.println(qv.removeComponent(Integer.parseInt(spalteTextField.getText()), Integer.parseInt(zeileTextField.getText())));
				}
			}
		});
	}

}
