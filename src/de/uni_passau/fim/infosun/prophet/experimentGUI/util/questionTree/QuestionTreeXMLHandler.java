/**
 * this class consist methods which could write an DataTreeNode to an xml file
 * or read an xml file into a DataTreeNode
 *
 * @author Markus K�ppen, Andreas Hasselberg
 */

package de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import au.com.bytecode.opencsv.CSVWriter;
import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.Pair;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import org.cdmckay.coffeedom.Document;
import org.cdmckay.coffeedom.Element;
import org.cdmckay.coffeedom.input.SAXBuilder;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class QuestionTreeXMLHandler {

    public final static String ATTRIBUTE_NAME = "name";
    public final static String ATTRIBUTE_VALUE = "value";
    public final static String ATTRIBUTE_TIME = "time";
    public final static String TYPE_ATTRIBUTES = "attributes";
    public final static String TYPE_ANSWERS = "answers";
    public final static String TYPE_ANSWER = "answer";
    public final static String TYPE_CHILDREN = "children";
    public final static String TYPE_CATEGORY = "category";
    public final static String TYPE_QUESTION = "question";
    private final static String HEADER_SEPARATOR = ":";
    private final static String HEADER_TIME = "time";

    /**
     * Recursively searches the given <code>directory</code> for files named <code>fileName</code> and
     * collects them in a list. If the given <code>File</code> is not a directory an empty list will be returned.
     *
     * @param directory
     *         the directory to be searched
     *
     * @return the list of files
     */
    public static List<File> getFilesByName(File directory, String fileName) {
        Objects.requireNonNull(directory, "directory must not be null!");
        Objects.requireNonNull(fileName, "fileName must not be null!");

        List<File> xmlFiles = new LinkedList<>();

        if (!directory.isDirectory()) {
            return xmlFiles;
        }

        for (File file : directory.listFiles()) {

            if (file.isDirectory()) {
                xmlFiles.addAll(getFilesByName(file, fileName));
            } else {
                if (file.getName().equals(fileName)) {
                    xmlFiles.add(file);
                }
            }
        }

        return xmlFiles;
    }

    /**
     * Parses the given <code>File</code> objects as XML files and collects the resulting <code>Document</code>
     * objects.
     *
     * @param files
     *         the files to be parsed
     *
     * @return the list of <code>Document</code>s
     */
    public static List<Document> getDocuments(List<File> files) {
        Objects.requireNonNull(files, "files must not be null!");

        List<Document> documents = new LinkedList<>();
        Document document;
        SAXBuilder builder = new SAXBuilder();

        for (File file : files) {

            try {
                document = builder.build(file);
                documents.add(document);
            } catch (IOException e) {
                System.err.println("Could not parse a file to XML: " + file);
            }
        }

        return documents;
    }

    /**
     * Saves the given XML answer files as one CSV file <code>csvFile</code>. If <code>csvFile</code> already exists
     * it will be overwritten.
     *
     * @param root
     *         the root node of the current <code>QuestionTree</code>
     * @param xmlAnswerFiles
     *         the answer XML files to be saved
     * @param csvFile
     *         the file in which the CSV data is to be saved
     */
    public static void saveAsCSV(QuestionTreeNode root, List<Document> xmlAnswerFiles, File csvFile,
            String experimentCode) {

        Objects.requireNonNull(root, "root must not be null");
        Objects.requireNonNull(xmlAnswerFiles, "xmlAnswerFiles must not be null!");
        Objects.requireNonNull(csvFile, "csvFile must not be null!");

        if (xmlAnswerFiles.isEmpty()) {
            return;
        }

        try {
            if (csvFile.exists()) {
                if (!csvFile.delete()) {
                    System.err.println("Could not overwrite " + csvFile);
                    return;
                }
            }

            if (!csvFile.createNewFile()) {
                throw new IOException();
            }
        } catch (IOException e) {
            System.err.println("Could not create " + csvFile);
            return;
        }

        CSVWriter csvWriter;
        try {
            csvWriter = new CSVWriter(new FileWriter(csvFile), ';', '"');
        } catch (IOException e) {
            System.err.println("Could not create a Writer for " + csvFile);
            return;
        }

        List<String> header = QuestionTreeHTMLHandler.headerFromQuestionTree(root);

        csvWriter.writeNext(header.toArray(new String[header.size()]));

        for (Document doc : xmlAnswerFiles) {
            csvWriter.writeNext(inputDocument(header, doc, experimentCode));
        }

        try {
            csvWriter.close();
        } catch (IOException e) {
            System.err.println("Could not close the Writer");
        }
    }

    public static String[] inputDocument(List<String> columns, Document document, String expCode) {
        Map<String, String> entries = new LinkedHashMap<>();

        for (String cName : columns) {
            entries.put(cName, "");
        }

        entries.put(QuestionTreeHTMLHandler.HEAER_EXPCODE, expCode);

        Element root = document.getRootElement();
        String key;
        List<Element> categories;
        List<Element> questions;
        List<Element> answers;
        Element collectionNode;

        // the experiment time
        key = root.getAttributeValue(ATTRIBUTE_NAME) + HEADER_SEPARATOR + HEADER_TIME;
        entries.put(key, root.getAttributeValue(ATTRIBUTE_TIME));

        // the subjectcode
        if ((collectionNode = root.getChild(TYPE_ANSWERS)) != null) {
            Element subjCode = collectionNode.getChild(TYPE_ANSWER);
            entries.put(QuestionTreeHTMLHandler.HEADER_PROBCODE, subjCode.getAttributeValue(ATTRIBUTE_VALUE));
        }

        if ((collectionNode = root.getChild(TYPE_CHILDREN)) != null) {

            categories = collectionNode.getChildren(TYPE_CATEGORY);
            for (Element category : categories) {

                key = category.getAttributeValue(ATTRIBUTE_NAME) + HEADER_SEPARATOR + HEADER_TIME;
                entries.put(key, category.getAttributeValue(ATTRIBUTE_TIME));

                if ((collectionNode = category.getChild(TYPE_ANSWERS)) != null) {

                    // all answers in the category
                    answers = collectionNode.getChildren(TYPE_ANSWER);
                    for (Element answer : answers) {
                        entries.put(answer.getAttributeValue(ATTRIBUTE_NAME),
                                answer.getAttributeValue(ATTRIBUTE_VALUE));
                    }
                }

                if ((collectionNode = category.getChild(TYPE_CHILDREN)) != null) {

                    // all questions in the category
                    questions = collectionNode.getChildren(TYPE_QUESTION);
                    for (Element question : questions) {

                        key = question.getAttributeValue(ATTRIBUTE_NAME) + HEADER_SEPARATOR + HEADER_TIME;
                        entries.put(key, category.getAttributeValue(ATTRIBUTE_TIME));

                        if ((collectionNode = question.getChild(TYPE_ANSWERS)) != null) {

                            // all answers in the question
                            answers = collectionNode.getChildren(TYPE_ANSWER);
                            for (Element answer : answers) {
                                entries.put(answer.getAttributeValue(ATTRIBUTE_NAME),
                                        answer.getAttributeValue(ATTRIBUTE_VALUE));
                            }
                        }
                    }
                }
            }
        }

        String[] values = new String[entries.size()];
        Iterator<Entry<String, String>> valueIterator = entries.entrySet().iterator();
        for (int i = 0; i < entries.size(); i++) {
            values[i] = valueIterator.next().getValue();
        }

        return values;
    }

    public static String[] makeContentLine(Document document, String experimentCode) { // TODO make private
        Objects.requireNonNull(document, "doc must not be null");

        ArrayList<String> contentElements = new ArrayList<>();
        Element root = document.getRootElement();
        List<Element> categories;
        List<Element> questions;
        List<Element> answers;
        Element collectionNode;

        // the experiment code
        contentElements.add(experimentCode);

        // the experiment time and all its answers
        contentElements.add(root.getAttributeValue(ATTRIBUTE_TIME));
        if ((collectionNode = root.getChild(TYPE_ANSWERS)) != null) {
            answers = collectionNode.getChildren(TYPE_ANSWER);

            for (Element answer : answers) {
                contentElements.add(answer.getAttributeValue(ATTRIBUTE_VALUE));
            }
        }

        if ((collectionNode = root.getChild(TYPE_CHILDREN)) != null) {

            categories = collectionNode.getChildren(TYPE_CATEGORY);
            for (Element category : categories) {

                // the time for the category
                contentElements.add(category.getAttributeValue(ATTRIBUTE_TIME));

                if ((collectionNode = category.getChild(TYPE_ANSWERS)) != null) {

                    // all answers in the category
                    answers = collectionNode.getChildren(TYPE_ANSWER);
                    for (Element answer : answers) {
                        contentElements.add(answer.getAttributeValue(ATTRIBUTE_VALUE));
                    }
                }

                if ((collectionNode = category.getChild(TYPE_CHILDREN)) != null) {

                    // all questions in the category
                    questions = collectionNode.getChildren(TYPE_QUESTION);
                    for (Element question : questions) {

                        // the time for the question
                        contentElements.add(question.getAttributeValue(ATTRIBUTE_TIME));

                        if ((collectionNode = question.getChild(TYPE_ANSWERS)) != null) {

                            // all answers in the question
                            answers = collectionNode.getChildren(TYPE_ANSWER);
                            for (Element answer : answers) {
                                contentElements.add(answer.getAttributeValue(ATTRIBUTE_VALUE));
                            }
                        }
                    }
                }
            }
        }

        return contentElements.toArray(new String[contentElements.size()]);
    }

    public static String[] makeHeader(Document document) { // TODO make private
        Objects.requireNonNull(document, "document must not be null");

        ArrayList<String> headerElements = new ArrayList<>();
        StringBuilder headerBuilder = new StringBuilder();
        Element root = document.getRootElement();
        List<Element> categories;
        List<Element> questions;
        List<Element> answers;
        Element collectionNode;
        int catLength; // the length of the header up to the category level
        int qLength; // the length of the header up to the question level

        headerElements.add("expCode");

        // create header columns for the 'time' attribute of the root node and all its answers
        headerBuilder.append(root.getAttributeValue(ATTRIBUTE_NAME)).append(HEADER_SEPARATOR).append(ATTRIBUTE_TIME);
        headerElements.add(headerBuilder.toString());
        headerBuilder.delete(0, headerBuilder.length());

        headerBuilder.append(root.getAttributeValue(ATTRIBUTE_NAME)).append(HEADER_SEPARATOR);
        catLength = headerBuilder.length();

        if ((collectionNode = root.getChild(TYPE_ANSWERS)) != null) {
            answers = collectionNode.getChildren(TYPE_ANSWER);

            for (Element answer : answers) {
                headerBuilder.append(answer.getAttributeValue(ATTRIBUTE_NAME));
                headerElements.add(headerBuilder.toString());
                headerBuilder.delete(catLength, headerBuilder.length());
            }
        }

        headerBuilder.delete(0, headerBuilder.length());

        if ((collectionNode = root.getChild(TYPE_CHILDREN)) != null) {

            // create column sections for all categories (and their question children)
            categories = collectionNode.getChildren(TYPE_CATEGORY);
            for (Element category : categories) {

                // append the category part of the header
                headerBuilder.append(category.getAttributeValue(ATTRIBUTE_NAME)).append(HEADER_SEPARATOR);
                catLength = headerBuilder.length();

                // the :time column for the category
                headerBuilder.append(ATTRIBUTE_TIME);
                headerElements.add(headerBuilder.toString());
                headerBuilder.delete(catLength, headerBuilder.length());

                if ((collectionNode = category.getChild(TYPE_ANSWERS)) != null) {

                    // columns for all answers in the category
                    answers = collectionNode.getChildren(TYPE_ANSWER);
                    for (Element answer : answers) {
                        headerBuilder.append(answer.getAttributeValue(ATTRIBUTE_NAME));
                        headerElements.add(headerBuilder.toString());
                        headerBuilder.delete(catLength, headerBuilder.length());
                    }
                }

                if ((collectionNode = category.getChild(TYPE_CHILDREN)) != null) {

                    // columns for all questions in the category
                    questions = collectionNode.getChildren(TYPE_QUESTION);
                    for (Element question : questions) {

                        // append the question part of the header
                        headerBuilder.append(question.getAttributeValue(ATTRIBUTE_NAME)).append(HEADER_SEPARATOR);
                        qLength = headerBuilder.length();

                        // the :time column for the question
                        headerBuilder.append(ATTRIBUTE_TIME);
                        headerElements.add(headerBuilder.toString());
                        headerBuilder.delete(qLength, headerBuilder.length());

                        if ((collectionNode = question.getChild(TYPE_ANSWERS)) != null) {

                            // columns for all answers in the question
                            answers = collectionNode.getChildren(TYPE_ANSWER);
                            for (Element answer : answers) {
                                headerBuilder.append(answer.getAttributeValue(ATTRIBUTE_NAME));
                                headerElements.add(headerBuilder.toString());
                                headerBuilder.delete(qLength, headerBuilder.length());
                            }
                        }

                        headerBuilder.delete(catLength, headerBuilder.length());
                    }
                }

                headerBuilder.delete(0, headerBuilder.length());
            }
        }

        return headerElements.toArray(new String[headerElements.size()]);
    }

    /**
     * Inserts the treeNode and all its children to the xmlNode.
     *
     * @param xmlTree
     *         the xml document where the xmlNode resides
     * @param xmlNode
     *         the xml node in which to add the treeNode
     * @param treeNode
     *         the treeNode that is to be converted to xml
     */
    private static void saveXMLNode(org.w3c.dom.Document xmlTree, org.w3c.dom.Element xmlNode,
            QuestionTreeNode treeNode) {

        // add name and value
        xmlNode.setAttribute(ATTRIBUTE_NAME, treeNode.getName());
        xmlNode.setAttribute(ATTRIBUTE_VALUE, treeNode.getValue());

        // add attributes
        if (!treeNode.getAttributes().isEmpty()) {
            org.w3c.dom.Element xmlAttributesNode = xmlTree.createElement(TYPE_ATTRIBUTES);

            xmlNode.appendChild(xmlAttributesNode);

            // add all tree attributes as new xml elements as children of xmlNode.xmlAttributesNode
            for (Entry<String, QuestionTreeNode> treeAttributeEntry : treeNode.getAttributes().entrySet()) {
                QuestionTreeNode treeChild = treeAttributeEntry.getValue();
                org.w3c.dom.Element xmlChild = xmlTree.createElement(treeChild.getType());

                xmlAttributesNode.appendChild(xmlChild);

                saveXMLNode(xmlTree, xmlChild, treeChild);
            }
        }

        // add children
        if (treeNode.getChildCount() > 0) {
            org.w3c.dom.Element xmlChildrenNode = xmlTree.createElement(TYPE_CHILDREN);

            xmlNode.appendChild(xmlChildrenNode);

            for (int i = 0; i < treeNode.getChildCount(); i++) {
                QuestionTreeNode treeChild = (QuestionTreeNode) treeNode.getChildAt(i);
                org.w3c.dom.Element xmlChild = xmlTree.createElement(treeChild.getType());

                xmlChildrenNode.appendChild(xmlChild);

                saveXMLNode(xmlTree, xmlChild, treeChild);
            }
        }
    }

    /**
     * Writes a QuestionTreeNode and all its children to a XML-File.
     *
     * @param treeRoot
     *         QuestionTreeNode which should be added (with children)
     * @param path
     *         path where to save the xml-file
     */
    public static void saveXMLTree(QuestionTreeNode treeRoot, String path) {
        org.w3c.dom.Document xmlTree = null;

        // Create file
        File parentDir = new File(path).getParentFile();

        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try {
            xmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }

        // Create root node
        org.w3c.dom.Element xmlRoot = xmlTree.createElement(treeRoot.getType());
        xmlTree.appendChild(xmlRoot);
        saveXMLNode(xmlTree, xmlRoot, treeRoot);

        // Save to file
        try {
            if (xmlTree != null) {
                TransformerFactory.newInstance().newTransformer()
                        .transform(new DOMSource(xmlTree), new StreamResult(path));
            }
        } catch (Exception | Error e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Adds all the answers in the treeNode and its children to the given xmlNode. Children of treeNode will be
     * converted to xml nodes.
     *
     * @param xmlTree
     *         the xml document where the xmlNode resides
     * @param xmlNode
     *         the xml node in which to add the treeNode
     * @param treeNode
     *         the treeNode that is to be converted to xml
     */
    private static void saveXMLAnswerNode(org.w3c.dom.Document xmlTree, org.w3c.dom.Element xmlNode,
            QuestionTreeNode treeNode) {
        // add name and time
        xmlNode.setAttribute(ATTRIBUTE_NAME, treeNode.getName());
        xmlNode.setAttribute(ATTRIBUTE_TIME, "" + treeNode.getAnswerTime());

        // add answers
        if (treeNode.getAnswers().size() > 0) {
            org.w3c.dom.Element xmlAnswersNode = xmlTree.createElement(TYPE_ANSWERS);

            xmlNode.appendChild(xmlAnswersNode);

            // add the single answers
            for (Entry<String, String> answerEntry : treeNode.getAnswers().entrySet()) {
                org.w3c.dom.Element xmlChild = xmlTree.createElement(TYPE_ANSWER);

                xmlChild.setAttribute(ATTRIBUTE_NAME, answerEntry.getKey());
                xmlChild.setAttribute(ATTRIBUTE_VALUE, answerEntry.getValue());

                xmlAnswersNode.appendChild(xmlChild);
            }
        }

        // add children
        if (treeNode.getChildCount() > 0) {
            org.w3c.dom.Element xmlChildrenNode = xmlTree.createElement(TYPE_CHILDREN);

            xmlNode.appendChild(xmlChildrenNode);

            for (int i = 0; i < treeNode.getChildCount(); i++) {
                QuestionTreeNode treeChild = (QuestionTreeNode) treeNode.getChildAt(i);
                org.w3c.dom.Element xmlChild = xmlTree.createElement(treeChild.getType());

                xmlChildrenNode.appendChild(xmlChild);

                saveXMLAnswerNode(xmlTree, xmlChild, treeChild);
            }
        }
    }

    /**
     * Writes a <code>QuestionTreeNode</code> with all its children into a XML-File.
     *
     * @param treeRoot
     *         QuestionTreeNode which should be added (with children)
     * @param path
     *         path where to save the xml-file
     */
    public static void saveXMLAnswerTree(QuestionTreeNode treeRoot, String path) {
        org.w3c.dom.Document xmlTree = null;

        // create file
        File dir = new File(path).getParentFile();

        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            xmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }

        // create root node
        org.w3c.dom.Element xmlRoot = xmlTree.createElement(treeRoot.getType());
        xmlTree.appendChild(xmlRoot);
        saveXMLAnswerNode(xmlTree, xmlRoot, treeRoot);

        // save to file
        try {
            if (xmlTree != null) {
                TransformerFactory.newInstance().newTransformer()
                        .transform(new DOMSource(xmlTree), new StreamResult(path));
            }
        } catch (Exception | Error e1) {
            e1.printStackTrace();
        }
    }

    /**
     * method which adds recursively the childs (to a DataTreeNode file)
     *
     * @param xmlChildren
     *         the childs which should be added
     * @param treeParent
     *         the DataTreeNode which should get the childs
     */
    private static QuestionTreeNode loadXMLNode(Node xmlNode) {
        QuestionTreeNode result = new QuestionTreeNode(xmlNode.getNodeName());
        // Name und Value setzen
        Node xmlNameNode = xmlNode.getAttributes().getNamedItem(ATTRIBUTE_NAME);
        if (xmlNameNode != null) {
            result.setName(xmlNameNode.getNodeValue());
        }
        Node xmlValueNode = xmlNode.getAttributes().getNamedItem(ATTRIBUTE_VALUE);
        if (xmlValueNode != null) {
            result.setValue(xmlValueNode.getNodeValue());
        }
        // Attribute und Kinder hinzuf�gen
        NodeList xmlMetaNodes = xmlNode.getChildNodes();
        for (int i = 0; i < xmlMetaNodes.getLength(); i++) {
            Node xmlMetaNode = xmlMetaNodes.item(i);
            if (xmlMetaNode.getNodeName().equals(TYPE_ATTRIBUTES) && xmlMetaNode.hasChildNodes()) {
                NodeList xmlAttributesList = xmlMetaNode.getChildNodes();
                for (int j = 0; j < xmlAttributesList.getLength(); j++) {
                    Node xmlAttributeNode = xmlAttributesList.item(j);
                    QuestionTreeNode attributeNode = loadXMLNode(xmlAttributeNode);
                    result.setAttribute(attributeNode.getName(), attributeNode);
                }
            } else if (xmlMetaNode.getNodeName().equals(TYPE_CHILDREN) && xmlMetaNode.hasChildNodes()) {
                NodeList xmlChildrenList = xmlMetaNode.getChildNodes();
                for (int j = 0; j < xmlChildrenList.getLength(); j++) {
                    Node xmlChildNode = xmlChildrenList.item(j);
                    result.add(loadXMLNode(xmlChildNode));
                }
            }
        }
        return result;
    }

    /**
     * loads an xml file an creates an corresponding DataTreeNode
     *
     * @param path
     *         path of the file
     *
     * @return root of the new tree-structure
     */
    public static QuestionTreeNode loadXMLTree(String path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        try {
            // Document lesen
            org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            // Wurzel holen
            Node xmlRoot = doc.getFirstChild();
            return loadXMLNode(xmlRoot);
        } catch (Exception e) {
            return null;
        }
    }

    private static QuestionTreeNode loadAnswerXMLNode(Node xmlNode) {
        QuestionTreeNode result = new QuestionTreeNode(xmlNode.getNodeName());
        Node xmlNameNode = xmlNode.getAttributes().getNamedItem(ATTRIBUTE_NAME);
        // Name und Value setzen
        if (xmlNameNode != null) {
            result.setName(xmlNameNode.getNodeValue());
        }
        Node xmlValueNode = xmlNode.getAttributes().getNamedItem(ATTRIBUTE_VALUE);
        if (xmlValueNode != null) {
            result.setValue(xmlValueNode.getNodeValue());
        }
        // Attribute hinzuf�gen
        NamedNodeMap xmlAttributeMap = xmlNode.getAttributes();
        for (int i = 0; i < xmlAttributeMap.getLength(); i++) {
            Node xmlAttribute = xmlAttributeMap.item(i);
            QuestionTreeNode attributeNode =
                    new QuestionTreeNode(QuestionTreeNode.TYPE_ATTRIBUTE, xmlAttribute.getNodeName());
            attributeNode.setValue(xmlAttribute.getNodeValue());
            result.setAttribute(xmlAttribute.getNodeName(), attributeNode);
        }
        // Answers und Kinder hinzuf�gen
        NodeList xmlMetaNodes = xmlNode.getChildNodes();
        for (int i = 0; i < xmlMetaNodes.getLength(); i++) {
            Node xmlMetaNode = xmlMetaNodes.item(i);
            if (xmlMetaNode.getNodeName().equals(TYPE_ANSWERS) && xmlMetaNode.hasChildNodes()) {
                NodeList xmlAttributesList = xmlMetaNode.getChildNodes();
                for (int j = 0; j < xmlAttributesList.getLength(); j++) {
                    Node xmlAttributeNode = xmlAttributesList.item(j);
                    result.setAnswer(xmlAttributeNode.getAttributes().getNamedItem(ATTRIBUTE_NAME).getNodeValue(),
                            xmlAttributeNode.getAttributes().getNamedItem(ATTRIBUTE_VALUE).getNodeValue());
                }
            } else if (xmlMetaNode.getNodeName().equals(TYPE_CHILDREN) && xmlMetaNode.hasChildNodes()) {
                NodeList xmlChildrenList = xmlMetaNode.getChildNodes();
                for (int j = 0; j < xmlChildrenList.getLength(); j++) {
                    Node xmlChildNode = xmlChildrenList.item(j);
                    result.add(loadAnswerXMLNode(xmlChildNode));
                }
            }
        }
        return result;
    }

    public static QuestionTreeNode loadAnswerXMLTree(String path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        try {
            // Document lesen
            org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            // Wurzel holen
            Node xmlRoot = doc.getFirstChild();
            return loadAnswerXMLNode(xmlRoot);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param formInfo
     *         Liste(Knotenname, Liste(Formname, Formvalue)) --> enth�lt alle
     *         formularknoten
     * @param answerNodes
     *         Alle root Knoten aus den answer-xml Dateien (1 pro datei)
     * @param experimentCode
     *         code des derzeitigen Experimentes
     */
    public static void saveAsCSVFile(ArrayList<Pair<QuestionTreeNode, ArrayList<Pair<String, String>>>> formInfo,
            ArrayList<QuestionTreeNode> answerNodes, String experimentCode, String path) {
        String lineSep = System.getProperty("line.separator");
        FileWriter fw;
        BufferedWriter bw;
        StringBuilder header = new StringBuilder();
        StringBuilder line = new StringBuilder();

        try {
            // TODO check if valid path
            fw = new FileWriter(new File(path + System.getProperty("file.separator") + experimentCode + ".csv"));
            bw = new BufferedWriter(fw);
            // write header
            header.append("\"expCode\";\"probCode\"");

            for (Pair<QuestionTreeNode, ArrayList<Pair<String, String>>> questionInfo : formInfo) {
                header.append(";\"" + questionInfo.getKey().getName() + "::time\"");
                ArrayList<Pair<String, String>> questionForms = questionInfo.getValue();

                for (Pair<String, String> questionForm : questionForms) {
                    String formName = questionForm.getKey();
                    if (formName != null) {
                        header.append(";\"" + questionInfo.getKey().getName() + "::" + formName + "\"");
                    }
                }
            }
            bw.write(header + lineSep);

            // Daten schreiben
            for (QuestionTreeNode currentNode : answerNodes) {
                int nodeIndex = 0;
                line.append("\"" + experimentCode.replaceAll("\"", "\"\"") + "\"");

                //probCode
                if (currentNode.getAnswer(Constants.KEY_SUBJECT) != null) {
                    line.append(";\"" + currentNode.getAnswer(Constants.KEY_SUBJECT).replaceAll("\"", "\"\"") + "\"");
                } else {
                    line.append(";\"\"");
                }

                while (currentNode != null) {
                    // Times
                    line.append(";\"" + currentNode.getAttributeValue(ATTRIBUTE_TIME) + "\"");

                    // Answers
                    ArrayList<Pair<String, String>> questionForms = formInfo.get(nodeIndex).getValue();

                    for (Pair<String, String> questionForm : questionForms) {
                        String value = currentNode.getAnswer(questionForm.getKey());
                        if (value == null) {
                            line.append(";\"\"");
                        } else {
                            line.append(";\"" + value.replaceAll("\"", "\"\"") + "\"");
                        }
                    }
                    currentNode = (QuestionTreeNode) currentNode.getNextNode();
                    nodeIndex++;
                }

                bw.write(line + lineSep);
                line = new StringBuilder();
            }

            bw.close();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, UIElementNames.QUESTION_TREE_XML_MESSAGE_ERROR_WHILE_WRITING_CSV);
        }
    }
}