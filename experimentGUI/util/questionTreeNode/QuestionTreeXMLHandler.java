/**
 * this class consist methods which could write an DataTreeNode to an xml file 
 * or read an xml file into a DataTreeNode
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package experimentGUI.util.questionTreeNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class QuestionTreeXMLHandler {
	private final static String ATTRIBUTE_NAME = "name";
	private final static String ATTRIBUTE_VALUE = "value";
	private final static String ATTRIBUTE_TIME = "time";
	private final static String TYPE_ATTRIBUTES = "attributes";
	private final static String TYPE_ANSWERS = "answers";
	private final static String TYPE_ANSWER = "answer";
	private final static String TYPE_CHILDREN = "children";

	/**
	 * method which adds recursively the childs (to an xml file)
	 * 
	 * @param treeChilds
	 *            childs which should bye added
	 * @param xmlParent
	 *            the parent who should get the childs
	 * @param xmlTree
	 *            the xml-document
	 */
	private static void saveXMLNode(Document xmlTree, Element xmlNode, QuestionTreeNode treeNode) {
		//Name und Value hinzufügen
		xmlNode.setAttribute(ATTRIBUTE_NAME, treeNode.getName());
		xmlNode.setAttribute(ATTRIBUTE_VALUE, treeNode.getValue());
		// evtl. Attribute hinzufügen
		if (treeNode.getAttributes().size()>0) {
			Element xmlAttributesNode = xmlTree.createElement(TYPE_ATTRIBUTES);
			xmlNode.appendChild(xmlAttributesNode);
			for (Entry<String, QuestionTreeNode> attributeNode : treeNode.getAttributes().entrySet()) {
				QuestionTreeNode treeChild = attributeNode.getValue();
				Element xmlChild = xmlTree.createElement(treeChild.getType());
				xmlAttributesNode.appendChild(xmlChild);
				saveXMLNode(xmlTree, xmlChild, treeChild);
			}
		}
		// evtl. Kinder hinzufügen
		if (treeNode.getChildCount()>0) {
			Element xmlChildrenNode = xmlTree.createElement(TYPE_CHILDREN);
			xmlNode.appendChild(xmlChildrenNode);
			for (int i=0; i<treeNode.getChildCount(); i++) {
				QuestionTreeNode treeChild = (QuestionTreeNode)treeNode.getChildAt(i);
				Element xmlChild = xmlTree.createElement(treeChild.getType());
				xmlChildrenNode.appendChild(xmlChild);
				saveXMLNode(xmlTree, xmlChild, treeChild);
			}
		}
	}

	/**
	 * writes an DataTreeNode with his children into an XML-File
	 * 
	 * @param treeRoot
	 *            DataTreeNode which should be added (with children)
	 * @param path
	 *            path for the xml-file
	 */
	public static void saveXMLTree(QuestionTreeNode treeRoot, String path) {
		Document xmlTree = null;
		try {
			// Dokument erstellen
			xmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
			// Wurzelknoten erschaffen
			Element xmlRoot = xmlTree.createElement(treeRoot.getType());
			xmlTree.appendChild(xmlRoot);
			saveXMLNode(xmlTree, xmlRoot, treeRoot);
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		// Fragebogen in Datei speichern
		try {
			if (xmlTree != null) {
				TransformerFactory
						.newInstance()
						.newTransformer()
						.transform(new DOMSource(xmlTree),
								new StreamResult(path));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch (Error e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * method which adds recursively the childs (to an xml file)
	 * 
	 * @param treeChilds
	 *            childs which should bye added
	 * @param xmlParent
	 *            the parent who should get the childs
	 * @param xmlTree
	 *            the xml-document
	 */
	private static void saveXMLAnswerNode(Document xmlTree, Element xmlNode, QuestionTreeNode treeNode) {
		//Name hinzufügen
		xmlNode.setAttribute(ATTRIBUTE_NAME, treeNode.getName());
		xmlNode.setAttribute(ATTRIBUTE_TIME, ""+treeNode.getAnswerTime());
		// evtl. Antworten hinzufügen
		if (treeNode.getAnswers().size()>0) {
			Element xmlAnswersNode = xmlTree.createElement(TYPE_ANSWERS);
			xmlNode.appendChild(xmlAnswersNode);
			for (Entry<String, String> answerEntry : treeNode.getAnswers().entrySet()) {
				Element xmlChild = xmlTree.createElement(TYPE_ANSWER);
				xmlChild.setAttribute(ATTRIBUTE_NAME, answerEntry.getKey());
				xmlChild.setAttribute(ATTRIBUTE_VALUE, answerEntry.getValue());
				xmlAnswersNode.appendChild(xmlChild);
			}
		}
		// evtl. Kinder hinzufügen
		if (treeNode.getChildCount()>0) {
			Element xmlChildrenNode = xmlTree.createElement(TYPE_CHILDREN);
			xmlNode.appendChild(xmlChildrenNode);
			for (int i=0; i<treeNode.getChildCount(); i++) {
				QuestionTreeNode treeChild = (QuestionTreeNode)treeNode.getChildAt(i);
				Element xmlChild = xmlTree.createElement(treeChild.getType());
				xmlChildrenNode.appendChild(xmlChild);
				saveXMLAnswerNode(xmlTree, xmlChild, treeChild);
			}
		}
	}

	/**
	 * writes an DataTreeNode with his children into an XML-File
	 * 
	 * @param treeRoot
	 *            DataTreeNode which should be added (with children)
	 * @param path
	 *            path for the xml-file
	 */
	public static void saveXMLAnswerTree(QuestionTreeNode treeRoot, String path) {
		Document xmlTree = null;
		try {
			// Dokument erstellen
			xmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
			// Wurzelknoten erschaffen
			Element xmlRoot = xmlTree.createElement(treeRoot.getType());
			xmlTree.appendChild(xmlRoot);
			saveXMLAnswerNode(xmlTree, xmlRoot, treeRoot);
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		// Fragebogen in Datei speichern
		try {
			if (xmlTree != null) {
				TransformerFactory
						.newInstance()
						.newTransformer()
						.transform(new DOMSource(xmlTree),
								new StreamResult(path));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch (Error e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * method which adds recursively the childs (to a DataTreeNode file)
	 * 
	 * @param xmlChildren
	 *            the childs which should be added
	 * @param treeParent
	 *            the DataTreeNode which should get the childs
	 */
	private static QuestionTreeNode loadXMLNode(Node xmlNode) {
		QuestionTreeNode result = new QuestionTreeNode(xmlNode.getNodeName());
		//Name und Value setzen
		Node xmlNameNode = xmlNode.getAttributes().getNamedItem(ATTRIBUTE_NAME);
		if (xmlNameNode !=null) {
			result.setName(xmlNameNode.getNodeValue());
		}
		Node xmlValueNode = xmlNode.getAttributes().getNamedItem(ATTRIBUTE_VALUE);
		if (xmlValueNode!=null) {
			result.setValue(xmlValueNode.getNodeValue());
		}
		//Attribute und Kinder hinzufügen
		NodeList xmlMetaNodes = xmlNode.getChildNodes();
		for (int i = 0; i < xmlMetaNodes.getLength(); i++) {
			Node xmlMetaNode = xmlMetaNodes.item(i);
			if (xmlMetaNode.getNodeName().equals(TYPE_ATTRIBUTES) && xmlMetaNode.hasChildNodes()) {
				NodeList xmlAttributesList = xmlMetaNode.getChildNodes();
				for (int j=0; j<xmlAttributesList.getLength();j++) {
					Node xmlAttributeNode = xmlAttributesList.item(j);
					QuestionTreeNode attributeNode = loadXMLNode(xmlAttributeNode);
					result.setAttribute(attributeNode.getName(), attributeNode);					
				}
			} else if (xmlMetaNode.getNodeName().equals(TYPE_CHILDREN) && xmlMetaNode.hasChildNodes()) {
				NodeList xmlChildrenList = xmlMetaNode.getChildNodes();
				for (int j=0; j<xmlChildrenList.getLength();j++) {
					Node xmlChildNode = xmlChildrenList.item(j);
					result.add(loadXMLNode(xmlChildNode));					
				}
			}
		}
		return result;
	}

	/**
	 * loads an xml file an creates an corresponding DataTreeNode
	 * 
	 * @param path
	 *            path of the file
	 * @return root of the new tree-structure
	 */
	public static QuestionTreeNode loadXMLTree(String path) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		try {			
			// Document lesen
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(file);
			// Wurzel holen
			Node xmlRoot = doc.getFirstChild();
			return loadXMLNode(xmlRoot);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
}
