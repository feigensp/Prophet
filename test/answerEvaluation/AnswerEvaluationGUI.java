package test.answerEvaluation;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;

public class AnswerEvaluationGUI extends JFrame {

	private JTextField questionnairePathTextField;
	private JButton questionnairePathButton;
	private JTextField answerPathTextField;
	private JButton answerPathButton;
	private JButton loadButton;
	private JTree questionOverviewTree;

	private QuestionTreeNode questionnaire;
	private QuestionTreeNode answers;
	private QuestionTreeNode selected;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AnswerEvaluationGUI frame = new AnswerEvaluationGUI();
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
	public AnswerEvaluationGUI() {
		selected = null;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 450);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel fileLoadPanel = new JPanel();
		contentPane.add(fileLoadPanel, BorderLayout.NORTH);
		fileLoadPanel.setLayout(new BorderLayout(0, 0));

		JPanel filePanel = new JPanel();
		fileLoadPanel.add(filePanel, BorderLayout.NORTH);
		filePanel.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel questionSourcePanel = new JPanel();
		filePanel.add(questionSourcePanel);
		questionSourcePanel.setLayout(new BorderLayout(0, 0));

		JLabel questionnaireLabel = new JLabel("Fragebogen:");
		questionSourcePanel.add(questionnaireLabel, BorderLayout.NORTH);

		JPanel questionnairePathPanel = new JPanel();
		questionSourcePanel.add(questionnairePathPanel, BorderLayout.SOUTH);

		questionnairePathTextField = new JTextField();
		questionnairePathPanel.add(questionnairePathTextField);
		questionnairePathTextField.setColumns(10);

		questionnairePathButton = new JButton("Durchsuchen");
		questionnairePathPanel.add(questionnairePathButton);

		JPanel answerSourcePanel = new JPanel();
		filePanel.add(answerSourcePanel);
		answerSourcePanel.setLayout(new BorderLayout(0, 0));

		JLabel answerLabel = new JLabel("Antworten:");
		answerSourcePanel.add(answerLabel, BorderLayout.NORTH);

		JPanel answerPathPanel = new JPanel();
		answerSourcePanel.add(answerPathPanel, BorderLayout.CENTER);

		answerPathTextField = new JTextField();
		answerPathPanel.add(answerPathTextField);
		answerPathTextField.setColumns(10);

		answerPathButton = new JButton("Durchsuchen");
		answerPathPanel.add(answerPathButton);

		loadButton = new JButton("Laden/Aktualisieren");
		fileLoadPanel.add(loadButton, BorderLayout.SOUTH);

		JSplitPane contentSplitPane = new JSplitPane();
		contentPane.add(contentSplitPane, BorderLayout.CENTER);

		JPanel questionOverviewPanel = new JPanel();
		contentSplitPane.setLeftComponent(questionOverviewPanel);
		questionOverviewPanel.setLayout(new BorderLayout(0, 0));

		questionOverviewTree = new JTree();
		questionOverviewTree.setModel((TreeModel) null);
		questionOverviewTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		new JScrollPane(questionOverviewTree);
		questionOverviewTree.setEnabled(false);
		questionOverviewPanel.add(questionOverviewTree);

		JPanel contentPanel = new JPanel();
		contentSplitPane.setRightComponent(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JTextPane contentTextPane = new JTextPane();
		contentPanel.add(contentTextPane, BorderLayout.CENTER);

		addListener();
	}

	private void addListener() {
		// search questionnaire file
		questionnairePathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					questionnairePathTextField.setText(file.getAbsolutePath());
				}
			}
		});
		// search answer file
		answerPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					answerPathTextField.setText(file.getAbsolutePath());
				}
			}
		});
		// load data
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				questionnaire = QuestionTreeXMLHandler.loadXMLTree(questionnairePathTextField.getText());
				answers = QuestionTreeXMLHandler.loadXMLTree(answerPathTextField.getText());
				if (questionnaire != null && answers != null) {
					// evtl. noch aufbau vergleichen, ob passend
					questionOverviewTree.setModel(new DefaultTreeModel(questionnaire));
				}
			}
		});
		// tree mouse listener
		questionOverviewTree.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				TreePath selPath = questionOverviewTree.getPathForLocation(e.getX(), e.getY());
				//System.out.println(selPath);
				if (selPath != null) {
					selected = (QuestionTreeNode) selPath.getLastPathComponent();
					if (selected.isCategory()) {
						// verarbeiten
					} else if (selected.isQuestion()) {
						// html quelltext holen
						String htmlContent = selected.getValue();
						ArrayList<HashMap<String, String>> specifiedAnswers = HTMLFileEvaluation
								.storeAnswerSpecifications(htmlContent);
						// antworten holen
						ArrayList<HashMap<String, String>> givenAnswers = AnswerXMLFileEvaluation
								.storeAnswers(null);
					} else if (selected.isExperiment()) {
						// verarbeiten
					}
				}
			}
		});
	}
}

// C:\Users\hasselbe\workspace\QuelltextProj\test.xml
