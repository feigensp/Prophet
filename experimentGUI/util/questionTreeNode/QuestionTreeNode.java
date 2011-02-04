package experimentGUI.util.questionTreeNode;

import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class QuestionTreeNode extends DefaultMutableTreeNode {
	public final static String TYPE_EXPERIMENT = "experiment";
	public final static String TYPE_CATEGORY = "category";
	public final static String TYPE_QUESTION = "question";
	public final static String TYPE_ATTRIBUTE = "attribute";
	
	private String type;
	private String name;
	private String value;

	private TreeMap<String,QuestionTreeNode> attributes;
	private TreeMap<String,String> answers;
	
	long answerTime = 0;
	public QuestionTreeNode() {
		this("");
	}
	public QuestionTreeNode(String t) {
		this(t,"");
	}
	public QuestionTreeNode(String t, String n) {
		value = "";
		attributes = new TreeMap<String,QuestionTreeNode>();
		answers = new TreeMap<String,String>();
		
		type = "(default)";
		setType(t);
		name = "(default)";
		setName(n);
	}
	
	public long getAnswerTime() {
		return answerTime;
	}
	public void setAnswerTime(long answerTime) {
		this.answerTime = answerTime;
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
	public boolean isAttribute() {
		return type.equals(TYPE_ATTRIBUTE);
	}

	public String getName() {
		return name;
	}
	public boolean setName(String n) {
		if (n==null || n.trim().equals("")) {
			return false;
		}
		name=n;
		return true;
	}
	public String toString() {
		return name;
//		String ret = name + " { ";
//		for(int i=0; i<this.getChildCount(); i++) {
//			ret += this.getChildAt(i).toString();  
//		}
//		ret += " } ";
//		return ret;
	}
	
	public boolean isLeaf() {
		return isQuestion();
	}
	@Override
	public void setUserObject(Object object) {
		setName((String)object);
	}
	public TreeMap<String,QuestionTreeNode> getAttributes() {
		return attributes;
	}
	public String getAttributeValue(String key) {
		QuestionTreeNode attribute = attributes.get(key);
		return attribute == null ? null : attribute.getValue();
	}
	public QuestionTreeNode getAddAttribute(String key) {
		QuestionTreeNode attribute = attributes.get(key);
		if (attribute == null) {
			attribute = new QuestionTreeNode(TYPE_ATTRIBUTE, key);
			attributes.put(key, attribute);
		}
		return attribute;
	}
	public void setAttribute(String key, QuestionTreeNode attribute) {
		attributes.put(key, attribute);
	}
	public QuestionTreeNode getAttribute(String key) {
		return attributes.get(key);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String v) {
		if (v!=null) {
			value=v;
		}
	}
	public String getType() {
		return type;
	}
	public boolean setType(String t) {
		if (t==null || t.trim().equals("")) {
			return false;
		}
		type=t;
		return true;
	}
	public void setAnswers(TreeMap<String,String> answers) {
		this.answers = answers;
	}
	public void setAnswer(String key, String value) {
		answers.put(key, value);
	}
	public String getAnswer(String key) {
		return answers.get(key);
	}
	public TreeMap<String,String> getAnswers() {
		return answers;
	}
	
	public QuestionTreeNode copy() {
		QuestionTreeNode ret = new QuestionTreeNode();
		ret.type=this.type;
		ret.name=this.name;
		ret.value=this.value;
		for (String key : answers.keySet()) {
			ret.answers.put(key, answers.get(key));
		}
		for (String key : attributes.keySet()) {
			ret.attributes.put(key, attributes.get(key).copy());
		}
		for(int i=0; i<this.getChildCount(); i++) {
			ret.add(((QuestionTreeNode) this.getChildAt(i)).copy());
		}
		return ret;
	}
}
