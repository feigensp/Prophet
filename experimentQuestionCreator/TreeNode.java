/**
 * Repräsentiert einen Knoten eines Baumes, durch den auf den gesamten Baum zugegriffen werden kann
 * Der Inhalt eines Knoten repräsentiert dabei die Informationen eines QuestionElement
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package experimentQuestionCreator;

import java.awt.Dimension;
import java.util.Vector;


public class TreeNode {	
	
	//Attributliste
	private Vector<ElementAttribute> attributes;
	
	private int childCount;	//Anzahl der Kinder
	private Vector<TreeNode> children;	//Kinder
	private TreeNode parent;//Vater
	
	/**
	 * Konstruktor der ein Wurzelelement erschafft
	 */
	public TreeNode() {
		children = new Vector<TreeNode>();
		this.parent = null;	
		attributes = new Vector<ElementAttribute>();
	}
	
	/**
	 * Konstruktor für einen Unterknoten
	 * @param parent Vater des Knoten
	 * @param attributes Attributliste des Knoten
	 */
	public TreeNode(TreeNode parent, Vector<ElementAttribute> attributes) {
		children = new Vector<TreeNode>();
		this.parent = null;
		
		this.attributes = attributes;
		
	}
	
	/**
	 * Liefert den Vater des Knoten zurück
	 * @return Vaterknoten
	 */
	public TreeNode getParent() {
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
	public void addChild(TreeNode child) {
		children.add(child);
		childCount++;
	}
	
	public Vector<ElementAttribute> getAttributes() {
		return attributes;
	}
	
	/**
	 * Gibt einen Vector mit allen Kindern zurück
	 * @return Vector der alle TreeNode, welche kinder sind, enthält
	 */
	public Vector<TreeNode> getChildren() {
		return children;
	}
	
	/**
	 * Gibt spezielles Kind zurück
	 * @param index Position an welcher das Kind im Vector steht
	 * @return TreeNode, welcher an der gewünschten Position im Vector ist
	 */
	public TreeNode getChild(int index) {
		return children.get(index);
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
}
