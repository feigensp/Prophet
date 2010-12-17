package experimentGUI.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Language {
	
	private static HashMap<String, ArrayList<String>> keywords;
	private static ArrayList<String> languages;
	
	private static int chosenLanguage;

	public static final String XML_PATH = "language.xml";
	public static final String ELEMENT_LANGUAGES = "languages";
	public static final String ELEMENT_LANGUAGE = "language";
	public static final String ELEMENT_KEYWORD = "keyword";
	public static final String ATTRIBUTE_NAME = "name";
	public static final String ELEMENT_KEY_LAN = "languageInterpretation";
	public static final String ATTRIBUTE_LANGUAGE = "language";
	public static final String ATTRIBUTE_INTERPRETATION = "interpretation";
		
	public static void init() {	
		keywords = new HashMap<String, ArrayList<String>>();
		languages = new ArrayList<String>();
		chosenLanguage = 0;
		
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XML_PATH);
			Node xmlRoot = doc.getFirstChild();
			Node child = null;
			NodeList children = xmlRoot.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				child = children.item(i);
				// languages
				if (child.getNodeName().equals(ELEMENT_LANGUAGES)) {
					NodeList lanList = child.getChildNodes();
					for (int j = 0; j < lanList.getLength(); j++) {
						Node lan = lanList.item(j);
						languages.add(lan.getTextContent());
					}
				} else {
					// keywords
					Node keyNode = children.item(i);
					String key = keyNode.getAttributes().getNamedItem(ATTRIBUTE_NAME).getNodeValue();
					ArrayList<String> interprets = new ArrayList<String>();
					NodeList interpretList = keyNode.getChildNodes();
					for (int k = 0; k < interpretList.getLength(); k++) {
						Node interpretNode = interpretList.item(k);
						String lanInterpret = interpretNode.getAttributes().getNamedItem(ATTRIBUTE_LANGUAGE)
								.getNodeValue();
						String interpretString = interpretNode.getAttributes()
								.getNamedItem(ATTRIBUTE_INTERPRETATION).getNodeValue();
						int lanIndex = getLanIndex(languages, lanInterpret);
						interprets.add(lanIndex, interpretString);
					}
					keywords.put(key, interprets);
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void setLanguage(String language) {
		chosenLanguage = getLanIndex(languages, language);
	}
	
	public static String getValue(String key) {
		ArrayList<String> specifications = keywords.get(key);
		return specifications.get(chosenLanguage);
	}

	private static int getLanIndex(ArrayList<String> list, String content) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(content)) {
				return i;
			}
		}
		return -1;
	}
}
