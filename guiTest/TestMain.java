package guiTest;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane rightTabs = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(rightTabs, BorderLayout.CENTER);
		
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
		
		JPanel filesystempanel = new JPanel();
		contentPane.add(filesystempanel, BorderLayout.WEST);
		filesystempanel.setLayout(new BorderLayout(0, 0));
		
		JTree filesystemtree = new JTree();
		filesystempanel.add(filesystemtree, BorderLayout.CENTER);
	}
}
