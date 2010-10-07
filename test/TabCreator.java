package test;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

public class TabCreator {
	
	private static JTabbedPane tabbedPane;
	private static TabComponents tabComp;
	
	public TabCreator(JTabbedPane tabbedPane) {
		TabCreator.tabbedPane = tabbedPane; 
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
		tabbedPane.setSelectedIndex(famIndex);
	}
	
	public void closeTab(String filename) {
		int famIndex = tabComp.getFamIndex(filename);
		tabbedPane.remove(tabComp.getPanel(famIndex));
		tabComp.removeComponentFamily(famIndex);
	}

}
