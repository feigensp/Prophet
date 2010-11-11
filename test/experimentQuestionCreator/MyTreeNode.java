/**
 * Repräsentiert einen Knoten eines Baumes, durch den auf den gesamten Baum zugegriffen werden kann
 * Der Inhalt eines Knoten repräsentiert dabei die Informationen eines QuestionElement
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package test.experimentQuestionCreator;

import java.util.Vector;


public class MyTreeNode {	
	
	//Attributliste
	private Vector<ElementAttribute> attributes;
	
	private int childCount;	//Anzahl der Kinder
	private Vector<MyTreeNode> children;	//Kinder
	private MyTreeNode parent;//Vater
	
	/**
	 * Konstruktor der ein Wurzelelement erschafft
	 */
	public MyTreeNode() {
		children = new Vector<MyTreeNode>();
		this.parent = null;	
		attributes = new Vector<ElementAttribute>();
	}
	
	/**
	 * Konstruktor für einen Unterknoten
	 * @param parent Vater des Knoten
	 * @param attributes Attributliste des Knoten
	 */
	public MyTreeNode(MyTreeNode parent, Vector<ElementAttribute> attributes) {
		children = new Vector<MyTreeNode>();
		this.parent = null;
		
		this.attributes = attributes;
		
	}
	
	/**
	 * Liefert den Vater des Knoten zurück
	 * @return Vaterknoten
	 */
	public MyTreeNode getParent() {
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
	public void addChild(MyTreeNode child) {
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
	public Vector<MyTreeNode> getChildren() {
		return children;
	}
	
	/**
	 * Gibt spezielles Kind zurück
	 * @param index Position an welcher das Kind im Vector steht
	 * @return TreeNode, welcher an der gewünschten Position im Vector ist
	 */
	public MyTreeNode getChild(int index) {
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
