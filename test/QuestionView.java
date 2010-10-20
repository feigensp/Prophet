/**
 * Diese Klasse fügt verschiedene Elemente in ein Panel ein.
 * Dabei wird ein vertikales BoxLayout als Grundstruktur verwendet.
 * In den einzelnen Ebenen kann dann entweder ein neues BoxLayout (horizontal)
 * oder TableLayout hinzugefügt werden.
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package test;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class QuestionView extends JPanel{

	private ArrayList<PanelContainer> rows;
	
	public QuestionView() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.setAlignmentY(TOP_ALIGNMENT);
		rows = new ArrayList<PanelContainer>();		
	}
	
	public void addRow() {
		PanelContainer pc = new PanelContainer();
		rows.add(pc);
		this.add(pc);
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.setAlignmentY(TOP_ALIGNMENT);
	}
	
	public PanelContainer getRow(int i) {
		return rows.get(i);
	}

}
