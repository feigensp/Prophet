package experimentGUI.util.experimentEditorDataVisualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import experimentGUI.util.experimentEditorDataVisualizer.Actions.Action;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.FileAction;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.InsertAction;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.RemoveAction;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.ScrollAction;
import experimentGUI.util.experimentEditorDataVisualizer.Actions.SearchAction;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;
import java.awt.FlowLayout;

public class VideoRecordAlternative extends JFrame {

	private JTextField minutesTextField;
	private JTextField secondsTextField;
	private JTextField actionIndexTextField;
	private JButton backwardButton;
	private JButton forwardButton;
	private JButton jumpToTimeButton;
	private JButton jumpToActionButton;
	private JMenuItem openMenuItem;
	private JMenuItem closeMenuItem;

	private VideoRecordController controller;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
					VideoRecordAlternative frame = new VideoRecordAlternative();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VideoRecordAlternative() {
		controller = null;

		initializeGUI();
		deactivate();
		addListener();
	}

	private void addListener() {
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO: eigentlich nicht gleich die log datei laden, sondern
				// die fragenerstellungs-xml, weil braucht init vom CodeViewer
				QuestionTreeNode log = null;
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					log = QuestionTreeXMLHandler.loadXMLTree(file.getAbsolutePath());
				}
				if (log != null && log.getType().equals("codeviewer")) {
					//TODO: codeViewer mit plugins erstellen und übergeben
					controller = new VideoRecordController(log, null);
					activate();
				} else {
					controller = null;
					deactivate();
				}
			}
		});
		backwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.lastAction();
			}
		});
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.nextAction();
			}
		});
		jumpToTimeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					long millis = Long.parseLong(secondsTextField.getText()) * 1000;
					millis += Long.parseLong(minutesTextField.getText()) * 60 * 1000;
					controller.jumpToTime(millis);
				} catch(NumberFormatException e) {
					//TODO: Meldung ausgeben					
				}
			}
		});
		jumpToActionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					controller.jumpToAction(Integer.parseInt(actionIndexTextField.getText()));
				} catch(NumberFormatException e) {
					//TODO: Meldung ausgeben
				}
			}
		});
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

	}


	private void activate() {
		minutesTextField.setEnabled(true);
		secondsTextField.setEnabled(true);
		actionIndexTextField.setEnabled(true);
		backwardButton.setEnabled(true);
		forwardButton.setEnabled(true);
		jumpToTimeButton.setEnabled(true);
		jumpToActionButton.setEnabled(true);
	}

	private void deactivate() {
		minutesTextField.setEnabled(false);
		secondsTextField.setEnabled(false);
		actionIndexTextField.setEnabled(false);
		backwardButton.setEnabled(false);
		forwardButton.setEnabled(false);
		jumpToTimeButton.setEnabled(false);
		jumpToActionButton.setEnabled(false);
	}

	/**
	 * Create the frame.
	 */
	private void initializeGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);

		openMenuItem = new JMenuItem("Laden");
		fileMenu.add(openMenuItem);

		closeMenuItem = new JMenuItem("Beenden");
		fileMenu.add(closeMenuItem);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(5, 5));

		JPanel oneStepPanel = new JPanel();
		oneStepPanel.setBorder(null);
		panel.add(oneStepPanel, BorderLayout.NORTH);
		oneStepPanel.setLayout(new GridLayout(0, 2, 0, 0));

		backwardButton = new JButton("<--");
		oneStepPanel.add(backwardButton);

		forwardButton = new JButton("-->");
		oneStepPanel.add(forwardButton);

		JPanel overviewPanel = new JPanel();
		overviewPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.add(overviewPanel, BorderLayout.SOUTH);
		overviewPanel.setLayout(new GridLayout(0, 3, 0, 0));

		JPanel actionAmountOverviewPanel = new JPanel();
		overviewPanel.add(actionAmountOverviewPanel);

		JLabel currentActionNumberLabel = new JLabel("0");
		actionAmountOverviewPanel.add(currentActionNumberLabel);

		JLabel label = new JLabel("/");
		actionAmountOverviewPanel.add(label);

		JLabel maxActionNumberLabel = new JLabel("0");
		actionAmountOverviewPanel.add(maxActionNumberLabel);

		JPanel timeOverviewPanel = new JPanel();
		overviewPanel.add(timeOverviewPanel);

		JLabel lblZeit = new JLabel("Zeit:");
		timeOverviewPanel.add(lblZeit);

		JLabel currentTimeLabel = new JLabel("");
		timeOverviewPanel.add(currentTimeLabel);

		JPanel actionDetailOverview = new JPanel();
		overviewPanel.add(actionDetailOverview);

		JLabel lblLetzteAction = new JLabel("Action:");
		actionDetailOverview.add(lblLetzteAction);

		JLabel lastActionLabel = new JLabel("");
		actionDetailOverview.add(lastActionLabel);

		JPanel menuPanel = new JPanel();
		panel.add(menuPanel, BorderLayout.CENTER);
		menuPanel.setLayout(new GridLayout(2, 0, 0, 5));

		JPanel jumpToTimePanel = new JPanel();
		jumpToTimePanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		menuPanel.add(jumpToTimePanel);
		jumpToTimePanel.setLayout(new BorderLayout(0, 0));

		JLabel lblSpringeZuZeitpunkt = new JLabel("<html><u>Springe zu Zeitpunkt</u></html>");
		lblSpringeZuZeitpunkt.setHorizontalAlignment(SwingConstants.CENTER);
		jumpToTimePanel.add(lblSpringeZuZeitpunkt, BorderLayout.NORTH);

		JPanel panel_1 = new JPanel();
		jumpToTimePanel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new BorderLayout(0, 0));
		
				jumpToTimeButton = new JButton("OK");
				panel_1.add(jumpToTimeButton, BorderLayout.EAST);
				
				JPanel panel_3 = new JPanel();
				FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
				flowLayout_1.setAlignment(FlowLayout.LEADING);
				panel_1.add(panel_3);
				
						JLabel lblMinuten = new JLabel("Minuten");
						panel_3.add(lblMinuten);
						
								minutesTextField = new JTextField();
								panel_3.add(minutesTextField);
								minutesTextField.setColumns(10);
								
										JLabel lblSekunden = new JLabel("Sekunden");
										panel_3.add(lblSekunden);
										
												secondsTextField = new JTextField();
												panel_3.add(secondsTextField);
												secondsTextField.setColumns(10);

		JPanel jumpToAction = new JPanel();
		jumpToAction.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		menuPanel.add(jumpToAction);
		jumpToAction.setLayout(new BorderLayout(0, 0));

		JLabel lblZuEinerAction = new JLabel("<html><u>Springe zu Position</u></html>");
		lblZuEinerAction.setHorizontalAlignment(SwingConstants.CENTER);
		jumpToAction.add(lblZuEinerAction, BorderLayout.NORTH);

		JPanel panel_2 = new JPanel();
		jumpToAction.add(panel_2, BorderLayout.CENTER);
				panel_2.setLayout(new BorderLayout(0, 0));
		
				jumpToActionButton = new JButton("OK");
				panel_2.add(jumpToActionButton, BorderLayout.EAST);
				
				JPanel panel_4 = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
				flowLayout.setAlignment(FlowLayout.LEADING);
				panel_2.add(panel_4);
				
						JLabel lblActionsindex = new JLabel("Index:");
						panel_4.add(lblActionsindex);
						
								actionIndexTextField = new JTextField();
								panel_4.add(actionIndexTextField);
								actionIndexTextField.setColumns(10);

		pack();
	}

}
