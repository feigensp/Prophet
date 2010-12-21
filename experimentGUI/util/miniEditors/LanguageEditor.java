package experimentGUI.util.miniEditors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
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

		setListener();
		newMenuItem.doClick();
		// create empty rack
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
				keywords.put(LANGUAGE_GERMAN, new HashMap<String, String>());
				showInfos();
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
					boxModel.addElement(lanName);
					keywords.put(lanName, new HashMap<String, String>());
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
					keywords.remove(lanName);
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
				String lanName = comboBox.getSelectedItem().toString();
				keywords.get(lanName).put(keyword, "");
				listModel.addElement(keyword);
			}
		});
		// remove keyword
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = list.getSelectedIndex();
				if (index != -1) {
					String lanName = comboBox.getSelectedItem().toString();
					String key = listModel.get(index).toString();
					keywords.get(lanName).remove(key);
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
					String lanName = comboBox.getSelectedItem().toString();
					textArea.setEnabled(true);
					textArea.setText(keywords.get(lanName).get(listModel.elementAt(listIndex)));
				} else {
					textArea.setEnabled(false);
					textArea.setText("");
				}
			}
		});
		// comboBox selection
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				String lanName = (String) comboBox.getSelectedItem();
				if (lanName != null) {
					listModel.clear();
					Iterator<String> keywordIterator = keywords.get(lanName).keySet().iterator();
					while (keywordIterator.hasNext()) {
						listModel.addElement(keywordIterator.next());
					}
					textArea.setEnabled(false);
					textArea.setText("");
					list.clearSelection();
				}
			}
		});
		// save changes in textarea
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			private void valueChanged() {
				int keyIndex = list.getSelectedIndex();
				if (keyIndex != -1) {
					String lanName = comboBox.getSelectedItem().toString();
					String key = listModel.getElementAt(keyIndex).toString();
					keywords.get(lanName).put(key, textArea.getText());
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
			NodeList children = xmlRoot.getChildNodes();
			keywords.clear();
			for (int i = 0; i < children.getLength(); i++) {
				// keywords
				Node lanNode = children.item(i);
				String language = lanNode.getAttributes().getNamedItem(ATTRIBUTE_LANGUAGE).getNodeValue();
				HashMap<String, String> xmlKeywords = new HashMap<String, String>();
				NodeList keyList = lanNode.getChildNodes();
				for (int j = 0; j < keyList.getLength(); j++) {
					Node keyNode = keyList.item(j);
					String interpretString = keyNode.getAttributes().getNamedItem(ATTRIBUTE_INTERPRETATION)
							.getNodeValue();
					String keyString = keyNode.getAttributes().getNamedItem(ATTRIBUTE_NAME).getNodeValue();
					xmlKeywords.put(keyString, interpretString);
				}
				keywords.put(language, xmlKeywords);
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
			// keywords
			Iterator<String> keyIterator = keywords.keySet().iterator();
			Iterator<HashMap<String, String>> valueIterator = keywords.values().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next();
				HashMap<String, String> specifications = valueIterator.next();
				Element keyEle = xmlTree.createElement(ELEMENT_LANGUAGE);
				keyEle.setAttribute(ATTRIBUTE_LANGUAGE, key);

				Iterator<String> keywordIterator = specifications.keySet().iterator();
				Iterator<String> interpretationIterator = specifications.values().iterator();
				while (keywordIterator.hasNext()) {
					String keyword = keywordIterator.next();
					String interpretation = interpretationIterator.next();
					Element specEle = xmlTree.createElement(ELEMENT_KEYWORD);
					specEle.setAttribute(ATTRIBUTE_NAME, keyword);
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
	 * shows the current data in the JList and JComboBox Clears the JList
	 * selection and the JTextArea
	 */
	private void showInfos() {
		boxModel.removeAllElements();

		Iterator<String> languageIterator = keywords.keySet().iterator();
		while (languageIterator.hasNext()) {
			boxModel.addElement(languageIterator.next());
		}
		comboBox.setSelectedIndex(0);
		String lanName = comboBox.getSelectedItem().toString();
		list.clearSelection();
		listModel.removeAllElements();
		Iterator<String> keywordIterator = keywords.get(lanName).keySet().iterator();
		while (keywordIterator.hasNext()) {
			String keyword = keywordIterator.next();
			listModel.addElement(keyword);
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