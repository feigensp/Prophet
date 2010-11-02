package questionEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
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
import java.awt.Dimension;

public class MakroCreator extends JFrame {

	private JPanel contentPane;

	private JList makroList;
	private DefaultListModel listModel;
	private JTextPane makroContentTextPane;
	private JTextField makroNameTextField;

	private ArrayList<ElementAttribute<String>> makros;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MakroCreator frame = new MakroCreator();
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
	public MakroCreator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 491, 300);

		JMenuBar makroMenuBar = new JMenuBar();
		setJMenuBar(makroMenuBar);

		JMenu makroMenu = new JMenu("Datei");
		makroMenuBar.add(makroMenu);

		JMenuItem saveMenuItem = new JMenuItem("Speichern");
		makroMenu.add(saveMenuItem);

		JMenuItem closeMenuItem = new JMenuItem("Beenden");
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		makroMenu.add(closeMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel overviewPanel = new JPanel();
		contentPane.add(overviewPanel, BorderLayout.WEST);
		overviewPanel.setLayout(new BorderLayout(0, 0));

		listModel = new DefaultListModel();
		makroList = new JList(listModel);
		makroList.setPreferredSize(new Dimension(150, 0));

		overviewPanel.add(makroList, BorderLayout.CENTER);

		JPopupMenu makroMenuPopupMenu = new JPopupMenu();
		addPopup(makroList, makroMenuPopupMenu);
		
		JMenuItem newMakroMenuItem = new JMenuItem("Neues Makro");
		makroMenuPopupMenu.add(newMakroMenuItem);

		JMenuItem removeMakroMenuItem = new JMenuItem("Makro L\u00F6schen");
		makroMenuPopupMenu.add(removeMakroMenuItem);

		JPanel makroPanel = new JPanel();
		contentPane.add(makroPanel, BorderLayout.CENTER);
		makroPanel.setLayout(new BorderLayout(0, 0));

		makroNameTextField = new JTextField();
		makroPanel.add(makroNameTextField, BorderLayout.NORTH);
		makroNameTextField.setColumns(10);

		makroContentTextPane = new JTextPane();
		makroPanel.add(makroContentTextPane, BorderLayout.CENTER);

		// makroansicht aktualisieren
		makroList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (makroList.getSelectedIndex() != -1) {
					makroNameTextField.setText(makros.get(
							makroList.getSelectedIndex()).getName());
					makroContentTextPane.setText(makros.get(
							makroList.getSelectedIndex()).getContent());
					makroNameTextField.setEnabled(true);
					makroContentTextPane.setEnabled(true);
				} else {
					makroNameTextField.setText("");
					makroNameTextField.setEnabled(false);
					makroContentTextPane.setText("");
					makroContentTextPane.setEnabled(false);
				}
			}

		});
		// makronamen in den Daten aktualisieren
		makroNameTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				makros.get(makroList.getSelectedIndex()).setName(
						makroNameTextField.getText());
			}
		});
		// Makroinhalt in Daten aktualisieren
		makroContentTextPane.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				makros.get(makroList.getSelectedIndex()).setContent(
						makroContentTextPane.getText());
			}
		});
		// neues Makro
		newMakroMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(null,
						"Makronamen eingeben: ", "Neues Makro", 1);
				if (name != null) {
					addMakro(name.replaceAll(" ", ""), "");
				}
			}
		});
		// makro löschen
		removeMakroMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selection = makroList.getSelectedIndex();
				if (selection != -1) {
					removeMakro(selection);
				}
			}
		});
		// Datei speichern
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Document xmlTree = null;
				try {
					// Dokument erstellen
					xmlTree = DocumentBuilderFactory.newInstance()
							.newDocumentBuilder().newDocument();
					// Wurzelknoten erschaffen und Attribute hinzufügen
					Element xmlRoot = xmlTree.createElement("makro");
					xmlTree.appendChild(xmlRoot);
					Element xmlChild;
					for (ElementAttribute<String> child : makros) {
						xmlChild = xmlTree.createElement(child.getName());
						xmlChild.setAttribute("name", child.getName());
						xmlChild.setTextContent(child.getContent());
						xmlRoot.appendChild(xmlChild);
					}
				} catch (ParserConfigurationException e1) {
					e1.printStackTrace();
				}
				// Fragebogen in Datei speichern
				try {
					if (xmlTree != null) {
						TransformerFactory
								.newInstance()
								.newTransformer()
								.transform(new DOMSource(xmlTree),
										new StreamResult("makro.xml"));
					}
				} catch (TransformerConfigurationException e1) {
					e1.printStackTrace();
				} catch (TransformerException e1) {
					e1.printStackTrace();
				} catch (TransformerFactoryConfigurationError e1) {
					e1.printStackTrace();
				}
			}
		});
		//drag and drop
		MouseInputAdapter mouseHandler = new MouseInputAdapter() {
			private String makroName, makroContent;
			private int fromIndex;

			public void mousePressed(final MouseEvent evt) {
				makroName = makroNameTextField.getText();
				makroContent = makroContentTextPane.getText();
				fromIndex = makroList.getSelectedIndex();
			}

			public void mouseDragged(final MouseEvent evt) {
				int toIndex = makroList.locationToIndex(evt.getPoint());
				if (toIndex != fromIndex) {
					removeMakro(fromIndex);
					insertMakro(toIndex, makroName, makroContent);
					fromIndex = toIndex;
				}
			}
		};
		makroList.addMouseListener(mouseHandler);
		makroList.addMouseMotionListener(mouseHandler);

		loadMakros();
	}

	private void loadMakros() {
		makros = new ArrayList<ElementAttribute<String>>();
		try {
			// Document lesen
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse("makro.xml");
			// Wurzel holen
			Node xmlRoot = doc.getFirstChild();
			Node child = null;
			NodeList children = xmlRoot.getChildNodes();
			String makroName = "";
			String makroContent = "";
			for (int i = 0; i < children.getLength(); i++) {
				child = children.item(i);
				makroName = child.getAttributes().getNamedItem("name")
						.getNodeValue();
				makroContent = child.getTextContent();
				addMakro(makroName, makroContent);
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void addMakro(String makroName, String makroContent) {
		makros.add(new ElementAttribute<String>(makroName, makroContent));
		listModel.addElement(makroName);
	}

	private void removeMakro(int index) {
		makros.remove(index);
		listModel.remove(index);
	}

	private void insertMakro(int index, String makroName, String makroContent) {
		makros.add(index, new ElementAttribute<String>(makroName, makroContent));
		listModel.add(index, makroName);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

}
