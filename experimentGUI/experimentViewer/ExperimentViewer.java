package experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;

import experimentGUI.Constants;
import experimentGUI.PluginInterface;
import experimentGUI.PluginList;
import experimentGUI.util.QuestionViewPane;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;

/**
 * This class shows the html files (questions) creates the navigation and
 * navigates everything...
 * 
 * @author Markus Köppen, Andreas Hasselberg
 * 
 */
public class ExperimentViewer extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	// the textpanes (one for each question)
	private QuestionViewPane currentViewPane;
	private HashMap<QuestionTreeNode, QuestionViewPane> textPanes;
	// time objects
	private JPanel timePanel;
	private HashMap<QuestionTreeNode, ClockLabel> times;
	private ClockLabel totalTime;
	// nodes of the question tree
	private QuestionTreeNode tree;
	private QuestionTreeNode currentNode;

	private File saveDir;
	
	private HashSet<QuestionTreeNode> enteredNodes;
	
	private boolean ignoreDenyNextNode;

	ActionListener myActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String command = arg0.getActionCommand();
			if (command.equals(Constants.KEY_BACKWARD)) {
				previousNode();
			} else if (command.equals(Constants.KEY_FORWARD)) {
				nextNode();
			}
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
					ExperimentViewer frame = new ExperimentViewer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * With the call of the Constructor the data is loaded and everything is
	 * initialized. The first question is showed.
	 * 
	 * @param path
	 *            path of the xml file with the data
	 * @param cqlp
	 *            the categorieQuestionListsPanel where the overview is shown
	 */
	public ExperimentViewer() {
		setTitle("Aufgaben");
		this.setSize(800, 600);
		setLocationRelativeTo(null);

		String fileName = Constants.DEFAULT_FILE;
		if (!(new File(fileName).exists())) {
			fileName = JOptionPane.showInputDialog("Bitte Experiment angeben:");
			if (fileName == null) {
				System.exit(0);
			}
			if (!fileName.endsWith(".xml")) {
				fileName += ".xml";
			}
		}
		try {
			boolean isInDir = new File(fileName).getCanonicalFile().getParentFile()
					.equals(new File(".").getCanonicalFile());
			if (!isInDir) {
				JOptionPane.showMessageDialog(this, "Experiment nicht im aktuellen Verzeichnis.");
				System.exit(0);
			}
			tree = QuestionTreeXMLHandler.loadXMLTree(fileName);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Experiment nicht gefunden.");
			System.exit(0);
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		this.setContentPane(contentPane);
		for (PluginInterface plugin : PluginList.getPlugins()) {
			plugin.experimentViewerRun(this);
		}
		// Starte Experiment
		QuestionTreeNode superRoot = new QuestionTreeNode();
		superRoot.add(tree);
		tree.setParent(null);
		currentNode = superRoot;				
		textPanes = new HashMap<QuestionTreeNode, QuestionViewPane>();
		times = new HashMap<QuestionTreeNode, ClockLabel>();
		totalTime = new ClockLabel("Gesamtzeit");
		timePanel = new JPanel();
		enteredNodes = new HashSet<QuestionTreeNode>();
		nextNode();
	}

	private boolean nextNode() {
		if (denyNextNode()) {
			return false;
		}
		pauseClock();
		if (currentNode == tree) {
			String subject = currentNode.getAnswer(Constants.KEY_SUBJECT);
			if (subject == null || subject.length() == 0) {
				JOptionPane.showMessageDialog(this, "Bitte Probanden-Code eingeben!", "Fehler!",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			String experiment = currentNode.getAttributeValue(Constants.KEY_EXPERIMENT_CODE);
			if (experiment == null) {
				experiment = "default";
			}
			saveDir = new File(experiment + "_" + subject);
			totalTime.start();
		}
		
		//step down if we may enter and there are children, else step aside if there is a sibling, else step up
		if (!denyEnterNode() && currentNode.getChildCount() != 0) {
			currentNode = (QuestionTreeNode) currentNode.getFirstChild();
		} else {
			exitNode();
			while (currentNode.getNextSibling() == null) {
				currentNode = (QuestionTreeNode) currentNode.getParent();
				if (currentNode == null) {
					return false;
				}
				exitNode();
			}
			currentNode = (QuestionTreeNode) currentNode.getNextSibling();
		}
		
		//check if found node is visitable
		if (denyEnterNode()) {
			return nextNode();
		} else {
			boolean doNotShowContent = Boolean.parseBoolean(currentNode
					.getAttributeValue(Constants.KEY_DONOTSHOWCONTENT));
			if (doNotShowContent) {
				enterNode();
				return nextNode();
			} else {
				refresh();
				enterNode();
				return true;
			}
		}
	}

	private boolean previousNode() {
		if (denyNextNode()) {
			return false;
		}
		pauseClock();
		if (currentNode.isQuestion()) {
			QuestionTreeNode tempNode = currentNode;
			while ((QuestionTreeNode) tempNode.getPreviousSibling() != null) {
				tempNode = (QuestionTreeNode) tempNode.getPreviousSibling();
				if (!denyEnterNode()) {
					exitNode();
					currentNode = tempNode;
					refresh();
					enterNode();
					return true;
				}
			}
		}
		return false;
	}

	private boolean denyEnterNode() {
		return denyEnterNode(currentNode);
	}
	public static boolean denyEnterNode(QuestionTreeNode node) {
		for (PluginInterface plugin : PluginList.getPlugins()) {
			if(plugin.denyEnterNode(node)) {
				return true;
			}
		}
		return false;
	}
	
	private void enterNode() {
		enteredNodes.add(currentNode);
		for (PluginInterface plugin : PluginList.getPlugins()) {
			plugin.enterNode(currentNode);
		}
	}
	
	private boolean denyNextNode() {
		if (ignoreDenyNextNode) {
			return false;
		}
		for (PluginInterface plugin : PluginList.getPlugins()) {
			String message = plugin.denyNextNode(currentNode);
			if (message!=null) {
				if (message.length()>0) {
					JOptionPane.showMessageDialog(this, message);
				}
				return true;
			}
		}
		return false;
	}

	private void exitNode() {
			if (enteredNodes.contains(currentNode)) {
				for (PluginInterface plugin : PluginList.getPlugins()) {
					plugin.exitNode(currentNode);
				}
				if (currentNode.isExperiment()) {
					endQuestionnaire();
				}
				enteredNodes.remove(currentNode);
			}
	}

	private void pauseClock() {
		ClockLabel clock = times.get(currentNode);
		if (clock != null) {
			clock.pause();
		}
	}

	private void refresh() {
		this.setEnabled(false);

		if (currentViewPane != null) {
			contentPane.remove(currentViewPane);
		}
		if (timePanel != null) {
			contentPane.remove(timePanel);
		}
		if (currentNode == null) {
			return;
		}
		currentViewPane = textPanes.get(currentNode);
		if (currentViewPane == null) {
			currentViewPane = new QuestionViewPane(currentNode);
			currentViewPane.setActionListener(myActionListener);
			textPanes.put(currentNode, currentViewPane);
		}
		contentPane.add(currentViewPane, BorderLayout.CENTER);

		timePanel.removeAll();
		ClockLabel clock = times.get(currentNode);
		if (clock == null) {
			if (currentNode.isExperiment()) {
				clock = new ClockLabel(currentNode, null);
			} else {
				clock = new ClockLabel(currentNode, "Aktuell");
			}
			times.put(currentNode, clock);
			clock.start();
		} else {
			clock.resume();
		}
		timePanel.add(clock);
		timePanel.add(totalTime);
		contentPane.add(timePanel, BorderLayout.SOUTH);
		contentPane.updateUI();

		this.setEnabled(true);
	}

	/**
	 * method which is called when the last question is answered
	 */
	private void endQuestionnaire() {
		contentPane.removeAll();
		contentPane.updateUI();
		JTextPane output = new JTextPane();
		output.setEditable(false);
		output.setEditorKit(new HTMLEditorKit());
		String endMessage = "Befragung beendet.";
		String outputString = "<p>" + endMessage + "</p>";
		QuestionTreeXMLHandler.saveXMLAnswerTree(tree,
				saveDir.getPath() + System.getProperty("file.separator") + Constants.FILE_ANSWERS);
		for (PluginInterface plugin : PluginList.getPlugins()) {
			String tmp = plugin.finishExperiment();
			if (tmp != null) {
				outputString += "<p>" + tmp + "</p>";
			}
		}
		output.setText(outputString);
		output.setCaretPosition(0);
		contentPane.add(output, BorderLayout.CENTER);
	}

	public QuestionTreeNode getTree() {
		return tree;
	}

	public File getSaveDir() {
		return saveDir;
	}
	
	public void forceNext(boolean hard) {
		ignoreDenyNextNode = hard;
		currentViewPane.clickSubmit();
		ignoreDenyNextNode = false;
	}
	public void saveCurrentAnswers() {
		currentViewPane.saveCurrentAnswersToNode();
	}
}
