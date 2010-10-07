package test;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

public class TabCreator {
	
	private static JTabbedPane tabbedPane;
	private static TabComponents tabComp;
	
	public TabCreator(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane; 
		tabComp = new TabComponents();
	}
	
	public TabCreator() {
	}
	
	public void createTab(String filename) {
		int famIndex = tabComp.addComponentFamily(filename);
		tabbedPane.addTab(filename, tabComp.getPanel(famIndex));
		tabComp.getPanel(famIndex).setLayout(new BorderLayout());
		tabComp.getPanel(famIndex).add(tabComp.getTextArea(famIndex), BorderLayout.CENTER);
		tabComp.getPanel(famIndex).add(tabComp.getSearchField(famIndex), BorderLayout.SOUTH);
		tabComp.getSearchField(famIndex).setVisible(false);
	}
	
	public void closeTab(String filename) {
		//famIndex extrahieren
		int famIndex = tabComp.getFamIndex(filename);
		tabbedPane.remove(famIndex);
		tabComp.removeComponentFamily(famIndex);
	}

}
