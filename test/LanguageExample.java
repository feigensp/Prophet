package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class LanguageExample extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblnamekonto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Look and Feel setzen
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LanguageExample frame = new LanguageExample();
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
	public LanguageExample() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 905, 532);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("1. Beispiel", null, panel, null);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setText("Dies soll ein Testtext sein, welcher zu Testzwecken Texte ausführt.\n" +
				"Wenn dies ein Texttest wäre, so würde er zu Textzwecken Teste ausführen. Da Texte aber keine Teste ausführen und Texte Testtexte nicht ausgeführt werden können ist ein Testtext, sowie Texttest überflüssig...\n\n" +
				"Name:\t\t<textfield name=nameTextfield>\n" +
				"Kontonummer:\t<Textfield name=kontoTextfield>\n\n" +
				"Danke für Ihre Kontonummer, ihr Geld wird so schnell wie möglich abgehoben.");
		panel.add(new JScrollPane(editorPane));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JPanel panel_2 = new JPanel();
		panel_2.setMaximumSize(new Dimension(32767, 20));
		panel_2.setPreferredSize(new Dimension(10, 35));
		FlowLayout flowLayout_6 = (FlowLayout) panel_2.getLayout();
		flowLayout_6.setAlignment(FlowLayout.LEFT);
		panel_1.add(panel_2);
		
		JLabel lblDiesSollEin = new JLabel("Dies soll ein Testtext sein, welcher zu Testzwecken Texte ausf\u00FChrt.");
		panel_2.add(lblDiesSollEin);
		
		JPanel panel_3 = new JPanel();
		String[] texte = "Wenn dies ein Texttest wäre, so würde er zu Textzwecken Teste ausführen. Da Texte aber keine Teste ausführen und Texte Testtexte nicht ausgeführt werden können ist ein Testtext, sowie Texttest überflüssig...".split(" ");
		for(int i=0; i<texte.length; i++) {
			panel_3.add(new JLabel(texte[i]));
		}
		panel_3.setMaximumSize(new Dimension(32767, 45));
		FlowLayout flowLayout_5 = (FlowLayout) panel_3.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		panel_1.add(panel_3);
		
		JPanel panel_14 = new JPanel();
		panel_14.setMaximumSize(new Dimension(32767, 20));
		panel_1.add(panel_14);
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		
		JPanel panel_7 = new JPanel();
		panel_7.setMaximumSize(new Dimension(50, 35));
		panel_7.setPreferredSize(new Dimension(50, 35));
		FlowLayout flowLayout = (FlowLayout) panel_7.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_4.add(panel_7);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalTextPosition(SwingConstants.LEFT);
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		panel_7.add(lblName);
		
		JPanel struct1Panel = new JPanel();
		struct1Panel.setPreferredSize(new Dimension(40, 10));
		struct1Panel.setMaximumSize(new Dimension(40, 10));
		panel_4.add(struct1Panel);
		
		JPanel panel_11 = new JPanel();
		panel_11.setMaximumSize(new Dimension(32767, 35));
		FlowLayout flowLayout_2 = (FlowLayout) panel_11.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_4.add(panel_11);
		
		textField = new JTextField();
		panel_11.add(textField);
		textField.setColumns(10);
		
		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.X_AXIS));
		
		JPanel panel_9 = new JPanel();
		panel_9.setMaximumSize(new Dimension(75, 35));
		FlowLayout flowLayout_1 = (FlowLayout) panel_9.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_5.add(panel_9);
		
		JLabel lblKontonummer = new JLabel("Kontonummer:");
		panel_9.add(lblKontonummer);
		
		JPanel struct2Panel = new JPanel();
		struct2Panel.setMaximumSize(new Dimension(10, 10));
		panel_5.add(struct2Panel);
		
		JPanel panel_12 = new JPanel();
		panel_12.setMaximumSize(new Dimension(32767, 35));
		FlowLayout flowLayout_3 = (FlowLayout) panel_12.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panel_5.add(panel_12);
		
		textField_1 = new JTextField();
		panel_12.add(textField_1);
		textField_1.setColumns(10);
		
		JPanel panel_13 = new JPanel();
		panel_13.setMaximumSize(new Dimension(32767, 20));
		panel_1.add(panel_13);
		
		JPanel panel_6 = new JPanel();
		panel_6.setMaximumSize(new Dimension(32767, 35));
		FlowLayout flowLayout_4 = (FlowLayout) panel_6.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		panel_1.add(panel_6);
		
		JLabel lblDankeFrIhre = new JLabel("Danke f\u00FCr ihre Kontonummer, ihr Geld wird so schnell wie m\u00F6glich abgehoben.");
		panel_6.add(lblDankeFrIhre);
		
		lblnamekonto = new JLabel("<html><table><tr><td>Name</td><td><input></td></tr><tr><td>Konto</td><td><input ></td></tr></table></html>");
		panel_1.add(lblnamekonto);
		
		JPanel panel_8 = new JPanel();
		tabbedPane.addTab("Combobox Bsp", null, panel_8, null);
		panel_8.setLayout(new GridLayout(1, 0, 0, 0));
		
		JTextPane textPane = new JTextPane();
		textPane.setText("ComboBox:\nin HTML\n<select name=\"Pizza\">\n<option>Pizza Napoli</option>\n<option>Pizza Funghi</option>\n<option>Pizza Mare</option>\n<option>Pizza Tonno</option>\n</select>\n\nin Scriptsprache\n<Combobox name=Pizza>\nPizza Napoli\nPizza Funghi\nPizza Mare\nPizza Tonno\n</Combobox>");
		panel_8.add(textPane);
		
		JPanel panel_10 = new JPanel();
		panel_8.add(panel_10);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Pizza Napoli", "Pizza Funghi", "Pizza Mare", "Pizza Tonno"}));
		panel_10.add(comboBox);
	}

}
