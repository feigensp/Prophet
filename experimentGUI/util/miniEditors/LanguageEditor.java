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

public class LanguageEditor extends JFrame {

	public static final String ELEMENT_LANGUAGES = "languages";
	public static final String ELEMENT_LANGUAGE = "language";
	public static final String ELEMENT_KEYWORD = "keyword";
	public static final String ATTRIBUTE_NAME = "name";
	public static final String ELEMENT_KEY_LAN = "languageInterpretation";
	public static final String ATTRIBUTE_LANGUAGE = "language";
	public static final String ATTRIBUTE_INTERPRETATION = "interpretation";

	private JList list;
	private DefaultListModel listModel;
	private JTextField textField;
	private JTextArea textArea;
	private JComboBox comboBox;
	private ArrayList<String> languages;
	private HashMap<String, ArrayList<String>> keywords;
	private JTextField lanTextField;
	private DefaultComboBoxModel boxModel;
	private JButton removeLanButton;
	private JMenuItem loadMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem closeMenuItem;
	private JButton addButton;
	private JButton removeButton;
	private JButton addLanButton;

	private String path;
	private JMenuItem saveAsMenuItem;

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
	 * Create the frame.
	 */
	public LanguageEditor() {
		languages = new ArrayList<String>();
		keywords = new HashMap<String, ArrayList<String>>();
		path = "language.xml";

		load(path);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);
		saveMenuItem = new JMenuItem("Speichern");
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
	}

	private void setListener() {
		// close program
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		// save current language specification
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save(path);
			}
		});
		// save-as option
		saveAsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("yay");
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
					Iterator<ArrayList<String>> listIterator = keywords.values().iterator();
					while (listIterator.hasNext()) {
						listIterator.next().add("");
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
					String lanName = comboBox.getSelectedItem().toString();
					int lanIndex = getLanIndex(languages, lanName);
					languages.remove(lanIndex);
					Iterator<ArrayList<String>> listIterator = keywords.values().iterator();
					while (listIterator.hasNext()) {
						listIterator.next().remove(lanIndex);
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
				ArrayList<String> specs = new ArrayList<String>();
				for (int i = 0; i < languages.size(); i++) {
					specs.add("");
				}
				keywords.put(keyword, specs);

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
							comboBox.getSelectedIndex()));
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
							comboBox.getSelectedIndex()));
				} else {
					textArea.setEnabled(false);
					textArea.setText("");
				}
			}
		});
		// save changes in textarea
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			private void valueChanged() {
				int lanIndex = comboBox.getSelectedIndex();
				int keyIndex = list.getSelectedIndex();
				if (keyIndex != -1) {
					String key = listModel.getElementAt(keyIndex).toString();

					ArrayList<String> alternatives = keywords.get(key);
					alternatives.set(lanIndex, textArea.getText());
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
			Iterator<ArrayList<String>> valueIterator = keywords.values().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next();
				ArrayList<String> specifications = valueIterator.next();
				Element keyEle = xmlTree.createElement(ELEMENT_KEYWORD);
				keyEle.setAttribute(ATTRIBUTE_NAME, key);

				for (int i = 0; i < specifications.size(); i++) {
					Element specEle = xmlTree.createElement(ELEMENT_KEY_LAN);
					specEle.setAttribute(ATTRIBUTE_LANGUAGE, languages.get(i));
					specEle.setAttribute(ATTRIBUTE_INTERPRETATION, specifications.get(i));
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

	private String getFreeName(String name) {
		if (name.equals("")) {
			return getFreeName("default");
		} else if (listModel.contains(name)) {
			return getFreeName(name + "'");
		} else {
			return name;
		}
	}

	private int getLanIndex(ArrayList<String> list, String content) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(content)) {
				return i;
			}
		}
		return -1;
	}
}