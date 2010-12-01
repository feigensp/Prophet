package experimentGUI.util.experimentEditorDataVisualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import experimentGUI.util.experimentEditorDataVisualizer.Actions.Action;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

	private JTextField minutesTextField;
	private JTextField secondsTextField;
	private JTextField actionIndexTextField;
	private JButton backwardButton;
	private JButton forwardButton;
	private JButton jumpToTimeButton;
	private JButton jumpToActionButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Menu() {
		initializeGUI();
	}
	
	public Menu(Vector<Action> actions) {
		initializeGUI();
		addListener();
	}
	
	private void addListener() {
		backwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		
		JLabel lblMinuten = new JLabel("Minuten");
		panel_1.add(lblMinuten);
		
		minutesTextField = new JTextField();
		panel_1.add(minutesTextField);
		minutesTextField.setColumns(10);
		
		JLabel lblSekunden = new JLabel("Sekunden");
		panel_1.add(lblSekunden);
		
		secondsTextField = new JTextField();
		panel_1.add(secondsTextField);
		secondsTextField.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		jumpToTimePanel.add(panel_3, BorderLayout.SOUTH);
		
		jumpToTimeButton = new JButton("OK");
		panel_3.add(jumpToTimeButton);
		
		JPanel jumpToAction = new JPanel();
		jumpToAction.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		menuPanel.add(jumpToAction);
		jumpToAction.setLayout(new BorderLayout(0, 0));
		
		JLabel lblZuEinerAction = new JLabel("<html><u>Springe zu Position</u></html>");
		lblZuEinerAction.setHorizontalAlignment(SwingConstants.CENTER);
		jumpToAction.add(lblZuEinerAction, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		jumpToAction.add(panel_2, BorderLayout.CENTER);
		
		JLabel lblActionsindex = new JLabel("Index:");
		panel_2.add(lblActionsindex);
		
		actionIndexTextField = new JTextField();
		panel_2.add(actionIndexTextField);
		actionIndexTextField.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		jumpToAction.add(panel_4, BorderLayout.SOUTH);
		
		jumpToActionButton = new JButton("OK");
		panel_4.add(jumpToActionButton);
		
		pack();
	}

}
