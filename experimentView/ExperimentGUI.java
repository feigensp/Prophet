package experimentView;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import experimentQuestionCreator.TreeNode;
import experimentQuestionCreator.XMLHandler;

public class ExperimentGUI extends JFrame {
	
	//Pfad zu den Anfangsfragen
	private String startPath;
	
	private JButton startButton;
	private DefaultListModel listModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExperimentGUI frame = new ExperimentGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	
	public void setListener() {
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
			}
		});
	}
	
	public void loadList() {
		TreeNode root = XMLHandler.loadXMLTree(startPath);
		//Seiten erstellen
		
	}

	/**
	 * Create the frame.
	 */
	public ExperimentGUI() {
		JPanel contentPane;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 743, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(10, 50));
		menuPanel.setMinimumSize(new Dimension(10, 50));
		contentPane.add(menuPanel, BorderLayout.SOUTH);
		menuPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel backPanel = new JPanel();
		menuPanel.add(backPanel, BorderLayout.WEST);
		backPanel.setLayout(new BorderLayout(0, 0));
		
		JButton backButton = new JButton("Zur\u00FCck");
		backPanel.add(backButton, BorderLayout.CENTER);
		
		JPanel forwardPanel = new JPanel();
		menuPanel.add(forwardPanel, BorderLayout.EAST);
		forwardPanel.setLayout(new BorderLayout(0, 0));
		
		JButton forwardButton = new JButton("Vorw\u00E4rts");
		forwardPanel.add(forwardButton, BorderLayout.CENTER);
		
		JPanel centerMenuPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) centerMenuPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		menuPanel.add(centerMenuPanel, BorderLayout.CENTER);
		
		JLabel timeLabel = new JLabel("Ben\u00F6tigte Zeit:");
		centerMenuPanel.add(timeLabel);
		
		JLabel showTimeLabel = new JLabel("     ");
		centerMenuPanel.add(showTimeLabel);
		
		JPanel centerPanel = new JPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));
		
		JSplitPane centerSplit = new JSplitPane();
		centerPanel.add(centerSplit);
		
		JPanel overviewPanel = new JPanel();
		overviewPanel.setPreferredSize(new Dimension(125, 10));
		overviewPanel.setMinimumSize(new Dimension(125, 10));
		centerSplit.setLeftComponent(overviewPanel);
		overviewPanel.setLayout(new BorderLayout(0, 0));
		
		JList list = new JList();
		overviewPanel.add(list, BorderLayout.CENTER);
		
		JPanel questionPanel = new JPanel();
		centerSplit.setRightComponent(questionPanel);
		questionPanel.setLayout(new CardLayout(0, 0));
		
		JPanel codePanel = new JPanel();
		questionPanel.add(codePanel, "name_23194295593081");
		codePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel codeNorth = new JPanel();
		codePanel.add(codeNorth, BorderLayout.NORTH);
		
		JLabel codeHeadlineLabel = new JLabel("Codeworteingabe");
		codeHeadlineLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		codeNorth.add(codeHeadlineLabel);
		
		JPanel codeCenter = new JPanel();
		codePanel.add(codeCenter, BorderLayout.CENTER);
		codeCenter.setLayout(new BoxLayout(codeCenter, BoxLayout.Y_AXIS));
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		codeCenter.add(verticalStrut_1);
		
		JLabel codeInstructionLabel = new JLabel("<html>Bitte geben sie ihr codewort ein, es wird wie folgt gebildet:<br>...</html>");
		codeInstructionLabel.setHorizontalTextPosition(SwingConstants.LEADING);
		codeCenter.add(codeInstructionLabel);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		codeCenter.add(verticalStrut);
		
		JLabel lblbeispiel = new JLabel("<html>Beispiel:<br>...</html>");
		codeCenter.add(lblbeispiel);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		codeCenter.add(verticalStrut_2);
		
		JLabel codeLabel = new JLabel("Codewort:");
		codeCenter.add(codeLabel);
		
		startButton = new JButton("Start");
		codeCenter.add(startButton);
	}
}
