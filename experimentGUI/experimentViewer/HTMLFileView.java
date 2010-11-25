package experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;

import experimentGUI.PluginInterface;
import experimentGUI.PluginList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.XMLTreeHandler;




/**
 * This class shows the html files (questions) creates the navigation and
 * navigates everything...
 * 
 * @author Markus Köppen, Andreas Hasselberg
 * 
 */
public class HTMLFileView extends JPanel {
	QuestionTreeNode experimentTree;
	//the textpanes (one is one question)
	private HashMap<QuestionTreeNode,QuestionViewPane> textPanes;
	//time objects
	private JPanel timePanel;
	private HashMap<QuestionTreeNode,ClockLabel> times;
	private ClockLabel totalTime;
	//nodes of the question tree
	QuestionTreeNode rootNode;
	QuestionTreeNode currentNode;
	
	ActionListener myActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			String command = arg0.getActionCommand();
			if (command.equals(QuestionViewPane.MODE_BACKWARD)) {
				previousNode();
			} else if (command.equals(QuestionViewPane.MODE_FORWARD)) {
				nextNode();
			}
		}		
	};

	/**
	 * With the call of the Constructor the data is loaded and everything is
	 * initialized. The first question is showed.
	 * 
	 * @param path
	 *            path of the xml file with the data
	 * @param cqlp
	 *            the categorieQuestionListsPanel where the overview is shown
	 */
	public HTMLFileView(QuestionTreeNode tree) {
		super();
		rootNode=tree;
	}

	/**
	 * starts the questionnaire with the first available Question
	 */
	public void start() {
		QuestionTreeNode superRoot = new QuestionTreeNode();
		superRoot.add(rootNode);
		rootNode.setParent(null);
		currentNode=superRoot;
		textPanes = new HashMap<QuestionTreeNode,QuestionViewPane>();
		times = new HashMap<QuestionTreeNode,ClockLabel>();
		totalTime = new ClockLabel("Gesamtzeit");
		totalTime.start();
		timePanel = new JPanel();
		this.setLayout(new BorderLayout());
		nextNode();
	}
	
	private boolean nextNode() {
		pauseClock();
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
		inactive = Boolean.parseBoolean(currentNode.getAttributeValue("inactive"));
		if (inactive) {
			return nextNode();
		} else {
			boolean doNotShowContent = Boolean.parseBoolean(currentNode.getAttributeValue("donotshowcontent"));
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
				boolean inactive = Boolean.parseBoolean(tempNode.getAttributeValue("inactive"));
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
			currentNode.setPluginData(plugin.getKey(), plugin.enterNode(currentNode, this));
		}
	}
	private void exitNode() {
		for (PluginInterface plugin : PluginList.getPlugins()) {
			plugin.exitNode(currentNode, this, currentNode.getPluginData(plugin.getKey()));
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
		this.removeAll();
		updateUI();
		if (currentNode==null) {
			return;
		}
		QuestionViewPane viewPane = textPanes.get(currentNode);
		if (viewPane==null) {
			viewPane = new QuestionViewPane(currentNode);
			viewPane.setActionListener(myActionListener);
			textPanes.put(currentNode, viewPane);
		} 
		this.add(viewPane, BorderLayout.CENTER);
		
		ClockLabel clock = times.get(currentNode);
		if (clock==null) {
			clock = new ClockLabel(currentNode, "Aktuell");
			times.put(currentNode, clock);
			clock.start();
		} else {
			clock.resume();
		}
		timePanel.removeAll();
		timePanel.updateUI();
		timePanel.add(clock);
		timePanel.add(totalTime);
		this.add(timePanel, BorderLayout.SOUTH);
	}

	/**
	 * method which is called when the last question is answered
	 */
	private void endQuestionnaire() {
		System.out.println("Beende Befragung");
		XMLTreeHandler.saveXMLAnswerTree(rootNode, "answers.xml");
		this.removeAll();
		this.updateUI();
	}
}
