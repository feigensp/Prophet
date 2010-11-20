package plugins.questionLists;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import util.QuestionTreeNode;

/**
 * Creates a panel which consits JList's.
 * The Lists represents categories and every item in the list represents a question
 * @author Markus Köppen, Andreas Hasselberg
 *
 */
public class QuestionListPanel extends JScrollPane{
	private QuestionTreeNode experimentTree;

	//list of all JLists
	private ArrayList<JList> categoryList;
	private JPanel panel;
	
	/**
	 * Constructor which creates an Empty Panel
	 */
	public QuestionListPanel() {
		super();		
		panel = new JPanel();
		this.setViewportView(panel);
		categoryList = new ArrayList<JList>();		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));		
		panel.updateUI();
	}
	
	public void addCategories(QuestionTreeNode tree) {
		experimentTree=tree;
		removeAll();
		for (int i=0; i<experimentTree.getChildCount();i++) {
			QuestionTreeNode category = (QuestionTreeNode)experimentTree.getChildAt(i);
			ArrayList<String> questions = new ArrayList<String>();
			for (int j=0; j<category.getChildCount();j++) {
				QuestionTreeNode question = (QuestionTreeNode)category.getChildAt(j);
				questions.add(question.getName());
			}
			addCategory(questions,Boolean.parseBoolean(category.getAttributeValue("questionswitching")) ? IconCellRenderer.ListIcons.UPDOWNARROW : IconCellRenderer.ListIcons.DOWNARROW);
		}
	}

	/**
	 * removes all JLists and clears the List which consisted the lists
	 */
	public void removeAll() {
		panel.removeAll();
		categoryList.clear();
		panel.updateUI();
	}
	
	/**
	 * creates a new jList which represents a categorie
	 * the strings in the ArrayList represent the names from the questions
	 * @param category Strings with the names of the questions
	 */
	public void addCategory(ArrayList<String> category, int iconType) {
		DefaultListModel listModel = new DefaultListModel();
		for(String question : category) {
			listModel.addElement(question);
		}
		JList list = new JList(listModel);
		//TODO: In Feature kapseln
		list.setCellRenderer(new IconCellRenderer(list, iconType));
		list.setEnabled(false);
		list.setBorder(BorderFactory.createEtchedBorder());
		//set size
		int width = (int) this.getParent().getPreferredSize().getWidth()-25;
		list.setPreferredSize(new Dimension(width, 75));
		list.setMinimumSize(new Dimension(width, 75));
		list.setMaximumSize(new Dimension(width, 75));
		
		categoryList.add(list);
		panel.add(list);
		panel.updateUI();
	}
	
	/**
	 * returns the index of the Categorie with the selected item
	 * @return index of the first list with an selected item, -1 if non ist selected
	 */
	public int getSelectedCategory() {
		int i = 0;
		for(JList list : categoryList) {
			if(list.getSelectedIndex() != -1) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	/**
	 * selects an question an deselect every else
	 * @param categorieIndex index of the categorie which consists the question
	 * @param questionIndex index of the question in his categorie
	 */
	public void selectQuestion(int categorieIndex, int questionIndex) {
		for(JList list : categoryList) {
			list.clearSelection();
		}
		categoryList.get(categorieIndex).setSelectedIndex(questionIndex);
	}
}
