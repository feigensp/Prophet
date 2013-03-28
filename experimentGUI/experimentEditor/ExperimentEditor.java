package experimentGUI.experimentEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import experimentGUI.util.language.UIElementNames;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeNodeEvent;
import experimentGUI.util.questionTreeNode.QuestionTreeNodeListener;

/**
 * An ExperimentEditor is a frame which allows its user to create and edit
 * experiments usable in the ExperimentViewer
 * 
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class ExperimentEditor extends JFrame {
	/**
	 * The window title for the main frame
	 */
	public static final String TITLE = "ExperimentEditor";

	private static final long serialVersionUID = 1L;

	/**
	 * JTree component on the left side of the ExperimentEditor
	 */
	private QuestionTree tree;

	/**
	 * JTabbedPane component on the right side of the ExperimentEditor
	 */
	private ExperimentEditorTabbedPane questionEditorTabbedPane;

	/**
	 * Main method to launch the ExperimentEditor
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
					ExperimentEditor frame = new ExperimentEditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor of the ExperimentEditor, called by the main() method;<br>
	 * sets some basic settings and adds used components
	 */
	public ExperimentEditor() {
		String selectedLanguage = getPreferredLanguage();
		
		// TODO initialize with selected language
		Locale locale = Locale.ENGLISH;
		if (selectedLanguage.equals("English"))
			locale = Locale.ENGLISH;
		else if (selectedLanguage.equals("German"))
			locale = Locale.GERMAN;
		
		UIElementNames.setUIElements(locale);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setTitle(TITLE);

		JSplitPane splitPane = new JSplitPane();
		add(splitPane);

		ExperimentEditorMenuBar menuBar = new ExperimentEditorMenuBar(this);
		setJMenuBar(menuBar);

		tree = new QuestionTree();
		tree.setPreferredSize(new Dimension(175, 10));
		tree.addQuestionTreeNodeListener(new QuestionTreeNodeListener() {
			public void questionTreeEventOccured(QuestionTreeNodeEvent e) {
				questionEditorTabbedPane.setSelected(e.getNode());
			}
		});
		tree.setBorder(null);
		splitPane.setLeftComponent(tree);

		questionEditorTabbedPane = new ExperimentEditorTabbedPane();
		questionEditorTabbedPane.setBorder(null);
		splitPane.setRightComponent(questionEditorTabbedPane);

		splitPane.setBorder(null);
		for (Component component : splitPane.getComponents())
			if (component instanceof BasicSplitPaneDivider)
				((BasicSplitPaneDivider) component).setBorder(null);

		add(splitPane, BorderLayout.CENTER);
	}

	private String getPreferredLanguage() {
		Object[] possibleValues = { "German", "English" };
		Object selectedLanguage = JOptionPane.showInputDialog(null,
				"Select a language for PROPHET", "Input",
				JOptionPane.INFORMATION_MESSAGE, null, possibleValues,
				possibleValues[0]);
		if (selectedLanguage == null)
			selectedLanguage = "English";
		System.out.println("Language: " + selectedLanguage);

		return selectedLanguage.toString();
	}

	/**
	 * 
	 * @return The JTree that represents the question tree
	 */
	public QuestionTree getTreeComponent() {
		return tree;
	}

	/**
	 * Tells the QuestionTree to create a new tree, called when the "New" item
	 * is selected in main menu
	 */
	public void newTree() {
		tree.newRoot();
	}

	/**
	 * Loads a question tree into the JTree component
	 * 
	 * @param root
	 */
	public void loadTree(QuestionTreeNode root) {
		tree.setRoot(root);
	}

	/**
	 * return the ExperimentEditorTabbedPane-object from the ExperimentEditor
	 * 
	 * @return questionEditorTabbedPane
	 */
	public ExperimentEditorTabbedPane getTabbedPane() {
		return questionEditorTabbedPane;
	}
}
