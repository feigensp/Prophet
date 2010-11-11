/**
 * Klasse die sich mit dem ein-/auslesen sowie dem umwandeln von XML-Dateien beschäftigt
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package test.experimentQuestionCreator;

import java.io.File;
import java.io.IOException;
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
import org.xml.sax.SAXException;

public class XMLHandler {

	/**
	 * Schreibt einen Fragebogen/Antworten in Baumform in eine XML-Datei
	 * 
	 * @param treeRoot
	 *            Wurzel des Baumes
	 * @param path
	 *            Wohin die XML-Datei geschrieben werden soll
	 */
	public static void writeXMLTree(MyTreeNode treeRoot, String path) {
		Document questionnaire = null;
		try {
			// Dokument erstellen
			questionnaire = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// Wurzelknoten erschaffen
			Element root = questionnaire.createElement("questionnaire");
			questionnaire.appendChild(root);
			// Alle Fragen hinzufügen
			Vector<MyTreeNode> questions = treeRoot.getChildren();
			for (MyTreeNode treeQuestion : questions) {
				Element question = questionnaire.createElement("question");
				root.appendChild(question);
				//Attribute der Frage setzen
				Vector<ElementAttribute> questionAttributes = treeQuestion.getAttributes();
				for(ElementAttribute ea : questionAttributes) {
					question.setAttribute(ea.getName(), ea.getContent().toString());
				}
				
				// Komponenten einer Frage hinzufügen
				Vector<MyTreeNode> components = treeQuestion.getChildren();
				for (MyTreeNode treeComponent : components) {
					Element component = questionnaire
							.createElement("component");
					question.appendChild(component);
					//Attribute der Componente setzen
					Vector<ElementAttribute> componentAttributes = treeComponent.getAttributes();
					for(ElementAttribute ea : componentAttributes) {
						component.setAttribute(ea.getName(), ea.getContent().toString());
					}
				}
			}
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

		// Fragebogen in Datei speichern
		try {
			if (questionnaire != null) {
				TransformerFactory
						.newInstance()
						.newTransformer()
						.transform(new DOMSource(questionnaire),
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
	 * Lädt eine XML-Datei und erstellt aus ihr die äquivalente Baumform
	 * 
	 * @param path
	 *            Wo sich die XML-Datei befindet
	 * @return Wurzel der Baumstruktur
	 */
	public static MyTreeNode loadXMLTree(String path) {
		MyTreeNode treeRoot = new MyTreeNode();
		try {
			// Document lesen
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new File(path));
			// Erste Frage holen und diese dann nacheinander durchgehen
			Node question = doc.getFirstChild().getFirstChild();
			while (question != null) {
				NamedNodeMap questionAttributes = question.getAttributes();
				Node questionAttribute;
				Vector<ElementAttribute> questionTreeAttribute = new Vector<ElementAttribute>();
				for(int i = 0; i < questionAttributes.getLength(); i++) {
					questionAttribute = questionAttributes.item(i);
					questionTreeAttribute.add(new ElementAttribute(questionAttribute.getNodeName(), questionAttribute.getNodeValue()));
				}
				MyTreeNode treeQuestion = new MyTreeNode(treeRoot, questionTreeAttribute);
				treeRoot.addChild(treeQuestion);
				// Bei den Komponenten äquivalentes vorgehen wie bei den Fragen
				Node component = question.getFirstChild();
				while (component != null) {
					// Kinder einlesen
					NamedNodeMap componentAttributes = component.getAttributes();
					Node componentAttribute;
					Vector<ElementAttribute> componentTreeAttribute = new Vector<ElementAttribute>();
					for(int i = 0; i < componentAttributes.getLength(); i++) {
						componentAttribute = componentAttributes.item(i);
						componentTreeAttribute.add(new ElementAttribute(componentAttribute.getNodeName(), componentAttribute.getNodeValue()));
					}
					MyTreeNode treeComponent = new MyTreeNode(treeQuestion, componentTreeAttribute);	
					treeQuestion.addChild(treeComponent);
					component = component.getNextSibling();
				}
				// nächste Frage
				question = question.getNextSibling();
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return treeRoot;
	}
}
