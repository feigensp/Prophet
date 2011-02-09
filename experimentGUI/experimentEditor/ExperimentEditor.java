package experimentGUI.experimentEditor;

/**
 * this class is contains the main for the editor to create and edit questions
 * furthermore it creates the view and some listeners
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeNodeEvent;
import experimentGUI.util.questionTreeNode.QuestionTreeNodeListener;

/**
 * An ExperimentEditor is a frame which allows its user to create and edit experiments usable in the ExperimentViewer
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class ExperimentEditor extends JFrame {
	public static final String TITLE = "ExperimentEditor";
	
	private static final long serialVersionUID = 1L;	
	/**
	 * JTree component to the left of the ExperimentEditor
	 */
	private QuestionTree tree;
	/**
	 * JTabbedPane component to the right of the ExperimentEditor
	 */
	private ExperimentEditorTabbedPane questionEditorTabbedPane;
	/**
	 * ContentPane of the ExperimentEditor, all components are put here
	 */
	private JPanel contentPane;

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
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setTitle(TITLE);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		ExperimentEditorMenuBar menuBar = new ExperimentEditorMenuBar(this);
		setJMenuBar(menuBar);

		tree = new QuestionTree();
		tree.setPreferredSize(new Dimension(175, 10));
		tree.addQuestionTreeNodeListener(new QuestionTreeNodeListener() {
			public void questionTreeEventOccured(QuestionTreeNodeEvent e) {
				questionEditorTabbedPane.setSelected(e.getNode());
			}			
		});		
		contentPane.add(tree, BorderLayout.WEST);

		questionEditorTabbedPane = new ExperimentEditorTabbedPane();
		contentPane.add(questionEditorTabbedPane, BorderLayout.CENTER);
	}
	/**
	 * 
	 * @return
	 * 	The JTree that represents the question tree
	 */
	public QuestionTree getTreeComponent() {
		return tree;
	}
	/**
	 * Tells the QuestionTree to create a new tree, called when "New" item is selected in menu
	 */
	public void newTree() {
		tree.newRoot();
	}
	/**
	 * Loads a question tree into the JTree component
	 * @param root
	 */
	public void loadTree(QuestionTreeNode root) {
		tree.setRoot(root);
	}
	
	public ExperimentEditorTabbedPane getTabbedPane() {
		return questionEditorTabbedPane;
	}
}
