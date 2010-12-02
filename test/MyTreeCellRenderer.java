package test;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected1,
			boolean expanded, boolean leaf, int row, boolean hasFocus1) {
		//call the normal TreeCellRendererComponent() method
		super.getTreeCellRendererComponent(tree, value, selected1, expanded, leaf, row, hasFocus1);
		//enable set Component to draw it normally
		this.setEnabled(true);
		return this;
	}
}
