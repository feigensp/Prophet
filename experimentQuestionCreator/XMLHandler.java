/**
 * Klasse die sich mit dem ein-/auslesen sowie dem umwandeln von XML-Dateien beschäftigt
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package experimentQuestionCreator;

import java.awt.Dimension;
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
	 * Schreibt einen Fragebogen in Baumform in eine XML-Datei Im text-Attribut
	 * werden die Zeilenumbrüche in HTML-Form dargestellt
	 * 
	 * @param treeRoot
	 *            Wurzel des Baumes
	 * @param path
	 *            Wohin die XML-Datei geschrieben werden soll
	 */
	public static void writeXMLTree(TreeNode treeRoot, String path) {
		Document questionnaire = null;
		try {
			// Dokument erstellen
			questionnaire = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// Wurzelknoten erschaffen
			Element root = questionnaire.createElement("questionnaire");
			questionnaire.appendChild(root);
			// Alle Fragen hinzufügen
			Vector<TreeNode> questions = treeRoot.getChildren();
			for (TreeNode treeQuestion : questions) {
				Element question = questionnaire.createElement("question");
				root.appendChild(question);
				question.setAttribute("text", treeQuestion.getText());
				// Komponenten einer Frage hinzufügen
				Vector<TreeNode> components = treeQuestion.getChildren();
				for (TreeNode treeComponent : components) {
					Element component = questionnaire
							.createElement("component");
					question.appendChild(component);
					component.setAttribute("text", treeComponent.getText()
							.replace("\n", "<br>"));
					component.setAttribute("model",
							"" + treeComponent.getModel());
					component.setAttribute("x", ""
							+ treeComponent.getSize().getWidth());
					component.setAttribute("y", ""
							+ treeComponent.getSize().getHeight());
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
	 * Lädt eine XML-Datei und erstellt aus ihr die äquivalente Baumform für den
	 * Fragebogen Die HTML-Zeilenumbrüche werden wieder zurückgewandelt
	 * 
	 * @param path
	 *            Wo sich die XML-Datei befindet
	 * @return Wurzel der Baumstruktur
	 */
	public static TreeNode loadXMLTree(String path) {
		TreeNode treeRoot = new TreeNode();
		try {
			// Document lesen
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new File(path));
			// Erste Frage holen und diese dann nacheinander durchgehen
			Node question = doc.getFirstChild().getFirstChild();
			while (question != null) {
				String qText = question.getAttributes().getNamedItem("text")
						.getTextContent();
				TreeNode treeQuestion = new TreeNode(treeRoot, qText);
				treeRoot.addChild(treeQuestion);
				// Bei den Komponenten äquivalentes vorgehen wie bei den Fragen
				Node component = question.getFirstChild();
				while (component != null) {
					// Kinder einlesen
					NamedNodeMap attr = component.getAttributes();
					int cModel = Integer.parseInt(attr.getNamedItem("model")
							.getTextContent());
					String cText = attr.getNamedItem("text").getTextContent()
							.replaceAll("<br>", "\n\r");
					Dimension size = new Dimension();
					size.setSize(Double.parseDouble(attr.getNamedItem("x")
							.getTextContent()), Double.parseDouble(attr
							.getNamedItem("y").getTextContent()));
					treeQuestion.addChild(new TreeNode(treeQuestion, cText,
							cModel, size));
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
