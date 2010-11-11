package experimentEditor;

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
import javax.swing.border.EmptyBorder;

import util.QuestionTreeNode;
import experimentEditor.QuestionTree.QuestionTree;
import experimentEditor.QuestionTree.QuestionTreeEvent;
import experimentEditor.QuestionTree.QuestionTreeListener;
import experimentEditor.tabbedPane.QuestionEditorTabbedPane;


@SuppressWarnings("serial")
public class QuestionEditor extends JFrame {
	private QuestionTree tree;
	private QuestionEditorTabbedPane questionEditorTabbedPane;

	private JPanel contentPane;

	/**
	 * the main method to launch the application
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestionEditor frame = new QuestionEditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * the constructor, sets some basic settings and start the method to create
	 * the view
	 */
	public QuestionEditor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		QuestionEditorMenuBar menuBar = new QuestionEditorMenuBar(this);
		setJMenuBar(menuBar);
		
		build();
	}
	
	public QuestionTree getTree() {
		return tree;
	}
	
	public void build() {		
		tree = new QuestionTree();
		tree.setPreferredSize(new Dimension(175, 10));
		tree.addQuestionTreeListener(new QuestionTreeListener() {
			public void questionTreeEventOccured(QuestionTreeEvent e) {
				questionEditorTabbedPane.setSelected(e.getNode());
			}			
		});		
		contentPane.add(tree, BorderLayout.WEST);

		questionEditorTabbedPane = new QuestionEditorTabbedPane();
		contentPane.add(questionEditorTabbedPane, BorderLayout.CENTER);
	}
	public void newTree() {
		tree.newRoot();
	}
	public void loadTree(QuestionTreeNode root) {
		tree.setRoot(root);
	}
}
