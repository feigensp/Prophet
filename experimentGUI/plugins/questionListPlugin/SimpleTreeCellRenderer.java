package experimentGUI.plugins.questionListPlugin;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class SimpleTreeCellRenderer extends DefaultTreeCellRenderer{

	public static int i = 0;

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected1,
			boolean expanded, boolean leaf, int row, boolean hasFocus1) {
		super.getTreeCellRendererComponent(tree, value, selected1, expanded, leaf, row, hasFocus1);
		setEnabled(true);
		setIcon(null);
		return this;
	}
}
