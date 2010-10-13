package experimentQuestionCreator;

import java.util.Vector;


public class TreeNode {	
	
	private String text;
	private int model;
	private int x, y;
	private int childCount;
	private Vector<TreeNode> children;
	private TreeNode parent;
	
	//Konstrukor für Wurzel
	public TreeNode() {
		children = new Vector<TreeNode>();
		this.parent = null;
		this.model = -1;
	}
	//Konstruktor für eine Seite/Frage
	public TreeNode(TreeNode parent, String text) {
		children = new Vector<TreeNode>();
		this.parent = parent;
		this.model = -1;
		this.text = text;
	}
	//Konstruktor für ein Element
	public TreeNode(TreeNode parent, String text, int component, int x, int y) {
		children = new Vector<TreeNode>();
		this.parent = parent;
		this.text = text;
		this.model = component;
		this.x = x;
		this.y = y;
	}
	
	public TreeNode getParent() {
		return parent;
	}
	
	public boolean isRoot() {
		return (parent == null);
	}
	
	public void addChild(TreeNode tn) {
		children.add(tn);
		childCount++;
	}
	
	public String getText() {
		return text;
	}

	public int getModel() {
		return model;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Vector<TreeNode> getChildren() {
		return children;
	}

	public Vector<TreeNode> getChild() {
		return children;
	}
		
	public TreeNode getChild(int index) {
		return children.get(index);
	}
	
	public int getChildCount() {
		return childCount;
	}
	
	public boolean isLeaf() {
		return (childCount == 0);
	}
}
