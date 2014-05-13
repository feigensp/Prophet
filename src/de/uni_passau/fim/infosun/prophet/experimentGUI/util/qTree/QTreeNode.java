package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.util.*;

/**
 * A tree where nodes contain data (name and type of the node, HTML content) to be displayed by the
 * <code>ExperimentViewer</code> or <code>ExperimentEditor</code> and <code>Attribute</code>s.
 */
public class QTreeNode implements Cloneable {

    /**
     * Possible types of <code>QTreeNode</code>s.
     */
    public static enum Type {
        EXPERIMENT, CATEGORY, QUESTION
    }

    private Type type;
    private String name;
    private String html;
    private Map<String, Attribute> attributes;

    private QTreeNode parent;
    private List<QTreeNode> children;

    private Map<String, String> answers;
    private long answerTime;

    /**
     * Constructs a new <code>QTreeNode</code> with the given parameters. The <code>parent</code> may be
     * <code>null</code> if this is the root node of the tree.
     *
     * @param parent the parent of this node
     * @param type the <code>Type</code> of the node
     * @param name the name of the node
     *
     * @throws NullPointerException if <code>type</code> or <code>name</code> is <code>null</code>
     */
    public QTreeNode(QTreeNode parent, Type type, String name) {
        Objects.requireNonNull(type, "type must not be null!");
        Objects.requireNonNull(name, "name must not be null!");

        this.type = type;
        this.name = name;
        this.html = "";
        this.attributes = new HashMap<>();
        this.parent = parent;
        this.children = new ArrayList<>();
        this.answers = new HashMap<>();
    }

    /**
     * Gets the answer for the given <code>key</code> or <code>null</code> if no mapping for the key exists.
     *
     * @param key the key for the answer
     * @return the answer or <code>null</code>
     */
    public String getAnswer(String key) {
        return answers.get(key);
    }

    /**
     * Sets a mapping from the given <code>key</code> to the given <code>answer</code>.
     * Neither <code>key</code> nor <code>answer</code> may be <code>null</code>.
     *
     * @param key the key for the answer
     * @param answer the answer
     */
    public void setAnswer(String key, String answer) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(answer, "answer must not be null");

        answers.put(key, answer);
    }

    /**
     * Gets the answer time for this node.
     *
     * @return the answer time
     */
    public long getAnswerTime() {
        return answerTime;
    }

    /**
     * Sets the answer time for this node.
     *
     * @param answerTime the new answer time
     */
    public void setAnswerTime(long answerTime) {
        this.answerTime = answerTime;
    }

    /**
     * Adds an <code>Attribute</code> to this <code>QTreeNode</code>. If an <code>Attribute</code> with the key of
     * the given <code>Attribute</code> has previously been added it will be overwritten.
     *
     * @param attribute the <code>Attribute</code> to be added
     */
    public void addAttribute(Attribute attribute) {
        Objects.requireNonNull(attribute, "attribute must not be null!");

        attributes.put(attribute.getKey(), attribute);
    }

    /**
     * Gets the <code>Attribute</code> with the given key. If it does not exist it will be created with the empty
     * <code>String</code> as its content.
     *
     * @param key the key of the <code>Attribute</code>
     * @return the <code>Attribute</code>
     */
    public Attribute getAttribute(String key) {
        Objects.requireNonNull(key, "key must not be null!");

        Attribute attribute = attributes.get(key);

        if (attribute == null) {
            attribute = new Attribute(key);
            addAttribute(attribute);
        }

        return attribute;
    }

    /**
     * Gets the <code>Attribute</code>s of this <code>QTreeNode</code>.
     *
     * @return the <code>Attribute</code>s
     */
    public Collection<Attribute> getAttributes() {
        return attributes.values();
    }

    /**
     * Tests whether an <code>Attribute</code> with the given key exists for this <code>QTreeNode</code>.
     *
     * @param key the key of the <code>Attribute</code>
     * @return true iff an <code>Attribute</code> with the given key exists
     */
    public boolean containsAttribute(String key) {
        Objects.requireNonNull(key, "key must not be null!");

        return attributes.containsKey(key);
    }

    /**
     * Sets the parent of the node to the new value.
     *
     * @param parent the new parent of the node
     */
    public void setParent(QTreeNode parent) {
        this.parent = parent;
    }

    /**
     * Sets the name of the node to the new value.
     *
     * @param name the new name of the node
     * @throws NullPointerException if <code>name</code> is <code>null</code>
     */
    public void setName(String name) {
        Objects.requireNonNull(name, "name must not be null!");

        this.name = name;
    }

    /**
     * Sets the HTML content of the node to the new value.
     *
     * @param html the new HTML content of the node
     * @throws NullPointerException if <code>html</code> is <code>null</code>
     */
    public void setHtml(String html) {
        Objects.requireNonNull(html, "html must not be null!");

        this.html = html;
    }

    /**
     * Removes the given child from this node.
     *
     * @param child the child to be removed
     * @throws NullPointerException if <code>child</code> is <code>null</code>
     */
    public void removeChild(QTreeNode child) {
        Objects.requireNonNull(child, "child must not be null!");

        children.remove(child);
    }

    /**
     * Adds a child to this node.
     *
     * @param child the child to be added
     * @throws NullPointerException if <code>child</code> if <code>null</code>
     */
    public void addChild(QTreeNode child) {
        Objects.requireNonNull(child, "child must not be null!");

        children.add(child);
    }

    /**
     * Adds a child to this node at the given index.
     *
     * @param child the child to be added
     * @param index the desired index
     * @throws NullPointerException if <code>child</code> is <code>null</code>
     */
    public void addChild(QTreeNode child, int index) {
        Objects.requireNonNull(child, "child must not be null!");

        children.add(index, child);
    }

    /**
     * Returns the tree under this node in preorder.
     *
     * @return the subtree in preorder
     */
    public List<QTreeNode> preOrder() {
        List<QTreeNode> preOrderNodes = new LinkedList<>();

        preOrderNodes.add(this);
        children.parallelStream().map(QTreeNode::preOrder).sequential().forEach(preOrderNodes::addAll);

        return preOrderNodes;
    }

    /**
     * Gets the <code>Type</code> of the node.
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the name of the node.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the HTML content stored in the node.
     *
     * @return the HTML content
     */
    public String getHtml() {
        return html;
    }

    /**
     * Gets the children of the node.
     *
     * @return the children
     */
    public List<QTreeNode> getChildren() {
        return children;
    }

    /**
     * Gets the parent of this <code>QTreeNode</code> or <code>null</code> if this is the root node.
     *
     * @return the parent
     */
    public  QTreeNode getParent() {
        return parent;
    }

    /**
     * Gets the child at the given index.
     *
     * @param index the index
     * @return the <code>QTreeNode</code> or <code>null</code>
     *
     * @throws IndexOutOfBoundsException if <code>index</code> is out of range (index < 0 || index >= getChildCount())
     */
    public QTreeNode getChild(int index) {
        return children.get(index);
    }

    /**
     * Gets the index of the given node or -1 if the node is not a child of this node.
     *
     * @param node the node whose index is to be returned
     * @return the index of the node
     */
    public int getIndexOfChild(QTreeNode node) {
        return children.indexOf(node);
    }

    /**
     * Gets the number of children this node has.
     *
     * @return the number of children
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Returns whether this node is a leaf (it has no children).
     *
     * @return true iff the node is a leaf
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        QTreeNode clone = (QTreeNode) super.clone();
        Map<String, Attribute> cAttributes = new HashMap<>();
        List<QTreeNode> cChildren = new ArrayList<>();

        clone.parent = null;

        // clone the attributes
        for (Map.Entry<String, Attribute> entry : attributes.entrySet()) {
            cAttributes.put(entry.getKey(), (Attribute) entry.getValue().clone());
        }
        clone.attributes = cAttributes;

        // clone the children
        for (QTreeNode child : children) {
            QTreeNode cChild = (QTreeNode) child.clone();
            cChild.parent = clone;
            cChildren.add(cChild);
        }
        clone.children = cChildren;

        return clone;
    }

    /**
     * This method will be called (by the JVM) after this object was deserialised. It is implemented because
     * the XML representation for this object uses implicit collections/maps whose fields will be <code>null</code>
     * if they were empty at the time of serialisation. These fields will be initialised with empty collections.
     *
     * @return always returns <code>this</code>
     */
    private Object readResolve() {

        if (attributes == null) {
            attributes = new HashMap<>();
        }

        if (children == null) {
            children = new ArrayList<>();
        }

        if (answers == null) {
            answers = new HashMap<>();
        }

        return this;
    }
}
