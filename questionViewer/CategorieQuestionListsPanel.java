package questionViewer;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Creates a panel which consits JList's.
 * The Lists represents categories and every item in the list represents a question
 * @author Markus Köppen, Andreas Hasselberg
 *
 */
public class CategorieQuestionListsPanel extends JScrollPane{

	//list of all JLists
	private ArrayList<JList> categorieLists;
	private JPanel panel;
	
	/**
	 * Constructor which creates an Empty Panel
	 */
	public CategorieQuestionListsPanel() {
		super();
		panel = new JPanel();
		this.setViewportView(panel);
		categorieLists = new ArrayList<JList>();		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));		
		panel.updateUI();
	}
	
	/**
	 * removes all JLists and clears the List which consisted the lists
	 */
	public void removeAll() {
		panel.removeAll();
		categorieLists.clear();
	}
	
	/**
	 * creates a new jList which represents a categorie
	 * the strings in the ArrayList represent the names from the questions
	 * @param categorie Strings with the names of the questions
	 */
	public void addCategorie(ArrayList<String> categorie, int iconType) {
		DefaultListModel listModel = new DefaultListModel();
		for(String question : categorie) {
			listModel.addElement(question);
		}
		JList list = new JList(listModel);
		list.setCellRenderer(new IconCellRenderer(list, iconType));
		list.setEnabled(false);
		list.setBorder(BorderFactory.createEtchedBorder());
		//set size
		int width = (int) this.getParent().getPreferredSize().getWidth()-20;
		list.setPreferredSize(new Dimension(width, 75));
		list.setMinimumSize(new Dimension(width, 75));
		list.setMaximumSize(new Dimension(width, 75));
		
		categorieLists.add(list);
		panel.add(list);
		panel.updateUI();
	}
	
	/**
	 * returns the index of the Categorie with the selected item
	 * @return index of the first list with an selected item, -1 if non ist selected
	 */
	public int getSelectedCategorie() {
		int i = 0;
		for(JList list : categorieLists) {
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
		for(JList list : categorieLists) {
			list.clearSelection();
		}
		categorieLists.get(categorieIndex).setSelectedIndex(questionIndex);
	}
}
