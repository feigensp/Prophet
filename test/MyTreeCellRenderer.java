package test;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

	public static int i = 0;
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected1,
			boolean expanded, boolean leaf, int row, boolean hasFocus1) {
		// call the normal TreeCellRendererComponent() method
		super.getTreeCellRendererComponent(tree, value, selected1, expanded, leaf, row, hasFocus1);
		setEnabled(true);
		setIcon(null);
//		if(value.toString().equals("blue")) {
//			this.setForeground(Color.red);
//		}
//		System.out.println("" + (i++) + tree.getPathForRow(row));
		// enable set Component to draw it normally
		return this;
	}
}
