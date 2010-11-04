package QuestionTree;

import java.util.Enumeration;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class QuestionTreeNode implements MutableTreeNode {
	private String name = "(default)";
	private QuestionTreeNode parent = null;
	private Vector<QuestionTreeNode> children = new Vector<QuestionTreeNode>();
	private TreeMap<String,String> attributes = new TreeMap<String,String>();
	
	public QuestionTreeNode() {
		
	}	
	public QuestionTreeNode(String n) {
		if (!(n.trim().equals(""))) {
			name=n;
		}
	}

	public String getName() {
		return name;
	}
	public boolean setName(String n) {
		if (n.trim().equals("")) {
			return false;
		}
		name=n;
		return true;
	}
	public String toString() {
		return name;
	}
	public boolean isQuestion() {
		return false;
	}
	public boolean isCategory() {
		return false;
	}
	@Override
	public Enumeration<QuestionTreeNode> children() {
		return children.elements();
	}
	@Override
	public boolean getAllowsChildren() {
		return isCategory();
	}
	@Override
	public TreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}
	@Override
	public int getIndex(TreeNode node) {
		return children.indexOf(node);
	}
	@Override
	public TreeNode getParent() {
		return parent;
	}
	@Override
	public boolean isLeaf() {
		return isQuestion();
	}
	@Override
	public int getChildCount() {
		return children.size();
	}
	@Override
	public void insert(MutableTreeNode child, int index) {
		children.insertElementAt((QuestionTreeNode) child, index);
		child.setParent(this);
		
	}
	@Override
	public void remove(int index) {
		children.remove(index);
		
	}
	@Override
	public void remove(MutableTreeNode node) {
		children.remove(node);		
	}
	@Override
	public void removeFromParent() {
		parent.remove(this);		
	}
	@Override
	public void setParent(MutableTreeNode newParent) {
		parent=(QuestionTreeNode) newParent;
		
	}
	@Override
	public void setUserObject(Object object) {
		setName((String)object);
	}
	public TreeMap<String,String> getAttributes() {
		return attributes;
	}
	
	public Vector<QuestionTreeNode> getChildren() {
		return children;
	}
}
