package experimentGUI.util.searchBar;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class SearchBarCtrlFListener extends KeyAdapter {
	private SearchBar searchBar;
	
	public SearchBarCtrlFListener(SearchBar searchBar) {
		this.searchBar=searchBar;
	}
	public void keyPressed(KeyEvent e) {
		if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_F)) {
			searchBar.setVisible(true);
			searchBar.grabFocus();
		}
	}
}
