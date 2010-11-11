package experimentView.FileTree;

import java.io.File;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


public class FileTreeModel implements TreeModel {
	FileTreeNode root;
	
	public FileTreeModel(File file) {
		 root = new FileTreeNode(file, null);
	}
	public Object getChild(Object parent, int index) {
		return ((FileTreeNode)parent).getChildAt(index);
	}
	public int getChildCount(Object parent) {
		return ((FileTreeNode)parent).getChildCount();
	}
	public int getIndexOfChild(Object parent, Object child) {
		return ((FileTreeNode)parent).getIndex((FileTreeNode)child);
	}
	public Object getRoot() {
		return root;
	} 
	public boolean isLeaf(Object node) {
		return ((FileTreeNode)node).isLeaf();
	}
	public void addTreeModelListener(TreeModelListener l) {
	}
	public void	removeTreeModelListener(TreeModelListener l) {
	}
	public void	valueForPathChanged(TreePath path, Object newValue) {
	}
};
