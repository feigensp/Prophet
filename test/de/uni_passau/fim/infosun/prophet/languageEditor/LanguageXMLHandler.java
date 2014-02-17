package test.languageEditor;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LanguageXMLHandler {

	/**
	 * xml constants
	 */
	public static final String ELEMENT_LANGUAGE_SPECIFICATION = "LanguageSpecifications";
	public static final String ELEMENT_DEFAULT_LANGUAGE = "fallbackLanguage";
	public static final String ELEMENT_LANGUAGES = "languages";
	public static final String ELEMENT_LANGUAGE = "language";
	public static final String ELEMENT_KEYWORD = "keyword";
	public static final String ATTRIBUTE_KEY = "key";
	public static final String ELEMENT_LANGUAGE_INTERPRETATION = "languageInterpretation";
	public static final String ATTRIBUTE_LANGUAGE = "language";
	public static final String ATTRIBUTE_INTERPRETATION = "interpretation";
	
	/**
	 * save the data in a xml file
	 * 
	 * @param path
	 *            path and location of the xml file
	 */
	public static void save(String path, TreeMap<String, TreeMap<String, String>> givenKeywords, String fallbackLanguage) {
		TreeMap<String, TreeMap<String, String>> keywords = givenKeywords;
		Document xmlTree = null;
		try {
			xmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element xmlRoot = xmlTree.createElement(ELEMENT_LANGUAGE_SPECIFICATION);
			xmlTree.appendChild(xmlRoot);
			//default language
			if(fallbackLanguage != null) {
				Element fallbackLanguageElement = xmlTree.createElement(ELEMENT_DEFAULT_LANGUAGE);
				fallbackLanguageElement.setAttribute(ATTRIBUTE_LANGUAGE, fallbackLanguage);
				xmlRoot.appendChild(fallbackLanguageElement);
			}
			// keywords
			Iterator<String> keyIterator = keywords.keySet().iterator();
			Iterator<TreeMap<String, String>> valueIterator = keywords.values().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next();
				TreeMap<String, String> specifications = valueIterator.next();
				Element keyEle = xmlTree.createElement(ELEMENT_LANGUAGE);
				keyEle.setAttribute(ATTRIBUTE_LANGUAGE, key);

				Iterator<String> keywordIterator = specifications.keySet().iterator();
				Iterator<String> interpretationIterator = specifications.values().iterator();
				while (keywordIterator.hasNext()) {
					String keyword = keywordIterator.next();
					String interpretation = interpretationIterator.next();
					Element specEle = xmlTree.createElement(ELEMENT_KEYWORD);
					specEle.setAttribute(ATTRIBUTE_KEY, keyword);
					specEle.setAttribute(ATTRIBUTE_INTERPRETATION, interpretation);
					keyEle.appendChild(specEle);
				}
				xmlRoot.appendChild(keyEle);
			}
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		try {
			if (xmlTree != null) {
				TransformerFactory.newInstance().newTransformer()
						.transform(new DOMSource(xmlTree), new StreamResult(path));
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
	 * load the data in a xml file
	 * 
	 * @param path
	 *            location and name of the file
	 */
	public static Pair<TreeMap<String, TreeMap<String, String>>, String> load(String path) {
		TreeMap<String, TreeMap<String, String>> keywords = new TreeMap<String, TreeMap<String, String>>();
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);
			Node xmlRoot = doc.getFirstChild();
			NodeList children = xmlRoot.getChildNodes();
			keywords.clear();
			String fallbackLanguage = "";
			for (int i = 0; i < children.getLength(); i++) {
				// keywords
				Node lanNode = children.item(i);
				if(lanNode.getNodeName().equals(ELEMENT_DEFAULT_LANGUAGE)) {
					fallbackLanguage = lanNode.getAttributes().getNamedItem(ATTRIBUTE_LANGUAGE).getNodeValue();
				} else {
					String language = lanNode.getAttributes().getNamedItem(ATTRIBUTE_LANGUAGE).getNodeValue();
					TreeMap<String, String> xmlKeywords = new TreeMap<String, String>();
					NodeList keyList = lanNode.getChildNodes();
					for (int j = 0; j < keyList.getLength(); j++) {
						Node keyNode = keyList.item(j);
						String interpretString = keyNode.getAttributes().getNamedItem(ATTRIBUTE_INTERPRETATION)
								.getNodeValue();
						String keyString = keyNode.getAttributes().getNamedItem(ATTRIBUTE_KEY).getNodeValue();
						xmlKeywords.put(keyString, interpretString);
					}
					keywords.put(language, xmlKeywords);
				}
			}
			return new Pair<TreeMap<String, TreeMap<String, String>>, String>(keywords, fallbackLanguage);
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
