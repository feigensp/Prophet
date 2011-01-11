package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
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
//			RSyntaxTextArea textArea = new RSyntaxTextArea();
//			RTextScrollPane scollPane = RTextScrollPane(textArea);
			loadXMLTree("annotations.xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> loadXMLTree(
			String path) throws FileNotFoundException {
		HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> infos = new HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>>();
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			// projekte
			NodeList projectList = doc.getChildNodes();
			for (int i = 0; i < projectList.getLength(); i++) {
				// dateien und ordner
				if (projectList.item(i).getChildNodes().getLength() > 0) {
					filesAndDirs(projectList.item(i).getChildNodes(), "", infos);
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Iterator<String> paths = infos.keySet().iterator();
		Iterator<ArrayList<Triple<Integer, Integer, ArrayList<String>>>> moreInfos = infos.values()
				.iterator();
		while (paths.hasNext()) {
			System.out.println(paths.next());
			ArrayList<Triple<Integer, Integer, ArrayList<String>>> al = moreInfos.next();
			System.out.println(al.size());
//			 for(int i=0; i<al.size(); i++) {
//			 System.out.println("\tOffset=" + al.get(i).getKey());
//			 System.out.println("\tLength=" + al.get(i).getValue1());
//			 for (int j = 0; j < al.get(i).getValue2().size(); j++) {
//			 System.out.println("\t\tFeature=" +
//			 al.get(i).getValue2().get(j));
//			 }
//			 }
		}
		return infos;
	}

	private static void filesAndDirs(NodeList list, String path,
			HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> infos) {
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals(NODE_FOLDER)) {
				String folder = list.item(i).getAttributes().getNamedItem(ATTRIBUE_NAME).getNodeValue();
				filesAndDirs(list.item(i).getChildNodes(), path + System.getProperty("file.separator") + folder, infos);
			} else if (list.item(i).getNodeName().equals(NODE_FILE)) {
				String file = list.item(i).getAttributes().getNamedItem(ATTRIBUE_NAME).getNodeValue();
				fragments(list.item(i).getChildNodes(), path + System.getProperty("file.separator") + file, infos);
			}
		}
	}

	private static void fragments(NodeList list, String path,
			HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> infos) {
		for (int i = 0; i < list.getLength(); i++) {
			Node fragment = list.item(i);
			if (!fragment.getNodeName().equals("#text")) {
				int offset = -1;
				int length = -1;
				NamedNodeMap fragAttributes = fragment.getAttributes();
				if (fragAttributes.getLength() > 0) {
					try {
						offset = Integer.parseInt(fragAttributes.getNamedItem(ATTRIBUE_OFFSET).getNodeValue());
						length = Integer.parseInt(fragAttributes.getNamedItem(ATTRIBUE_LENGTH).getNodeValue());
					} catch (NumberFormatException e0) {
						System.err.print("corrupt xml file");
					}
					// Features
					ArrayList<String> features = new ArrayList<String>();
					NodeList featureList = fragment.getChildNodes();
					for (int j = 0; j < featureList.getLength(); j++) {
						if (!featureList.item(j).getNodeName().equals("#text")) {
							features.add(featureList.item(j).getTextContent());
						}
					}
					if (infos.get(path) == null) {
						infos.put(path, new ArrayList<Triple<Integer, Integer, ArrayList<String>>>());
					}
					infos.get(path).add(
							new Triple<Integer, Integer, ArrayList<String>>(offset, length, features));
				}
			}
		}
	}
}
