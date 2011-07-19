package experimentGUI.plugins.codeViewerPlugin.fileTree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class FileTreeModel extends DefaultTreeModel {
	private static final long serialVersionUID = 1L;

	public FileTreeModel(TreeNode arg0) {
		super(arg0);
	}
	
	@Override
	public boolean isLeaf(Object node) {
		return ((FileTreeNode)node).isFile();
	}

}
