package questionViewer;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CategorieQuestionListsPanel extends JScrollPane{

	private ArrayList<JList> categorieLists;
	private JPanel panel;
	
	public CategorieQuestionListsPanel() {
		super();
		panel = new JPanel();
		this.setViewportView(panel);
		categorieLists = new ArrayList<JList>();
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));		
		panel.updateUI();
	}
	
	public void removeAll() {
		panel.removeAll();
		categorieLists.clear();
	}
	
	/**
	 * creates a new jList which represents a categorie
	 * the string sin the ArrayList represent the names from the questions
	 * @param categorie Strings with the names of the questions
	 */
	public void addCategorie(ArrayList<String> categorie, boolean enabled) {
		DefaultListModel listModel = new DefaultListModel();
		for(String question : categorie) {
			listModel.addElement(question);
		}
		JList list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setEnabled(enabled);
		if(enabled) {
			list.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg0) {
					//event aufrufen
				}				
			});
		}
		list.setBorder(BorderFactory.createEtchedBorder());
		int width = (int) this.getParent().getPreferredSize().getWidth()-5;
		list.setPreferredSize(new Dimension(width, 75));
		list.setMinimumSize(new Dimension(width, 75));
		list.setMaximumSize(new Dimension(width, 75));
		categorieLists.add(list);		
		panel.add(list);

		panel.updateUI();
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
