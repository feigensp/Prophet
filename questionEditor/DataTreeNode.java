/**
 * This class represents a node with childs
 * the node has a identifier (name), and content.
 * Furthermore ist consist of a list of attributes
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package questionEditor;

import java.util.Vector;

public class DataTreeNode {

	// list of attributes
	private Vector<ElementAttribute> attributes;

	private String name; // string identifier
	private int childCount; // amount of childs
	private Vector<DataTreeNode> children; // childs
	private String content; // some possible content

	/**
	 * Constructor to create a new node
	 * 
	 * @param name
	 *            string identifier
	 */
	public DataTreeNode(String name) {
		this.name = name;
		this.children = new Vector<DataTreeNode>();
		this.attributes = new Vector<ElementAttribute>();
		this.content = "";
	}

	/**
	 * Returns the name of the node
	 * 
	 * @return name of the node
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of a node
	 * 
	 * @param name
	 *            the new name of the node
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the content of the node
	 * 
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * set the content of the Node
	 * 
	 * @param content
	 *            new content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * adds a new Child to this node
	 * 
	 * @param child
	 *            child which should be added
	 * @param index
	 *            position between all childs
	 */
	public void addChild(DataTreeNode child, int index) {
		children.add(index, child);
		childCount++;
	}

	/**
	 * returns the child at index
	 * 
	 * @param index
	 *            position of the child in the child-vector
	 * @return child node at the index
	 */
	public DataTreeNode getChild(int index) {
		return children.get(index);
	}

	/**
	 * returns a child by a string identifier
	 * 
	 * @param name
	 *            string identifier of the child
	 * @return child-node with the string as identifier or null if not found
	 */
	public DataTreeNode getChild(String name) {
		for (DataTreeNode child : children) {
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * returns a vector with all childs of this node
	 * 
	 * @return vector with all childs
	 */
	public Vector<DataTreeNode> getChildren() {
		return children;
	}

	/**
	 * removes a child from this node - identified bye name
	 * 
	 * @param name
	 *            name of the child which should be deleted
	 * @return true if found, false if not
	 */
	public boolean removeChild(String name) {
		int i = 0;
		for (DataTreeNode child : children) {
			if (child.getName().equals(name)) {
				children.removeElementAt(i);
				childCount--;
				return true;
			}
			i++;
		}
		return false;
	}

	/**
	 * returns the mount of childs from this node
	 * 
	 * @return amount of the childs
	 */
	public int getChildCount() {
		return childCount;
	}

	/**
	 * adds a new Attribute to the list of attributes from this node
	 * 
	 * @param attribute
	 *            new attribute
	 */
	public void addAttribute(ElementAttribute attribute) {
		this.attributes.add(attribute);
	}

	/**
	 * returns a special attribute - identified by name
	 * 
	 * @param name
	 *            name of the attribute
	 * @return attribute (or null if not found)
	 */
	public ElementAttribute getAttribute(String name) {
		for (ElementAttribute attribute : attributes) {
			if (attribute.getName().equals(name)) {
				return attribute;
			}
		}
		return null;
	}

	/**
	 * returns the whole list of attribute from this node
	 * 
	 * @return vector which consists all attributes
	 */
	public Vector<ElementAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * Looks if this node or a child (childs... child) node has the string
	 * identifier name
	 * 
	 * @param name
	 *            string identifier which should be looked at
	 * @return true if found, false if not
	 */
	public boolean nameExist(String name) {
		if (this.name.equals(name)) {
			return true;
		} else {
			for (DataTreeNode child : children) {
				if (child.nameExist(name)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * gives this node with his childs... child back as a String representation
	 */
	public String toString() {
		String ret = this.name;
		if (childCount > 0) {
			ret += " { ";
			for (DataTreeNode child : children) {
				ret += child.toString() + "|";
			}
			ret += " } ";
		}
		return ret;
	}
}
