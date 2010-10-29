/**
 * Repräsentiert einen Knoten eines Baumes, durch den auf den gesamten Baum zugegriffen werden kann
 * Der Inhalt eines Knoten repräsentiert dabei die Informationen eines QuestionElement
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package questionEditor;

import java.awt.Dimension;
import java.util.Vector;


public class DataTreeNode {	
	
	//Attributliste
	private Vector<ElementAttribute> attributes;
	
	private String name;	//Name des Knoten
	private int childCount;	//Anzahl der Kinder
	private Vector<DataTreeNode> children;	//Kinder
	private DataTreeNode parent;//Vater
	private String content;
	
	/**
	 * Konstruktor der ein Wurzelelement erschafft
	 * @param name Name des Knoten
	 */	
	public DataTreeNode(String name) {
		this.name = name;
		this.children = new Vector<DataTreeNode>();
		this.parent = null;	
		this.attributes = new Vector<ElementAttribute>();
		this.content = "";
	}
	
	/**
	 * Konstruktor für einen Unterknoten
	 * @param name Name des Knoten
	 * @param parent Vater des Knoten
	 * @param attributes Attributliste des Knoten
	 */
	public DataTreeNode(String name,  DataTreeNode parent, Vector<ElementAttribute> attributes) {
		this.name = name;
		this.children = new Vector<DataTreeNode>();
		this.parent = null;
		
		this.attributes = attributes;
		this.content = "";
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Returns the Name of the Node
	 * @return Name of the Node
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the Name of a Node
	 * @param name the new Name of the Node
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Liefert den Vater des Knoten zurück
	 * @return Vaterknoten
	 */
	public DataTreeNode getParent() {
		return parent;
	}
	
	/**
	 * Testet ob es die Wurzel des Baumes ist
	 * @return true wenn es eine Wurzel ist, sonst false
	 */
	public boolean isRoot() {
		return (parent == null);
	}
	
	/**
	 * Fügt dem Knoten ein Kind hinzu
	 * @param child Knoten der als Kind hinzugefügt wird 
	 */
	public void addChild(DataTreeNode child, int index) {
		children.add(index, child);
		childCount++;
	}
	
	public Vector<ElementAttribute> getAttributes() {
		return attributes;
	}
	
	public ElementAttribute getAttribute(String name) {
		for(ElementAttribute attribute : attributes) {
			if(attribute.getName().equals(name)) {
				return attribute;
			}
		}
		return null;
	}
	
	public boolean removeAttribute(String name) {
		for(ElementAttribute attribute : attributes) {
			if(attribute.getName().equals(name)) {
				attributes.remove(attribute);
				return true;
			}
		}
		return false;
		
	}
	
	public void addAttribute(ElementAttribute attribute) {
		this.attributes.add(attribute);
	}
	
	/**
	 * Gibt einen Vector mit allen Kindern zurück
	 * @return Vector der alle TreeNode, welche kinder sind, enthält
	 */
	public Vector<DataTreeNode> getChildren() {
		return children;
	}
	
	/**
	 * Gibt spezielles Kind zurück
	 * @param index Position an welcher das Kind im Vector steht
	 * @return TreeNode, welcher an der gewünschten Position im Vector ist
	 */
	public DataTreeNode getChild(int index) {
		return children.get(index);
	}
	
	public DataTreeNode getChild(String name) {
		for(DataTreeNode child : children) {
			if(child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}
	
	/**
	 * Gibt die Anzahl der kinder dieses Knotens zurück
	 * @return Anzahl der Kinder
	 */
	public int getChildCount() {
		return childCount;
	}
	
	/**
	 * Testet ob dieser Knoten ein Blatt ist
	 * @return true wenn ja, false sonst
	 */
	public boolean isLeaf() {
		return (childCount == 0);
	}
	
	public boolean removeChild(String name) {
		int i = 0;
		for(DataTreeNode child : children) {
			if(child.getName().equals(name)) {
				children.removeElementAt(i);
				childCount--;
				return true;
			}
			i++;
		}
		return false;
	}
	
	public String toString() {
		String ret = this.name;
		if(childCount > 0) {
			ret += " { ";
			for(DataTreeNode child : children) {
				ret += child.toString() + "|";
			}
			ret += " } ";
		}
		return ret;
	}
	
	public boolean nameExist(String name) {
		if(this.name.equals(name)) {
			return true;
		} else {
			for(DataTreeNode child : children) {
				if(child.nameExist(name)) {
					return true;
				}
			}
		}
		return false;
	}
}
