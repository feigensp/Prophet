package util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
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


@SuppressWarnings("serial")
public class MacroEditor extends JFrame {

	private JPanel contentPane;

	private JList macroList;
	private DefaultListModel listModel;
	private JTextPane macroContentTextPane;
	private JTextField macroNameTextField;

	private ArrayList<StringTupel> macros;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MacroEditor frame = new MacroEditor();
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
	public MacroEditor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 491, 300);

		JMenuBar macroMenuBar = new JMenuBar();
		setJMenuBar(macroMenuBar);

		JMenu macroMenu = new JMenu("Datei");
		macroMenuBar.add(macroMenu);

		JMenuItem saveMenuItem = new JMenuItem("Speichern");
		macroMenu.add(saveMenuItem);

		JMenuItem closeMenuItem = new JMenuItem("Beenden");
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		macroMenu.add(closeMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel overviewPanel = new JPanel();
		contentPane.add(overviewPanel, BorderLayout.WEST);
		overviewPanel.setLayout(new BorderLayout(0, 0));

		listModel = new DefaultListModel();
		macroList = new JList(listModel);
		macroList.setPreferredSize(new Dimension(150, 0));

		overviewPanel.add(macroList, BorderLayout.CENTER);

		JPopupMenu macroMenuPopupMenu = new JPopupMenu();
		addPopup(macroList, macroMenuPopupMenu);
		
		JMenuItem newMacroMenuItem = new JMenuItem("Neues Makro");
		macroMenuPopupMenu.add(newMacroMenuItem);

		JMenuItem removeMacroMenuItem = new JMenuItem("Makro L\u00F6schen");
		macroMenuPopupMenu.add(removeMacroMenuItem);

		JPanel macroPanel = new JPanel();
		contentPane.add(macroPanel, BorderLayout.CENTER);
		macroPanel.setLayout(new BorderLayout(0, 0));

		macroNameTextField = new JTextField();
		macroPanel.add(macroNameTextField, BorderLayout.NORTH);
		macroNameTextField.setColumns(10);

		macroContentTextPane = new JTextPane();
		macroPanel.add(macroContentTextPane, BorderLayout.CENTER);

		macroNameTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void inputChanged() {
				if(macroList.getSelectedIndex() != -1) {
					listModel.setElementAt(macroNameTextField.getText(), macroList.getSelectedIndex());
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				inputChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				inputChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				inputChanged();
			}
			
		});
		
		// makroansicht aktualisieren
		macroList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (macroList.getSelectedIndex() != -1) {
					macroNameTextField.setText(macros.get(
							macroList.getSelectedIndex()).getKey());
					macroContentTextPane.setText(macros.get(
							macroList.getSelectedIndex()).getValue());
					macroNameTextField.setEnabled(true);
					macroContentTextPane.setEnabled(true);
				} else {
					macroNameTextField.setText("");
					macroNameTextField.setEnabled(false);
					macroContentTextPane.setText("");
					macroContentTextPane.setEnabled(false);
				}
			}

		});
		// makronamen in den Daten aktualisieren
		macroNameTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				macros.get(macroList.getSelectedIndex()).setKey(
						macroNameTextField.getText());
			}
		});
		// Makroinhalt in Daten aktualisieren
		macroContentTextPane.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				macros.get(macroList.getSelectedIndex()).setValue(
						macroContentTextPane.getText());
			}
		});
		// neues Makro
		newMacroMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(null,
						"Makronamen eingeben: ", "Neues Makro", 1);
				if (name != null) {
					addMacro(name, "");
				}
			}
		});
		// makro löschen
		removeMacroMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selection = macroList.getSelectedIndex();
				if (selection != -1) {
					removeMacro(selection);
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
					Element xmlRoot = xmlTree.createElement("macros");
					xmlTree.appendChild(xmlRoot);
					Element xmlChild;
					for (StringTupel child : macros) {
						xmlChild = xmlTree.createElement("macro");
						xmlChild.setAttribute("name", child.getKey());
						xmlChild.setTextContent(child.getValue());
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
										new StreamResult("macro.xml"));
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
		MouseInputListener mouseHandler = new MouseInputAdapter() {
			private String macroName, macroContent;
			private int fromIndex;

			public void mousePressed(final MouseEvent evt) {
				macroName = macroNameTextField.getText();
				macroContent = macroContentTextPane.getText();
				fromIndex = macroList.getSelectedIndex();
			}

			public void mouseDragged(final MouseEvent evt) {
				if(macroList.getSelectedIndex() != -1) {
					int toIndex = macroList.locationToIndex(evt.getPoint());
					if (toIndex != fromIndex) {
						removeMacro(fromIndex);
						insertMacro(toIndex, macroName, macroContent);
						fromIndex = toIndex;
					}
				}
			}
		};
		macroList.addMouseListener(mouseHandler);
		macroList.addMouseMotionListener(mouseHandler);

		loadMacros();
	}

	private void loadMacros() {
		macros = new ArrayList<StringTupel>();
		try {
			// Document lesen
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse("macro.xml");
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
				addMacro(makroName, makroContent);
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void addMacro(String macroName, String makroContent) {
		macros.add(new StringTupel(macroName, makroContent));
		listModel.addElement(macroName);
	}

	private void removeMacro(int index) {
		macros.remove(index);
		listModel.remove(index);
	}

	private void insertMacro(int index, String macroName, String macroContent) {
		macros.add(index, new StringTupel(macroName, macroContent));
		listModel.add(index, macroName);
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
