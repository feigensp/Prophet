package questionViewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import questionEditor.ElementAttribute;

/**
 * This class contains the data loaded out of an xml file
 * the data represents questions in their categories
 * 
 * @author Markus Köppen, Andreas Hasselberg
 *
 */
public class QuestionData {
	
	public static final String HTML_FOOTER_FORWARD = "<br><br><input name =nextQuestion type='submit' value='Weiter'></body></html>";
	public static final String HTML_FOOTER_Backward_FORWARD = "<br><br><input name =lastQuestion type='submit' value='Zurück'><input name ='nextQuestion' type='submit' value='Weiter'></body></html>";
		
	/* consist the questions and the categories
	 * a categorie is represented by an AttributeArrayList in the ArrayList
	 */
	ArrayList<AttributeArrayList<QuestionInfos>> data;
	//the last question index (an the corresponding categorie Index) which were requestet
	int lastCategorieIndex, lastQuestionIndex;
	
	/**
	 * The Construktor which starts the loading of the data
	 * @param path the path to the xml file which consist the data
	 */
	public QuestionData(String path) {
		data = new ArrayList<AttributeArrayList<QuestionInfos>>();
		lastCategorieIndex = lastQuestionIndex = 0;
		loadData(path);
	}
	
	/**
	 * loads all the data from an xml file down to the third level
	 * @param path path of the xml file
	 */
	public void loadData(String path) {
		try {
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new File(path));
			Node xmlRoot = doc.getFirstChild();
			//categories
			NodeList xmlCategories = xmlRoot.getChildNodes();
			for(int i = 0; i<xmlCategories.getLength(); i++) {
				AttributeArrayList<QuestionInfos> categorie = new AttributeArrayList<QuestionInfos>();
				//add attributes
				NamedNodeMap xmlCategorieAttributes = xmlCategories.item(i).getAttributes();
				for(int j=0; j<xmlCategorieAttributes.getLength(); j++) {
					categorie.addAttribute(new StringTupel(xmlCategorieAttributes.item(j).getNodeName(), xmlCategorieAttributes.item(j).getNodeValue()));
				}
				//add questions
				NodeList xmlQuestions = xmlCategories.item(i).getChildNodes();
				for(int j=0; j<xmlQuestions.getLength(); j++) {
					String name = "";
					NamedNodeMap xmlQuestionAttributes = xmlQuestions.item(j).getAttributes();
					for(int k=0; k<xmlQuestionAttributes.getLength();k++) {
						if(xmlQuestionAttributes.item(k).getNodeName().equals("name")) {
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
	 * returns the amount of the level 2 nodes
	 * @return child count of the root
	 */
	public int getCategorieCount() {
		return data.size();
	}
	
	/**
	 * returns the amount of questions in a specific categorie
	 * @param categorieIndex index to specify the categorie
	 * @return childcount of the categorie
	 */
	public int getQuestionCount(int categorieIndex) {
		return data.get(categorieIndex).size();
	}
	
	/**
	 * return the settings (like path an if searchable is true) as a String
	 * @param categorieIndex the index of the categorie
	 * @return the String which represents the settings
	 */
	public ArrayList<StringTupel> getCategorieSettings(int categorieIndex) {
		return data.get(categorieIndex).getAttributes();
	}
	
	/**
	 * returns the HTML content of a question as String
	 * @param categorieIndex index of the categorie which consist the question
	 * @param questionIndex index of the question in the categorie
	 * @return String which consist the content of the HTML file
	 */
	public QuestionInfos getQuestion(int categorieIndex, int questionIndex) {
		lastCategorieIndex = categorieIndex;
		lastQuestionIndex = questionIndex;
		return data.get(categorieIndex).get(questionIndex);
	}
	
	/**
	 * return the categorie from the last requestet question
	 * @return index from the last categorie
	 */
	public int getLastCategorieIndex() {
		return lastCategorieIndex;
	}
	
	/**
	 * returns the index of the last requestet question
	 * @return index of the last question
	 */
	public int getLastQuestionIndex() {
		return lastQuestionIndex;
	}
	
	/**
	 * adds an answers to the last requestet question
	 * @param questionAnswers answers which would be saved
	 */
	public void addAnswers(String name, String answer) {
		data.get(lastCategorieIndex).get(lastQuestionIndex).addAnswer(name, answer);
	}
}
