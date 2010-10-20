/**
 * Diese Klasse baut Komponenten in einem Panel Tabellenartig auf
 * Dabei können verschiedene Komponenten eingefügt werden.
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package test;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class Table extends JPanel{
	
	private int x, y;	//Anzahl der Spalten und Zeilen
	private ArrayList<ArrayList<JComponent>> components;
	private int cellSizeX, cellSizeY;
	
	public Table() {
		super();
		x = 0;
		y = 0;
		components = new ArrayList<ArrayList<JComponent>>();
		cellSizeX = 0;
		cellSizeY = 0;
	}
	
	/**
	 * Fügt ein Komponent zur Tabelle hinzu
	 * @param comp Komponente die hinzugefügt werden soll
	 * @param x Spalte
	 * @param y Zeile
	 */
	public void addComponent(JComponent comp, int x, int y) {
		//Zuerst wird das Element in die verschachtelte ArrayList eingefügt
		//dazu muss getestet werden ob sie den jetzigen Bereich überschreitet
		if(x>this.x) {
			//überzählige Spalten auffüllen
			for(int i = this.x; i <= x; i++) {
				components.add(new ArrayList<JComponent>());
			}
			//überzählige Zellen in allen Spalten einfügen
			for(int i=0; i<x; i++) {
				for(int j=components.get(x).size(); j<y; j++) {
					components.get(x).add(null);					
				}
			}
			//Element einfügen
			components.get(x).add(comp);
			this.x = x;
			this.y = Math.max(y, this.y);
			return;
		}
		if(y> this.y) {
			//überzählige Zellen in der Spalte einfügen
			for(int i=0; i<y; i++) {
				components.get(x).add(null);
			}
			//Element einfügen
			components.get(x).add(comp);	
			this.y = y;
			return;
		}
		//Ansonsten Element das derzeit dort ist löschen und dann neues einfügen
		components.get(x).remove(y);
		components.get(x).add(y, comp);
		//Tabelle neu malen
		createTable();
	}
	
	private void createTable() {
		//Alle elemente Löschen
		this.removeAll();
		//Größte Abmaße für die Zellengröße herausfinden
		Dimension compSize;
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				if(components.get(x).get(y) != null) {
					compSize = components.get(x).get(y).getSize();
					cellSizeX = Math.max(cellSizeX, (int)compSize.getWidth());
					cellSizeY = Math.max(cellSizeY, (int)compSize.getHeight());
				}
			}
		}
		//Formlayout aufbauen und komponenten hinzufügen
		String cols = "";
		for(int i=0; i<=x; i++) {
			cols += cellSizeX + "px, ";
		}
		cols.substring(0, cols.length()-2);
		String rows = "";
		for(int i=0; i<=y; i++) {
			rows += cellSizeY + "px, ";
		}
		rows.substring(0, rows.length()-2);
		FormLayout formLayout = new FormLayout(cols, rows);
		this.setLayout(formLayout);
		CellConstraints cc = new CellConstraints();
		//Componenten hinzufügen
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				if(components.get(x).get(y) != null) {
					this.add(components.get(x).get(y), cc.xy(i, j, CellConstraints.LEFT, CellConstraints.TOP));
				}
			}
		}		
	}
	
}
