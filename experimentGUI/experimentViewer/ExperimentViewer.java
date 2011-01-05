package experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;

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
	//the textpanes (one for each question)
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
		boolean loadFromJar = false;
		
		this.setSize(800, 600);
		setLocationRelativeTo(null);
		
		File resJar = new File(Constants.RES_JAR_NAME);
		if(resJar.exists()) {
            URL url;
			try {
				url = resJar.toURI().toURL();
	            URLClassLoader ucl = new URLClassLoader(new URL[] { url });
	    		InputStream is = ucl.getResourceAsStream(Constants.DEFAULT_FILE);
	    		if(is != null) {
	    			tree = QuestionTreeXMLHandler.loadXMLTree(is);
	    			loadFromJar = true;
	    		}
			} catch (MalformedURLException e1) {
				JOptionPane.showMessageDialog(this, "Experiment nicht gefunden.");
				System.exit(0); 
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "Experiment nicht gefunden.");
				System.exit(0); 
			}
		}
		
		if(!loadFromJar) {
			String fileName = Constants.DEFAULT_FILE;
			if (!(new File(fileName).exists())) {
				fileName = JOptionPane.showInputDialog("Bitte Experiment angeben:");
				if(fileName == null) {
					System.exit(0);
				}
				if (!fileName.endsWith(".xml")) {
					fileName+=".xml";
				}
			}
			try {
				boolean isInDir = new File(fileName).getCanonicalFile().getParentFile().equals(new File(".").getCanonicalFile());
				if (!isInDir) {
					JOptionPane.showMessageDialog(this, "Experiment nicht im aktuellen Verzeichnis.");
					System.exit(0);
				}
				tree = QuestionTreeXMLHandler.loadXMLTree(fileName);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Experiment nicht gefunden.");
				System.exit(0); 
			}
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
		pauseClock();
		if (currentNode==tree) {
			String subject = currentNode.getAnswer(Constants.KEY_SUBJECT);
			if (subject==null || subject.length()==0) {
				JOptionPane.showMessageDialog(this, "Bitte Probanden-Code eingeben!", "Fehler!", JOptionPane.ERROR_MESSAGE);
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
		if (!inactive && currentNode.getChildCount() != 0) {
			currentNode=(QuestionTreeNode)currentNode.getFirstChild();
		} else {
			if (!inactive) {
				exitNode();
			}
			while(currentNode.getNextSibling()==null) {
				currentNode = (QuestionTreeNode)currentNode.getParent();
				if (currentNode==null) {
					return false;
				}
				exitNode();
			}
			currentNode=(QuestionTreeNode)currentNode.getNextSibling();
		}
		inactive = Boolean.parseBoolean(currentNode.getAttributeValue(Constants.KEY_INACTIVE));
		if (inactive) {
			return nextNode();
		} else {
			boolean doNotShowContent = Boolean.parseBoolean(currentNode.getAttributeValue(Constants.KEY_DONOTSHOWCONTENT));
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
		pauseClock();
		if (currentNode.isQuestion()) {
			QuestionTreeNode tempNode = currentNode;
			while ((QuestionTreeNode)tempNode.getPreviousSibling()!=null) {
				tempNode = (QuestionTreeNode)tempNode.getPreviousSibling();
				boolean inactive = Boolean.parseBoolean(tempNode.getAttributeValue(Constants.KEY_INACTIVE));
				if (!inactive) {
					exitNode();
					currentNode=tempNode;
					refresh();
					enterNode();
					return true;
				}
			}
		}
		return false;
	}
	private void enterNode() {
		for (PluginInterface plugin : PluginList.getPlugins()) {
			currentNode.setPluginData(plugin.getKey(), plugin.enterNode(currentNode));
		}
	}
	private void exitNode() {
		for (PluginInterface plugin : PluginList.getPlugins()) {
			plugin.exitNode(currentNode, currentNode.getPluginData(plugin.getKey()));
		}
		if (currentNode.isExperiment()) {
			endQuestionnaire();
		}
	}
	private void pauseClock() {
		ClockLabel clock = times.get(currentNode);
		if (clock!= null) {
			clock.pause();
		}
	}
	private void refresh() {
		this.setEnabled(false);
		
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
		String outputString = "<p>Befragung beendet.</p>";
		QuestionTreeXMLHandler.saveXMLAnswerTree(tree, saveDir.getPath()+System.getProperty("file.separator")+Constants.FILE_ANSWERS);
		for (PluginInterface plugin : PluginList.getPlugins()) {
			String tmp = plugin.finishExperiment();
			if (tmp!=null) {
				outputString+="<p>"+tmp+"</p>";
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
}
