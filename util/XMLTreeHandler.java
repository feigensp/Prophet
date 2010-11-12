/**
 * this class consist methods which could write an DataTreeNode to an xml file 
 * or read an xml file into a DataTreeNode
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package util;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




public class XMLTreeHandler {

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
	private static void addChildToXML(Document xmlTree, Element xmlNode, QuestionTreeNode treeNode) {
		//Name hinzufügen
		xmlNode.setAttribute("name", treeNode.getName());
		// Attribute hinzufügen
		for (Entry<String,String> nodeAttribute : treeNode.attributes()) {
			xmlNode.setAttribute(nodeAttribute.getKey(), nodeAttribute
					.getValue());
		}
		//Content hinzufügen
		if (treeNode.hasContent()) {
			xmlNode.setTextContent(treeNode.getContent());
		}
		// evtl. neue Kinder hinzufügen
		for (int i=0; i<treeNode.getChildCount(); i++) {
			QuestionTreeNode treeChild = (QuestionTreeNode)treeNode.getChildAt(i);
			Element xmlChild = xmlTree.createElement(treeChild.getType());
			xmlNode.appendChild(xmlChild);
			addChildToXML(xmlTree, xmlChild, treeChild);
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
	public static void writeXMLTree(QuestionTreeNode treeRoot, String path) {
		Document xmlTree = null;
		try {
			// Dokument erstellen
			xmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
			// Wurzelknoten erschaffen
			Element xmlRoot = xmlTree.createElement(treeRoot.getType());
			xmlTree.appendChild(xmlRoot);
			addChildToXML(xmlTree, xmlRoot, treeRoot);
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
								new StreamResult(path + ".xml"));
			}
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		} catch (TransformerException e1) {
			e1.printStackTrace();
		} catch (TransformerFactoryConfigurationError e1) {
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
	private static void addTreeChildren(QuestionTreeNode treeParent, NodeList xmlChildren) {
		// Kinder hinzufügen
		for (int i = 0; i < xmlChildren.getLength(); i++) {
			QuestionTreeNode treeChild = new QuestionTreeNode();
			Node xmlChild = xmlChildren.item(i);
			String type = xmlChild.getNodeName();			
			if (type.equals("#text")) {
				treeParent.setContent(xmlChild.getTextContent());
				continue;
			} else {
				treeChild.setType(type);
			}
			// Attribute hinzufügen
			if (xmlChild.hasAttributes()) {
				NamedNodeMap xmlChildAttributes = xmlChild.getAttributes();
				for (int j = 0; j < xmlChildAttributes.getLength(); j++) {
					Node xmlChildAttribute = xmlChildAttributes.item(j);
					if (xmlChildAttribute.getNodeName().equals("name")) {
						treeChild.setName(xmlChildAttribute.getNodeValue());
					} else {
						treeChild.setAttribute(xmlChildAttribute.getNodeName(), xmlChildAttribute.getNodeValue());
					}
				}
			}
			// Kind hinzufügen
			treeParent.insert(treeChild, treeParent.getChildCount());
			// evtl. weitere Kinder hinzufügen
			if (xmlChild.getChildNodes().getLength() > 0) {
				addTreeChildren(treeChild, xmlChild.getChildNodes());
			}
		}
	}

	/**
	 * loads an xml file an creates an corresponding DataTreeNode
	 * 
	 * @param path
	 *            path of the file
	 * @return root of the new tree-structure
	 */
	public static QuestionTreeNode loadXMLTree(String path) {
		try {
			// Document lesen
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new File(path));
			// Wurzel holen
			Node xmlRoot = doc.getFirstChild();
			QuestionTreeNode treeRoot = new QuestionTreeNode(xmlRoot.getNodeName());
			// Wurzelattribute
			NamedNodeMap xmlRootAttributes = xmlRoot.getAttributes();
			for (int i = 0; i < xmlRootAttributes.getLength(); i++) {
				Node xmlRootAttribute = xmlRootAttributes.item(i);
				if (xmlRootAttribute.getNodeName().equals("name")) {
					treeRoot.setName(xmlRootAttribute.getNodeValue());
				} else {
					treeRoot.setAttribute(xmlRootAttribute.getNodeName(), xmlRootAttribute.getNodeValue());
				}
			}
			// Kinder hinzufügen
			addTreeChildren(treeRoot, xmlRoot.getChildNodes());
			return treeRoot;
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
