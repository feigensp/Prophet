package guiTest;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JTree;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;

public class TestMain extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestMain frame = new TestMain();
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
	public TestMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 722, 533);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JPanel left = new JPanel();
		
		JPanel right = new JPanel();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addComponent(left, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(right, GroupLayout.PREFERRED_SIZE, 471, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(right, GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
				.addComponent(left, GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
		);
		right.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane rightTabs = new JTabbedPane(JTabbedPane.TOP);
		right.add(rightTabs, BorderLayout.CENTER);
		
		JPanel rightexperiment = new JPanel();
		rightTabs.addTab("Experiment", null, rightexperiment, null);
		
		JLabel lblAufgabe = new JLabel("Aufgabe:");
		
		JTextArea txtrBitteAufgabenbeschreibungEingeben = new JTextArea();
		txtrBitteAufgabenbeschreibungEingeben.setEnabled(false);
		txtrBitteAufgabenbeschreibungEingeben.setText("Aufgabenbeschreibung...");
		
		JLabel lblAntwort = new JLabel("Antwort:");
		
		JTextArea txtrJeNachAufgabe = new JTextArea();
		txtrJeNachAufgabe.setText("Je nach Aufgabe aus:\r\nCheckboxen\r\nRadioButtons\r\nTextfelder");
		
		JLabel lblBearbeitungszeit = new JLabel("Bearbeitungszeit:");
		
		JLabel lblMin = new JLabel("5 min");
		
		JLabel lblVerstricheneZeit = new JLabel("Verstrichene Zeit:");
		
		JLabel lblDerzeitVerbrauchteZeit = new JLabel("Derzeit verbrauchte Zeit f\u00FCr Aufgabe");
		GroupLayout gl_rightexperiment = new GroupLayout(rightexperiment);
		gl_rightexperiment.setHorizontalGroup(
			gl_rightexperiment.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightexperiment.createSequentialGroup()
					.addGap(27)
					.addGroup(gl_rightexperiment.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblAntwort)
						.addComponent(lblAufgabe)
						.addGroup(gl_rightexperiment.createParallelGroup(Alignment.LEADING)
							.addComponent(lblVerstricheneZeit)
							.addComponent(lblBearbeitungszeit)))
					.addGap(18)
					.addGroup(gl_rightexperiment.createParallelGroup(Alignment.LEADING)
						.addComponent(txtrBitteAufgabenbeschreibungEingeben, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtrJeNachAufgabe, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMin)
						.addComponent(lblDerzeitVerbrauchteZeit))
					.addContainerGap(54, Short.MAX_VALUE))
		);
		gl_rightexperiment.setVerticalGroup(
			gl_rightexperiment.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightexperiment.createSequentialGroup()
					.addGap(19)
					.addGroup(gl_rightexperiment.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAufgabe)
						.addComponent(txtrBitteAufgabenbeschreibungEingeben, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addGroup(gl_rightexperiment.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAntwort)
						.addComponent(txtrJeNachAufgabe, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
					.addGroup(gl_rightexperiment.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBearbeitungszeit)
						.addComponent(lblMin))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_rightexperiment.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblVerstricheneZeit)
						.addComponent(lblDerzeitVerbrauchteZeit))
					.addGap(72))
		);
		rightexperiment.setLayout(gl_rightexperiment);
		rightTabs.setBackgroundAt(0, new Color(240, 128, 128));
		
		JPanel editorpanel = new JPanel();
		rightTabs.addTab("Datei (1)", null, editorpanel, null);
		editorpanel.setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea = new JTextArea();
		editorpanel.add(textArea, BorderLayout.CENTER);
		left.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane leftTabs = new JTabbedPane(JTabbedPane.TOP);
		left.add(leftTabs, BorderLayout.CENTER);
		
		JPanel filesystempanel = new JPanel();
		leftTabs.addTab("Dateiverwaltung", null, filesystempanel, null);
		filesystempanel.setLayout(new BorderLayout(0, 0));
		
		JTree filesystemtree = new JTree();
		filesystempanel.add(filesystemtree, BorderLayout.CENTER);
		
		JPanel leftExperiment = new JPanel();
		leftTabs.addTab("Experiment", null, leftExperiment, null);
		GridBagLayout gbl_leftExperiment = new GridBagLayout();
		gbl_leftExperiment.columnWidths = new int[]{0, 0, 0};
		gbl_leftExperiment.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_leftExperiment.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_leftExperiment.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		leftExperiment.setLayout(gbl_leftExperiment);
		
		JLabel lblCodewort = new JLabel("Codewort:");
		GridBagConstraints gbc_lblCodewort = new GridBagConstraints();
		gbc_lblCodewort.insets = new Insets(0, 0, 5, 5);
		gbc_lblCodewort.anchor = GridBagConstraints.EAST;
		gbc_lblCodewort.gridx = 0;
		gbc_lblCodewort.gridy = 1;
		leftExperiment.add(lblCodewort, gbc_lblCodewort);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		leftExperiment.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnStarten = new JButton("Starten");
		GridBagConstraints gbc_btnStarten = new GridBagConstraints();
		gbc_btnStarten.gridx = 1;
		gbc_btnStarten.gridy = 3;
		leftExperiment.add(btnStarten, gbc_btnStarten);
		leftTabs.setBackgroundAt(1, new Color(240, 128, 128));
		panel.setLayout(gl_panel);
		
		JMenuBar menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu menu = new JMenu("New menu");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("New menu item");
		menu.add(menuItem);
	}
}
