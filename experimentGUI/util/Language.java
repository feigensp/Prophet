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

/**
 * this class can load language specifications from xml files you can set the
 * language and get - per method your desired notes which correspond to a
 * keyword
 * 
 * @author Markus Köppen, Andreas Hasselberg
 * 
 */
public class Language {

	/**
	 * contains the keywords and the corresponding notes
	 */
	private static HashMap<String, HashMap<String, String>> keywords;
	/**
	 * contains the languages and theyr order
	 */
	private static ArrayList<String> languages;
	/**
	 * contains the current chose language
	 */
	private static int chosenLanguage;

	/**
	 * xml constants
	 */
	public static final String ELEMENT_LANGUAGES = "languages";
	public static final String ELEMENT_LANGUAGE = "language";
	public static final String ELEMENT_KEYWORD = "keyword";
	public static final String ATTRIBUTE_NAME = "name";
	public static final String ELEMENT_KEY_LAN = "languageInterpretation";
	public static final String ATTRIBUTE_LANGUAGE = "language";
	public static final String ATTRIBUTE_INTERPRETATION = "interpretation";

	/**
	 * initialize the data with the content of an xml file
	 * 
	 * @param path
	 *            path of the xml file
	 */
	public static void init(String path) {
		keywords = new HashMap<String, HashMap<String, String>>();
		languages = new ArrayList<String>();
		chosenLanguage = 0;

		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);
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
					HashMap<String, String> interprets = new HashMap<String, String>();
					NodeList interpretList = keyNode.getChildNodes();
					for (int k = 0; k < interpretList.getLength(); k++) {
						Node interpretNode = interpretList.item(k);
						String interpretString = interpretNode.getAttributes()
								.getNamedItem(ATTRIBUTE_INTERPRETATION).getNodeValue();
						String languageString = interpretNode.getAttributes()
								.getNamedItem(ATTRIBUTE_LANGUAGE).getNodeValue();
						interprets.put(languageString, interpretString);
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

	/**
	 * sets the language
	 * 
	 * @param language
	 *            name of the language
	 * @return true if applying was successful
	 */
	public static boolean setLanguage(String language) {
		int index = getLanIndex(languages, language);
		if (index != -1) {
			chosenLanguage = index;
			return true;
		} else {
			chosenLanguage = 0;
			return false;
		}
	}

	/**
	 * returns the String representation of the currently used language
	 * 
	 * @return current used language
	 */
	public static String getLanguage() {
		return languages.get(chosenLanguage);
	}

	/**
	 * returns all available languages
	 * 
	 * @return String-ArrayList with all languages
	 */
	public static ArrayList<String> getLanguages() {
		return languages;
	}

	/**
	 * returns the note to a specific keyword in the current language
	 * 
	 * @param key
	 *            keyword to the corresponding note
	 * @return note to the corresponding keyword (or null if not found)
	 */
	public static String getValue(String key) {
		HashMap<String, String> specifications = keywords.get(key);
		if (specifications != null) {
			return specifications.get(languages.get(chosenLanguage));
		} else {
			return null;
		}
	}

	/**
	 * returns the index of a String in an ArrayList
	 * 
	 * @param list
	 *            the list which contains Strings
	 * @param content
	 *            the String which index is unknown
	 * @return index of the String in the ArrayList
	 */
	private static int getLanIndex(ArrayList<String> list, String content) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(content)) {
				return i;
			}
		}
		return -1;
	}
}
