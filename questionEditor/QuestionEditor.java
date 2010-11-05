package questionEditor;

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

import questionEditor.CatEdit.CategoryEditorPanel;
import questionEditor.QuestEdit.QuestionEditorPanel;
import questionEditor.QuestionTree.QuestionTree;
import questionEditor.QuestionTree.QuestionTreeEvent;
import questionEditor.QuestionTree.QuestionTreeListener;
import questionEditor.QuestionTree.QuestionTreeNode;


@SuppressWarnings("serial")
public class QuestionEditor extends JFrame {
	private QuestionTree tree;
	private QuestionEditorPanel questionEditorPanel;
	private CategoryEditorPanel categoryEditorPanel;

	private QuestionTreeNode selected;

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
		setBounds(100, 100, 611, 431);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		QuestionEditorMenuBar menuBar = new QuestionEditorMenuBar(this);
		setJMenuBar(menuBar);
		
		build(null);
	}
	
	public QuestionTree getTree() {
		return tree;
	}

	public void removeEditorElements() {
		if (questionEditorPanel!=null) {
			contentPane.remove(questionEditorPanel);
			questionEditorPanel=null;
		}
		if (categoryEditorPanel!=null) {
			contentPane.remove(categoryEditorPanel);
			categoryEditorPanel=null;
		}
		contentPane.updateUI();
	}
	
	public void build(QuestionTreeNode root) {
		selected=null;
		contentPane.removeAll();
		contentPane.updateUI();
		
		tree = new QuestionTree(root);
		tree.setPreferredSize(new Dimension(175, 10));
		tree.addQuestionTreeListener(new QuestionTreeListener() {
			public void questionTreeEventOccured(QuestionTreeEvent e) {
				removeEditorElements();
				selected=e.getNode();
				if (e.getNode().isCategory()) {
					categoryEditorPanel = new CategoryEditorPanel(selected);
					contentPane.add(categoryEditorPanel);
				} else if (e.getNode().isQuestion()) {	
					questionEditorPanel = new QuestionEditorPanel(selected); 
					contentPane.add(questionEditorPanel);
				}
			}			
		});		
		contentPane.add(tree, BorderLayout.WEST);

		questionEditorPanel = new QuestionEditorPanel(null);
		contentPane.add(questionEditorPanel, BorderLayout.CENTER);
	}

}
