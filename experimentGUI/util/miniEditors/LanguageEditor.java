package experimentGUI.util.miniEditors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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

/**
 * This ist a little programm to create/manipulate a xml File, which could be
 * used from language.java
 * 
 * @author Markus Köppen, Andreas Hasselberg
 * 
 */
public class LanguageEditor extends JFrame {

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
	 * standard language constant
	 */
	public static String LANGUAGE_GERMAN = "deutsch";

	private JList list;
	private DefaultListModel listModel;
	private JTextField textField;
	private JTextArea textArea;
	private JComboBox comboBox;
	private DefaultComboBoxModel boxModel;
	private JTextField lanTextField;
	private JButton removeLanButton;
	private JMenuItem loadMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem closeMenuItem;
	private JButton addButton;
	private JButton removeButton;
	private JButton addLanButton;
	private JMenuItem saveAsMenuItem;
	private JMenuItem newMenuItem;

	/**
	 * the current path for the file to save in
	 */
	private String path;
	/**
	 * structure to save the different languages
	 */
	private ArrayList<String> languages;
	/**
	 * structure to save the keyword and the corresponding words/sentences the
	 * order of these words is the same as the language order
	 */
	private HashMap<String, HashMap<String, String>> keywords;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LanguageEditor frame = new LanguageEditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creating the GUI and initializing
	 */
	public LanguageEditor() {
		// initialise the data structures
		languages = new ArrayList<String>();
		keywords = new HashMap<String, HashMap<String, String>>();
		path = null;

		// path = "language.xml";
		// load(path);

		// build up GUI
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);
		saveMenuItem = new JMenuItem("Speichern");

		newMenuItem = new JMenuItem("Neu");
		fileMenu.add(newMenuItem);
		loadMenuItem = new JMenuItem("Laden");
		fileMenu.add(loadMenuItem);
		fileMenu.add(saveMenuItem);

		saveAsMenuItem = new JMenuItem("Speichern unter");
		fileMenu.add(saveAsMenuItem);
		closeMenuItem = new JMenuItem("Beenden");
		fileMenu.add(closeMenuItem);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.SOUTH);

		addButton = new JButton("Hinzuf\u00FCgen");
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		panel.add(addButton);
		removeButton = new JButton("Entfernen");
		panel.add(removeButton);

		listModel = new DefaultListModel();
		list = new JList();
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(listModel);
		list.setPreferredSize(new Dimension(150, 0));

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPanel.add(textArea, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPanel.add(panel_2, BorderLayout.NORTH);

		comboBox = new JComboBox();
		boxModel = new DefaultComboBoxModel();
		comboBox.setModel(boxModel);
		panel_2.add(comboBox);

		lanTextField = new JTextField();
		panel_2.add(lanTextField);
		lanTextField.setColumns(10);

		addLanButton = new JButton("Neue Sprache");
		panel_2.add(addLanButton);
		removeLanButton = new JButton("Sprache entfernen");
		panel_2.add(removeLanButton);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setLeftComponent(list);
		splitPane.setRightComponent(contentPanel);
		contentPane.add(splitPane, BorderLayout.CENTER);

		showInfos();
		setListener();
		// create empty rack
		newMenuItem.doClick();
	}

	/**
	 * set all Listeners and define theyr methods
	 */
	private void setListener() {
		// close program
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		// new language specification
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				path = null;
				keywords.clear();
				listModel.clear();
				languages.clear();
				languages.add(LANGUAGE_GERMAN);
				boxModel.removeAllElements();
				boxModel.addElement(LANGUAGE_GERMAN);
			}
		});
		// save current language specification
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (path == null) {
					saveAsMenuItem.doClick();
				} else {
					save(path);
				}
			}
		});
		// save-as option
		saveAsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				int n = fileChooser.showSaveDialog(null);
				if (n == JFileChooser.APPROVE_OPTION) {
					path = fileChooser.getSelectedFile().getAbsolutePath();
					save(path);
				}
			}
		});
		// load language specification
		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				int n = fileChooser.showOpenDialog(null);
				if (n == JFileChooser.APPROVE_OPTION) {
					path = fileChooser.getSelectedFile().getAbsolutePath();
					load(path);
					showInfos();
				}
			}
		});
		// add language
		addLanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String lanName = lanTextField.getText();
				if (!lanName.equals("")) {
					languages.add(lanName);
					boxModel.addElement(lanName);
					Iterator<HashMap<String, String>> listIterator = keywords.values().iterator();
					while (listIterator.hasNext()) {
						listIterator.next().put(lanName, "");
					}
				}
			}
		});
		// remove language
		removeLanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (boxModel.getSize() == 1) {
					System.out.println("Es muss mindestens eine Sprache existieren");
				} else {
					int lanIndex = comboBox.getSelectedIndex();
					String lanName = comboBox.getSelectedItem().toString();
					languages.remove(lanIndex);
					Iterator<HashMap<String, String>> listIterator = keywords.values().iterator();
					while (listIterator.hasNext()) {
						listIterator.next().remove(lanName);
					}
					boxModel.removeElementAt(lanIndex);
					list.clearSelection();
					textArea.setText("");
					textArea.setEnabled(false);
				}
			}
		});
		// add keyword
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String keyword = getFreeName(textField.getText());
				// ArrayList<String> specs = new ArrayList<String>();
				// for (int i = 0; i < languages.size(); i++) {
				// specs.add("");
				// }
				keywords.put(keyword, new HashMap<String, String>());

				listModel.addElement(keyword);
			}
		});
		// remove keyword
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = list.getSelectedIndex();
				if (index != -1) {
					String key = listModel.get(index).toString();
					keywords.remove(key);
					listModel.remove(index);
					list.clearSelection();
				}
			}
		});
		// list selection
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int listIndex = list.getSelectedIndex();
				if (listIndex != -1) {
					textArea.setEnabled(true);
					textArea.setText(keywords.get(listModel.elementAt(listIndex)).get(
							comboBox.getSelectedItem().toString()));
				} else {
					textArea.setEnabled(false);
					textArea.setText("");
				}
			}
		});
		// comboBox selection
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int listIndex = list.getSelectedIndex();
				if (listIndex != -1) {
					textArea.setEnabled(true);
					textArea.setText(keywords.get(listModel.elementAt(listIndex)).get(
							comboBox.getSelectedItem().toString()));
				} else {
					textArea.setEnabled(false);
					textArea.setText("");
				}
			}
		});
		// save changes in textarea
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			private void valueChanged() {
				String lanName = comboBox.getSelectedItem().toString();
				int keyIndex = list.getSelectedIndex();
				if (keyIndex != -1) {
					String key = listModel.getElementAt(keyIndex).toString();

					HashMap<String, String> alternatives = keywords.get(key);
					alternatives.put(lanName, textArea.getText());
					keywords.put(key, alternatives);
				}
			}

			public void changedUpdate(DocumentEvent arg0) {
				valueChanged();
			}

			public void insertUpdate(DocumentEvent arg0) {
				valueChanged();
			}

			public void removeUpdate(DocumentEvent arg0) {
				valueChanged();
			}
		});
	}

	/**
	 * load the data in a xml file
	 * 
	 * @param path
	 *            location and name of the file
	 */
	private void load(String path) {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);
			Node xmlRoot = doc.getFirstChild();
			Node child = null;
			NodeList children = xmlRoot.getChildNodes();
			languages.clear();
			keywords.clear();
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
	 * save the data in a xml file
	 * 
	 * @param path
	 *            path and location of the xml file
	 */
	private void save(String path) {
		Document xmlTree = null;
		try {
			xmlTree = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element xmlRoot = xmlTree.createElement("LanguageSpecifications");
			xmlTree.appendChild(xmlRoot);
			// language specifications
			Element lans = xmlTree.createElement(ELEMENT_LANGUAGES);
			for (String lan : languages) {
				Element lanName = xmlTree.createElement(ELEMENT_LANGUAGE);
				lanName.setTextContent(lan);
				lans.appendChild(lanName);
			}
			xmlRoot.appendChild(lans);
			// keywords
			Iterator<String> keyIterator = keywords.keySet().iterator();
			Iterator<HashMap<String, String>> valueIterator = keywords.values().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next();
				HashMap<String, String> specifications = valueIterator.next();
				Element keyEle = xmlTree.createElement(ELEMENT_KEYWORD);
				keyEle.setAttribute(ATTRIBUTE_NAME, key);

				for (int i = 0; i < specifications.size(); i++) {
					Element specEle = xmlTree.createElement(ELEMENT_KEY_LAN);
					specEle.setAttribute(ATTRIBUTE_LANGUAGE, languages.get(i));
					specEle.setAttribute(ATTRIBUTE_INTERPRETATION, specifications.get(languages.get(i)));
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
	 * shows the current data in the JList and JComboBox Clears the JList
	 * selection and the JTextArea
	 */
	private void showInfos() {
		list.clearSelection();
		listModel.removeAllElements();
		Iterator<String> keywordIterator = keywords.keySet().iterator();
		while (keywordIterator.hasNext()) {
			listModel.addElement(keywordIterator.next());
		}
		boxModel.removeAllElements();
		for (String language : languages) {
			boxModel.addElement(language);
		}
		textArea.setText("");
		textArea.setEnabled(false);
	}

	/**
	 * gets the next free name for a keyword by appending a "'" if needed
	 * 
	 * @param name
	 *            keyword name which is wanted
	 * @return next free keyword name
	 */
	private String getFreeName(String name) {
		if (name.equals("")) {
			return getFreeName("default");
		} else if (listModel.contains(name)) {
			return getFreeName(name + "'");
		} else {
			return name;
		}
	}
}