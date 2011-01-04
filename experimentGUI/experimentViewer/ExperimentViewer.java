package experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
	private JPanel contentPane;
	//the textpanes (one is one question)
	private QuestionViewPane currentViewPane;
	private HashMap<QuestionTreeNode,QuestionViewPane> textPanes;
	//time objects
	private JPanel timePanel;
	private HashMap<QuestionTreeNode,ClockLabel> times;
	private ClockLabel totalTime;
	//nodes of the question tree
	private QuestionTreeNode tree;
	private QuestionTreeNode currentNode;
	
	private File saveDir;
	
	private static Logger logger;
	private Handler fileHandler;
	private Formatter formatter;
	
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
		//logger
		try {
			logger = Logger.getLogger("experimentGUI.experimentViewer.ExperimentViewer");
			fileHandler = new FileHandler("experimentGUI.experimentViewer.ExperimentViewer.txt");
			formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
			logger.addHandler(fileHandler);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		
		this.setSize(800, 600);
		setLocationRelativeTo(null);
		
		String fileName = Constants.DEFAULT_FILE;
		if (!(new File(fileName).exists())) {
			logger.info(Constants.DEFAULT_FILE + " existierte nicht.");
			fileName = JOptionPane.showInputDialog("Bitte Experiment angeben:");
			if(fileName == null) {
				logger.info("Beende Programm.");
				System.exit(0);
			}
			if (!fileName.endsWith(".xml")) {
				fileName+=".xml";
			}
			logger.info("Setze Dateinamen auf " + fileName);
		} else {
			logger.info(Constants.DEFAULT_FILE + " existierte.");
		}
		try {
			tree = QuestionTreeXMLHandler.loadXMLTree(fileName);
		} catch (FileNotFoundException e) {
			logger.info("Fehler beim laden der Datei \"" + fileName + "\" in " + System.getProperty("user.dir") + "\r\n" + e.getLocalizedMessage());
			JOptionPane.showMessageDialog(this, "Experiment nicht gefunden.");
			logger.info("Beende Programm.");
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
		//Starte Experiment
		QuestionTreeNode superRoot = new QuestionTreeNode();
		superRoot.add(tree);
		tree.setParent(null);
		currentNode=superRoot;
		textPanes = new HashMap<QuestionTreeNode,QuestionViewPane>();
		times = new HashMap<QuestionTreeNode,ClockLabel>();
		totalTime = new ClockLabel("Gesamtzeit");
		timePanel = new JPanel();
		nextNode();
	}
	
	private boolean nextNode() {
		logger.info("enter \"nextNode()\"");
		pauseClock();
		if (currentNode==tree) {
			logger.info("currentNode==tree");
			String subject = currentNode.getAnswer(Constants.KEY_SUBJECT);
			if (subject==null || subject.length()==0) {
				JOptionPane.showMessageDialog(this, "Bitte Probanden-Code eingeben!", "Fehler!", JOptionPane.ERROR_MESSAGE);
				logger.info("exit \"nextNode()\" --> kein Probandencode");
				return false;
			}
			String experiment = currentNode.getAttributeValue(Constants.KEY_CODE);
			if (experiment==null) {
				experiment="default";
			}
			saveDir = new File(experiment+"_"+subject);
			totalTime.start();
		}
		boolean inactive = Boolean.parseBoolean(currentNode.getAttributeValue("inactive"));
		logger.info("inactive: " + inactive);
		logger.info("child count of currentNode: " + currentNode.getChildCount());
		if (!inactive && currentNode.getChildCount() != 0) {
			currentNode=(QuestionTreeNode)currentNode.getFirstChild();
		} else {
			if (!inactive) {
				exitNode();
			}
			logger.info("enter while loop");
			while(currentNode.getNextSibling()==null) {
				currentNode = (QuestionTreeNode)currentNode.getParent();
				if (currentNode==null) {
					logger.info("exit \"nextNode()\" --> currentNode == null");
					return false;
				}
				logger.info("currentNode: " + currentNode.getName());
				exitNode();
			}
			logger.info("exit while loop");
			currentNode=(QuestionTreeNode)currentNode.getNextSibling();
		}
		inactive = Boolean.parseBoolean(currentNode.getAttributeValue(Constants.KEY_INACTIVE));
		if (inactive) {
			logger.info("exit \"nextNode()\" --> inactive == true");
			return nextNode();
		} else {
			boolean doNotShowContent = Boolean.parseBoolean(currentNode.getAttributeValue(Constants.KEY_DONOTSHOWCONTENT));
			if (doNotShowContent) {
				enterNode();
				logger.info("exit \"nextNode()\" --> doNotShowContent == true");
				return nextNode();
			} else {
				refresh();
				enterNode();
				logger.info("exit \"nextNode()\" --> doNotShowContent == false");
				return true;
			}
		}
	}
	private boolean previousNode() {
		logger.info("enter \"previousNode()\"");
		pauseClock();
		if (currentNode.isQuestion()) {
			logger.info("currentNode.isQuestion() == true");
			QuestionTreeNode tempNode = currentNode;
			logger.info("enter while loop");
			while ((QuestionTreeNode)tempNode.getPreviousSibling()!=null) {
				tempNode = (QuestionTreeNode)tempNode.getPreviousSibling();
				logger.info("tempNode: " + tempNode.getName());
				boolean inactive = Boolean.parseBoolean(tempNode.getAttributeValue(Constants.KEY_INACTIVE));
				logger.info("inactive: " + inactive);
				if (!inactive) {
					exitNode();
					currentNode=tempNode;
					refresh();
					enterNode();
					logger.info("exit \"previous Node()\" --> inactive == false");
					return true;
				}
			}
			logger.info("exit while loop");
		}
		logger.info("exit \"previous Node()\" --> end of method");
		return false;
	}
	private void enterNode() {
		logger.info("enter \"enterNode()\"");
		for (PluginInterface plugin : PluginList.getPlugins()) {
			logger.info("plugin.getKey(): " + plugin.getKey());
			currentNode.setPluginData(plugin.getKey(), plugin.enterNode(currentNode));
		}
		logger.info("exit \"enterNode()\"");
	}
	private void exitNode() {
		logger.info("enter \"exitNode()\"");
		for (PluginInterface plugin : PluginList.getPlugins()) {
			logger.info("plugin.getKey(): " + plugin.getKey());
			plugin.exitNode(currentNode, currentNode.getPluginData(plugin.getKey()));
		}
		if (currentNode.isExperiment()) {
			logger.info("currentNode.isExperiment() == true");
			endQuestionnaire();
		}
		logger.info("exit \"exitNode()\"");
	}
	private void pauseClock() {
		ClockLabel clock = times.get(currentNode);
		if (clock!= null) {
			clock.pause();
		}
	}
	private void refresh() {
		if (currentViewPane!=null) {
			contentPane.remove(currentViewPane);
		}
		if (timePanel!=null) {
			contentPane.remove(timePanel);
		}
		if (currentNode==null) {
			return;
		}
		currentViewPane = textPanes.get(currentNode);
		if (currentViewPane==null) {
			currentViewPane = new QuestionViewPane(currentNode);
			currentViewPane.setActionListener(myActionListener);
			textPanes.put(currentNode, currentViewPane);
		} 
		contentPane.add(currentViewPane, BorderLayout.CENTER);
		
		timePanel.removeAll();
		ClockLabel clock = times.get(currentNode);
		if (clock==null) {
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
		for (PluginInterface plugin : PluginList.getPlugins()) {
			plugin.refresh();
		}
	}

	/**
	 * method which is called when the last question is answered
	 */
	private void endQuestionnaire() {
		logger.info("enter \"endQuestionnaire()\"");
		contentPane.removeAll();
		contentPane.updateUI();
		JTextPane output = new JTextPane();
		output.setEditable(false);
		output.setEditorKit(new HTMLEditorKit());
		String outputString = "<p>Befragung beendet.</p>";
		QuestionTreeXMLHandler.saveXMLAnswerTree(tree, saveDir.getPath()+System.getProperty("file.separator")+Constants.FILE_ANSWERS);
		for (PluginInterface plugin : PluginList.getPlugins()) {
			String tmp = plugin.finishExperiment();
			if (tmp!=null) {
				logger.info(tmp);
				outputString+="<p>"+tmp+"</p>";
			}
		}
		output.setText(outputString);
		output.setCaretPosition(0);
		contentPane.add(output, BorderLayout.CENTER);
		logger.info("exit \"endQuestionnaire()\"");
	}

	public QuestionTreeNode getTree() {
		return tree;
	}
	
	public File getSaveDir() {
		return saveDir;
	}
}
