package questionViewer;

import java.awt.AWTEvent;

import javax.swing.JList;

public class ListSelectionChangedEvent extends AWTEvent{

    int categorie, question;
    
    public static final int LIST_SELECTION_CHANGED = RESERVED_ID_MAX + 1;
    
    ListSelectionChangedEvent(JList source, int id, int categorie, int question) {
        super(source, id);
        this.categorie = categorie;
        this.question = question;
    }
    public int getCategorie() {
        return categorie;
    }
    public int getQuestion() {
    	return question;
    }


}
