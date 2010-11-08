package questionViewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

/**
 * This class contains the data loaded out of an xml file the data represents
 * questions in their categories
 * 
 * @author Markus Köppen, Andreas Hasselberg
 * 
 */
public class QuestionData {

	// consist the questions and the categories a categorie is represented by an
	// AttributeArrayList in the ArrayList
	private ArrayList<AttributeArrayList<QuestionInfos>> data;
	// the last question and categorie index
	private int lastCategoryIndex, lastQuestionIndex;

	/**
	 * The Constructor which starts the loading of the data
	 * 
	 * @param path
	 *            the path to the xml file which consist the data
	 */
	public QuestionData(String path) {
		data = new ArrayList<AttributeArrayList<QuestionInfos>>();
		lastCategoryIndex = lastQuestionIndex = 0;
		loadData(path);
	}

	/**
	 * loads all the data from an xml file down to the third level
	 * 
	 * @param path
	 *            path of the xml file
	 */
	public void loadData(String path) {
		try {
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new File(path));
			Node xmlRoot = doc.getFirstChild();
			// categories
			NodeList xmlCategories = xmlRoot.getChildNodes();
			for (int i = 0; i < xmlCategories.getLength(); i++) {
				AttributeArrayList<QuestionInfos> categorie = new AttributeArrayList<QuestionInfos>();
				// add attributes
				NamedNodeMap xmlCategorieAttributes = xmlCategories.item(i)
						.getAttributes();
				for (int j = 0; j < xmlCategorieAttributes.getLength(); j++) {
					categorie.addAttribute(new StringTupel(
							xmlCategorieAttributes.item(j).getNodeName(),
							xmlCategorieAttributes.item(j).getNodeValue()));
				}
				// add questions
				NodeList xmlQuestions = xmlCategories.item(i).getChildNodes();
				for (int j = 0; j < xmlQuestions.getLength(); j++) {
					String name = "";
					NamedNodeMap xmlQuestionAttributes = xmlQuestions.item(j)
							.getAttributes();
					for (int k = 0; k < xmlQuestionAttributes.getLength(); k++) {
						if (xmlQuestionAttributes.item(k).getNodeName()
								.equals("name")) {
							name = xmlQuestionAttributes.item(k).getNodeValue();
							break;
						}
					}
					String content = xmlQuestions.item(j).getTextContent();
					categorie.add(new QuestionInfos(name, content));
				}
				data.add(categorie);
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
	 * returns the amount of the level 2 nodes (categories)
	 * 
	 * @return child count of the root
	 */
	public int getCategorieCount() {
		return data.size();
	}

	/**
	 * returns the amount of questions in a specific category
	 * 
	 * @param categorieIndex
	 *            index to specify the category
	 * @return child count of the category
	 */
	public int getQuestionCount(int categorieIndex) {
		return data.get(categorieIndex).size();
	}

	/**
	 * return the settings (like path an if searchable is true) as a String
	 * 
	 * @param categorieIndex
	 *            the index of the category
	 * @return the String which represents the settings
	 */
	public ArrayList<StringTupel> getCategorieSettings(int categorieIndex) {
		return data.get(categorieIndex).getAttributes();
	}

	/**
	 * returns a specific setting of the last requested category by name
	 * 
	 * @param name
	 *            name of the setting
	 * @return string which represent value of the setting
	 */
	public String getCategorieSetting(String name) {
		return data.get(lastCategoryIndex).getAttribute(name);
	}

	/**
	 * returns the HTML content of a question as string sets the
	 * lastCategorieIndex and questionIndex to this index
	 * 
	 * @param categorieIndex
	 *            index of the category which consist the question
	 * @param questionIndex
	 *            index of the question in the category
	 * @return String which consist the content of the HTML file
	 */
	public QuestionInfos getQuestion(int categorieIndex, int questionIndex) {
		lastCategoryIndex = categorieIndex;
		lastQuestionIndex = questionIndex;
		return data.get(categorieIndex).get(questionIndex);
	}

	/**
	 * return the category from the last requested question
	 * 
	 * @return index from the last category
	 */
	public int getLastCategoryIndex() {
		return lastCategoryIndex;
	}

	/**
	 * returns the index of the last requested question
	 * 
	 * @return index of the last question
	 */
	public int getLastQuestionIndex() {
		return lastQuestionIndex;
	}

	/**
	 * decrement the lastQuestionIndex decrement if necessary the
	 * lastCategoryIndex also
	 * 
	 * @return true if it could be decremented, false if not
	 */
	public boolean decLastQuestionIndex() {
		if (lastQuestionIndex == 0) {
			while (data.get(--lastCategoryIndex).size() > 0) {
				if (lastCategoryIndex == 0)
					return false;
			}
			lastQuestionIndex = data.get(lastCategoryIndex).size() - 1;
		} else {
			lastQuestionIndex--;
		}
		return true;
	}

	/**
	 * increment the lastQuestionIndex increment if necessary the
	 * lastCategoryIndex also
	 * 
	 * @return true if it could be decremented, false if not
	 */
	public boolean incLastQuestionIndex() {
		if (lastQuestionIndex == data.get(lastCategoryIndex).size() - 1) {
			while (data.get(++lastCategoryIndex).size() > 0) {
				if (lastCategoryIndex == data.size() - 1) {
					return false;
				}
			}
			lastQuestionIndex = 0;
		} else {
			lastQuestionIndex++;
		}
		return true;
	}

	/**
	 * adds an answers to the last requestet question
	 * 
	 * @param questionAnswers
	 *            answers which would be saved
	 */
	public void addAnswers(String name, String answer) {
		data.get(lastCategoryIndex).get(lastQuestionIndex)
				.addAnswer(name, answer);
	}

	/**
	 * saves the yet collected answer data to an xml file
	 * 
	 * @param path
	 *            path (+name) of the xml file
	 */
	public void saveAnswersToXML(String path) {
		Document xmlDoc = null;
		try {
			xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();
			Element xmlRoot = xmlDoc.createElement("answers");
			xmlDoc.appendChild(xmlRoot);
			// categories
			for (AttributeArrayList<QuestionInfos> categorie : data) {
				String catName = categorie.getAttribute("name") == null ? "default"
						: categorie.getAttribute("name");
				Element xmlCategorie = xmlDoc.createElement("categorie");
				xmlCategorie.setAttribute("name", catName);
				xmlRoot.appendChild(xmlCategorie);
				// answers
				for (QuestionInfos question : categorie) {
					Element xmlQuestion = xmlDoc.createElement("question");
					for (StringTupel answer : question.getAnswers()) {
						xmlQuestion.setAttribute(answer.getKey(),
								answer.getValue());
					}
					xmlCategorie.appendChild(xmlQuestion);
				}
			}
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		// save in file
		try {
			if (xmlDoc != null) {
				TransformerFactory
						.newInstance()
						.newTransformer()
						.transform(new DOMSource(xmlDoc),
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
}
