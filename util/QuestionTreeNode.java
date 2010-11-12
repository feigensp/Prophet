package util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class QuestionTreeNode implements MutableTreeNode {
	public final static String TYPE_EXPERIMENT = "experiment";
	public final static String TYPE_CATEGORY = "category";
	public final static String TYPE_QUESTION = "question";
	
	private String name = "(default)";
	private String content = "";
	private String type = "(default)";
	private QuestionTreeNode parent = null;
	private Vector<QuestionTreeNode> children = new Vector<QuestionTreeNode>();
	private HashMap<String,String> attributes = new HashMap<String,String>();
	private HashMap<String,Vector<StringTuple>> results;
	
	public QuestionTreeNode() {
		this("");
	}
	public QuestionTreeNode(String t) {
		this(t,"");
	}
	public QuestionTreeNode(String t, String n) {
		if (!(t.trim().equals(""))) {
			type=t;
		}
		if (!(n.trim().equals(""))) {
			name=n;
		}
	}
	public boolean isExperiment() {
		return type.equals(TYPE_EXPERIMENT);
	}
	public boolean isCategory() {
		return type.equals(TYPE_CATEGORY);
	}
	public boolean isQuestion() {
		return type.equals(TYPE_QUESTION);
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
	
	@Override
	public Enumeration<QuestionTreeNode> children() {
		return children.elements();
	}
	@Override
	public boolean getAllowsChildren() {
		return !isLeaf();
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
	public Set<Entry<String,String>> attributes() {
		return attributes.entrySet();
	}
	public String getAttribute(String key) {
		return attributes.get(key);
	}
	public String setAttribute(String key, String value) {
		return attributes.put(key, value);
	}
	public Vector<QuestionTreeNode> getChildren() {
		return children;
	}
	public boolean hasContent() {
		return content!=null;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String c) {
		content=c;
	}
	public String getType() {
		return type;
	}
	public void setType(String t) {
		type=t;
	}
}
