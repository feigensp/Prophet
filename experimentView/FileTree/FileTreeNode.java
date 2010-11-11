package experimentView.FileTree;

import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;


public class FileTreeNode implements TreeNode {	
	File file;
	FileTreeNode parent;
	boolean isFile, extended;
	int childCount;
	Vector<FileTreeNode> children;
	
	public FileTreeNode(File f, FileTreeNode p) {
		file = f;
		parent = p;
		isFile = f.isFile();
		extended = false;
	}
	public File getFile() {
		return file;
	}
	public String toString() {
		return file.getName();
	}
	private void extend() {
		if (extended) {
			return;
		}
		File[] content = file.listFiles();
		Arrays.sort(content);
		Vector<FileTreeNode> dirs = new Vector<FileTreeNode>();
		Vector<FileTreeNode> files = new Vector<FileTreeNode>();
		for (File f : content) {
			if (f.isDirectory()) {
				dirs.add(new FileTreeNode(f, this));
			} else {
				files.add(new FileTreeNode(f, this));
			}
		}
		children=new Vector<FileTreeNode>();
		children.addAll(dirs);
		children.addAll(files);
		childCount=children.size();
		extended=true;
	}
	public int getChildCount() {
		if (!extended) {
			extend();
		}
		return childCount;
	}
	public boolean isLeaf() {
		return isFile;
	}
	@Override
	public Enumeration<FileTreeNode> children() {
		if (!extended) {
			extend();
		}
		return children.elements();
	}
	@Override
	public boolean getAllowsChildren() {
		return !isLeaf();
	}
	@Override
	public TreeNode getChildAt(int childIndex) {
		if (!extended) {
			extend();
		}
		return children.get(childIndex);
	}
	@Override
	public int getIndex(TreeNode node) {
		if (!extended) {
			extend();
		}
		return children.indexOf(node);
	}
	@Override
	public TreeNode getParent() {
		return parent;
	}
}
