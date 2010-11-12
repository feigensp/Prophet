package experimentViewer;

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

public class GenerateTestXML {
	
	public static void main(String[] args) {
		Document xmlTree = null;
		try {
			// Dokument erstellen
			xmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
			// Wurzelknoten erschaffen und Attribute hinzuf�gen
			Element xmlRoot = xmlTree.createElement("root");
			xmlTree.appendChild(xmlRoot);
			Element cat1 = xmlTree.createElement("categorie");
			Element cat2 = xmlTree.createElement("categorie");
			cat1.setAttribute("name", "cat1");
			cat1.setAttribute("path", "path1");
			cat2.setAttribute("name", "cat2");
			cat2.setAttribute("path", "path2");
			cat2.setAttribute("allowswitching", "true");
			xmlRoot.appendChild(cat1);
			xmlRoot.appendChild(cat2);
			Element child1 = xmlTree.createElement("q1");
			child1.setAttribute("name", "q1");
			String s = "frage 1 test<br><input name=\"vorname\" type=\"text\" size=\"30\">";
			child1.setTextContent(s);
			Element child2 = xmlTree.createElement("q2");
			child2.setAttribute("name", "q2");
			child2.setTextContent("frage 2 test");
			Element child21 = xmlTree.createElement("q21");
			child21.setAttribute("name", "q21");
			child21.setTextContent("frage 21 test<br>" + s);
			Element child22 = xmlTree.createElement("q22");
			child22.setAttribute("name", "q22");
			child22.setTextContent("frage 22 test");
			Element child23 = xmlTree.createElement("q23");
			child23.setAttribute("name", "q23");
			child23.setTextContent("frage 23 test");
			cat1.appendChild(child1);
			cat1.appendChild(child2);
			cat2.appendChild(child21);
			cat2.appendChild(child22);
			cat2.appendChild(child23);
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
								new StreamResult("myTest.xml"));
			}
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		} catch (TransformerException e1) {
			e1.printStackTrace();
		} catch (TransformerFactoryConfigurationError e1) {
			e1.printStackTrace();
		}
	}

}