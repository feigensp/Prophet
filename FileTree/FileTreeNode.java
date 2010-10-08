package FileTree;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;


public class FileTreeNode {	
	File file;
	boolean isFile, extended;
	int childCount;
	Vector<FileTreeNode> children;
	
	public FileTreeNode(File f) {
		file = f;
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
				dirs.add(new FileTreeNode(f));
			} else {
				files.add(new FileTreeNode(f));
			}
		}
		children=new Vector<FileTreeNode>();
		children.addAll(dirs);
		children.addAll(files);
		childCount=children.size();
		extended=true;
	}
	public FileTreeNode getChild(int index) {
		if (!extended) {
			extend();
		}
		return children.get(index);
	}
	public int getChildCount() {
		if (!extended) {
			extend();
		}
		return childCount;
	}
	public int getIndexOfChild(Object child) {
		if (!extended) {
			extend();
		}
		return children.indexOf(child);
	}
	public boolean isLeaf() {
		return isFile;
	}
}
