package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LoadAnnotationsTest {

	public static final String NODE_PROJECT = "project";
	public static final String ATTRIBUE_NAME = "name";
	public static final String NODE_FILE = "file";
	public static final String NODE_FOLDER = "folder";
	public static final String NODE_FRAGMENT = "fragment";
	public static final String ATTRIBUE_LENGTH = "length";
	public static final String ATTRIBUE_OFFSET = "offset";
	public static final String NODE_FEATURE = "feature";

	public static void main(String[] args) {
		try {
			loadXMLTree("annotations.xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void loadXMLTree(String path) throws FileNotFoundException {
		String whitespaces = "";

		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			// projekte
			NodeList projectList = doc.getChildNodes();
			for (int i = 0; i < projectList.getLength(); i++) {
				System.out.println(projectList.item(i).getNodeName() + ":" + i);
				// dateien und ordner
				filesAndDirs(projectList.item(i).getChildNodes(), whitespaces + "\t");
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private static void filesAndDirs(NodeList list, String whitespaces) throws IOException {
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals(NODE_FOLDER)) {
				System.out.println(whitespaces + "folder (" + list.item(i).getNodeName() + "): "
						+ list.item(i).getAttributes().getNamedItem(ATTRIBUE_NAME));
				filesAndDirs(list.item(i).getChildNodes(), whitespaces + "\t");
			} else if (list.item(i).getNodeName().equals(NODE_FILE)) {
				System.out.println(whitespaces + "file (" + list.item(i).getNodeName() + "): "
						+ list.item(i).getAttributes().getNamedItem(ATTRIBUE_NAME));
				fragments(list.item(i).getChildNodes(), whitespaces + "\t");
			}
		}
	}

	private static void fragments(NodeList list, String whitespaces) throws IOException {
		for (int i = 0; i < list.getLength(); i++) {
			if (!list.item(i).getNodeName().equals("#text")) {
				System.out.println(whitespaces + "Fragment (" + list.item(i).getNodeName() + "): offset="
						+ list.item(i).getAttributes().getNamedItem(ATTRIBUE_OFFSET) + " : length="
						+ list.item(i).getAttributes().getNamedItem(ATTRIBUE_LENGTH));
				// Features
				for (int j = 0; j < list.item(i).getChildNodes().getLength(); j++) {
					if (!list.item(i).getChildNodes().item(j).getNodeName().equals("#text")) {
						System.out.println(whitespaces + "\tFeature ("
								+ list.item(i).getChildNodes().item(j).getNodeName() + "): "
								+ list.item(i).getChildNodes().item(j).getTextContent());
					}
				}
			}
		}
	}
}
