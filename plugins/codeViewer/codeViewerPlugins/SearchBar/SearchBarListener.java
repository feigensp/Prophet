package plugins.codeViewer.codeViewerPlugins.SearchBar;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SearchBarListener extends KeyAdapter {
	SearchBar searchBar;
	
	public SearchBarListener(SearchBar searchBar) {
		this.searchBar=searchBar;
	}
	public void keyPressed(KeyEvent e) {
		if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_F)) {
			searchBar.setVisible(true);
			searchBar.grabFocus();
		}
	}
}
