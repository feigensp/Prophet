package questionEditor.QuestionTree;

import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class QuestionTreeNode implements MutableTreeNode {
	private String name = "(default)";
	private String content = "";
	private boolean isQ = false;
	private boolean isC = false;
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
		return isQ;
	}
	public void setQuestion(boolean q) {
		isQ=q;
	}
	public boolean isCategory() {
		return isC;
	}
	public void setCategory(boolean c) {
		isC=c;
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
}
