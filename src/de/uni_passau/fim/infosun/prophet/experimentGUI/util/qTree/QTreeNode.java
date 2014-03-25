package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    }

    /**
     * Adds an <code>Attribute</code> to this <code>QTreeNode</code>. If an <code>Attribute</code> with the key of
     * the given <code>Attribute</code> has previously been added it will be overwritten.
     *
     * @param attribute the <code>Attribute</code> to be added
     */
    public void addAttribute(Attribute attribute) {
        attributes.put(attribute.getKey(), attribute);
    }

    /**
     * Gets the <code>Attribute</code> with the given key.
     *
     * @param key the key of the <code>Attribute</code>
     * @return the <code>Attribute</code>
     */
    public Attribute getAttribute(String key) {
        return attributes.get(key);
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

        return this;
    }
}
