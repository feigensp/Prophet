package experimentGUI.util.experimentEditorDataVisualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import experimentGUI.plugins.codeViewerPlugin.CodeViewer;
import experimentGUI.util.loggingTreeNode.LoggingTreeNode;
import experimentGUI.util.loggingTreeNode.LoggingTreeXMLHandler;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;

public class VideoRecordAlternative extends JFrame {

	private JTextField minutesTextField;
	private JTextField secondsTextField;
	private JTextField actionIndexTextField;
	private JButton backwardButton;
	private JButton forwardButton;
	private JButton jumpToTimeButton;
	private JButton jumpToActionButton;

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
					VideoRecordAlternative frame = new VideoRecordAlternative("recorder - Kopie.xml", "test.xml");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VideoRecordAlternative(String logPath, String questionnairePath) {
		System.out.println(new File(logPath).exists());
		System.out.println(new File(questionnairePath).exists());
		LoggingTreeNode log = LoggingTreeXMLHandler.loadXMLTree(logPath);
		QuestionTreeNode questionnaire;
		try {
			questionnaire = QuestionTreeXMLHandler.loadXMLTree(questionnairePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		CodeViewer codeViewer = new CodeViewer(questionnaire, null);
		codeViewer.setVisible(true);
		controller = new VideoRecordController(log, codeViewer);
		
		initializeGUI();
		deactivate();
		addListener();
		activate();
	}

	private void addListener() {
		backwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.nextAction();
			}
		});
		jumpToTimeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		jumpToActionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
