package experimentGUI.plugins.questionListPlugin;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class SimpleTreeCellRenderer extends DefaultTreeCellRenderer{
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected1,
			boolean expanded, boolean leaf, int row, boolean hasFocus1) {
		super.getTreeCellRendererComponent(tree, value, selected1, expanded, leaf, row, hasFocus1);
		setEnabled(true);
		setIcon(new ListIcon(ListIcon.CIRCLE));
		return this;
	}
}
