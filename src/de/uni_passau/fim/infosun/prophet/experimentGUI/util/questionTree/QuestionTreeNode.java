package de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree;

import java.util.Objects;
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

    private TreeMap<String, QuestionTreeNode> attributes;
    private TreeMap<String, String> answers;

    long answerTime = 0;

    /**
     * Creates a new <code>QuestionTreeNode</code> with (default) as type and name.
     */
    public QuestionTreeNode() {
        this("");
    }

    /**
     * Creates a new <code>QuestionTreeNode</code> with the given <code>type</code> and (default) as name.
     *
     * @param type
     *         the type of this node
     *
     * @throws java.lang.NullPointerException
     *         if <code>type</code> was <code>null</code>
     */
    public QuestionTreeNode(String type) {
        this(type, "");
    }

    /**
     * Creates a new <code>QuestionTreeNode</code> with the given <code>type</code> and <code>name</code>.
     *
     * @param type
     *         the type of this node
     * @param name
     *         the name of this node
     *
     * @throws java.lang.NullPointerException
     *         if <code>type</code> or <code>name</code> was <code>null</code>
     */
    public QuestionTreeNode(String type, String name) {
        Objects.requireNonNull(type, "type must not be null!");
        Objects.requireNonNull(name, "name must not be null!");

        value = "";
        attributes = new TreeMap<>();
        answers = new TreeMap<>();

        this.type = "(default)";
        setType(type);
        this.name = "(default)";
        setName(name);
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

    public boolean setName(String name) {
        if (name == null || name.trim().equals("")) {
            return false;
        }
        this.name = name;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isLeaf() {
        return isQuestion();
    }

    @Override
    public void setUserObject(Object name) {

        if (name instanceof String) {
            setName((String) name);
        }
    }

    public TreeMap<String, QuestionTreeNode> getAttributes() {
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

    public void setValue(String value) {

        if (value != null) {
            this.value = value;
        }
    }

    public String getType() {
        return type;
    }

    public boolean setType(String type) {
        if (type == null || type.trim().equals("")) {
            return false;
        }
        this.type = type;
        return true;
    }

    public void setAnswers(TreeMap<String, String> answers) {
        this.answers = answers;
    }

    public void setAnswer(String key, String value) {
        answers.put(key, value);
    }

    public String getAnswer(String key) {
        return answers.get(key);
    }

    public TreeMap<String, String> getAnswers() {
        return answers;
    }

    public QuestionTreeNode copy() {
        QuestionTreeNode ret = new QuestionTreeNode();
        ret.type = this.type;
        ret.name = this.name;
        ret.value = this.value;
        for (String key : answers.keySet()) {
            ret.answers.put(key, answers.get(key));
        }
        for (String key : attributes.keySet()) {
            ret.attributes.put(key, attributes.get(key).copy());
        }
        for (int i = 0; i < this.getChildCount(); i++) {
            ret.add(((QuestionTreeNode) this.getChildAt(i)).copy());
        }
        return ret;
    }
}
