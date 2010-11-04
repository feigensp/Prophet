/**
 * this class consist methods which could write an DataTreeNode to an xml file 
 * or read an xml file into a DataTreeNode
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package questionEditor;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;
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

import QuestionTree.CategoryNode;
import QuestionTree.QuestionNode;
import QuestionTree.QuestionTreeNode;

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
	private static void addChildsToXML(Vector<QuestionTreeNode> treeChilds,
			Element xmlParent, Document xmlTree) {
		// Kinder hinzufügen
		for (QuestionTreeNode treeChild : treeChilds) {
			Element xmlChild = xmlTree.createElement(treeChild.getName());
			xmlParent.appendChild(xmlChild);
			// Attribute hinzufügen
			TreeMap<String,String> childAttributes = treeChild
					.getAttributes();
			if (treeChild.isQuestion()) {
				xmlChild.setTextContent(((QuestionNode)treeChild).getContent());
			}
			for (Entry<String,String> childAttribute : childAttributes.entrySet()) {
				xmlChild.setAttribute(childAttribute.getKey(), childAttribute
						.getValue());
			}
			// evtl. neue Kinder hinzufügen
			if (treeChild.getChildCount() > 0) {
				addChildsToXML(treeChild.getChildren(), xmlChild, xmlTree);
			}
		}
	}

	/**
	 * writes an DataTreeNode with his childs into an XML-File
	 * 
	 * @param treeRoot
	 *            DataTreeNode which should be added (with childs)
	 * @param path
	 *            path for the xml-file
	 */
	public static void writeXMLTree(QuestionTreeNode treeRoot, String path) {
		Document xmlTree = null;
		try {
			// Dokument erstellen
			xmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
			// Wurzelknoten erschaffen und Attribute hinzufügen
			Element xmlRoot = xmlTree.createElement(treeRoot.getName());
			xmlTree.appendChild(xmlRoot);
			TreeMap<String,String> rootAttributes = treeRoot.getAttributes();
			for (Entry<String,String> rootAttribute : rootAttributes.entrySet()) {
				xmlRoot.setAttribute(rootAttribute.getKey(), rootAttribute.getValue());
			}
			// Kinder hinzufügen
			if (treeRoot.getChildCount() > 0) {
				addChildsToXML(treeRoot.getChildren(), xmlRoot, xmlTree);
			}
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
	 * @param xmlCategories
	 *            the childs which should be added
	 * @param treeRoot
	 *            the DataTreeNode which should get the childs
	 */
	private static void addCategories(QuestionTreeNode treeRoot, NodeList xmlCategories) {
		// Kinder hinzufügen
		for (int i = 0; i < xmlCategories.getLength(); i++) {
			Node xmlCategory = xmlCategories.item(i);
			CategoryNode category = new CategoryNode(xmlCategory.getNodeName());
			// Attribute hinzufügen
			NamedNodeMap xmlCategoryAttributes = xmlCategory.getAttributes();
			TreeMap<String,String> attributes = category.getAttributes();
			for (int j = 0; j < xmlCategoryAttributes.getLength(); j++) {
				Node xmlCategoryAttribute = xmlCategoryAttributes.item(j);
				attributes.put(xmlCategoryAttribute.getNodeName(), xmlCategoryAttribute.getNodeValue());
			}
			// Kind hinzufügen
			treeRoot.insert(category, i);
			// evtl. weitere Kinder hinzufügen
			if (xmlCategory.getChildNodes().getLength() > 0) {
				addQuestions(category, xmlCategory.getChildNodes());
			}
		}
	}
	
	/**
	 * method which adds recursively the childs (to a DataTreeNode file)
	 * 
	 * @param xmlQuestions
	 *            the childs which should be added
	 * @param category
	 *            the DataTreeNode which should get the childs
	 */
	private static void addQuestions(CategoryNode category, NodeList xmlQuestions) {
		// Kinder hinzufügen
		for (int i = 0; i < xmlQuestions.getLength(); i++) {
			Node xmlQuestion = xmlQuestions.item(i);
			QuestionNode question = new QuestionNode(xmlQuestion.getNodeName());
			// Attribute hinzufügen
			question.setContent(xmlQuestion.getTextContent());
			NamedNodeMap xmlQuestionAttributes = xmlQuestion.getAttributes();
			TreeMap<String,String> attributes = question.getAttributes();
			for (int j = 0; j < xmlQuestionAttributes.getLength(); j++) {
				Node xmlQuestionAttribute = xmlQuestionAttributes.item(j);
				attributes.put(xmlQuestionAttribute.getNodeName(), xmlQuestionAttribute.getNodeValue());
			}
			// Kind hinzufügen
			category.insert(question, i);
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
			NamedNodeMap rootAttributes = xmlRoot.getAttributes();
			TreeMap<String,String> attributes = treeRoot.getAttributes();
			for (int i = 0; i < rootAttributes.getLength(); i++) {
				Node rootAttribute = rootAttributes.item(i);
				attributes.put(rootAttribute.getNodeName(), rootAttribute.getNodeValue());
			}
			// Kinder hinzufügen
			addCategories(treeRoot, xmlRoot.getChildNodes());

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
