package experimentEditor.ContentEdit.ToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import experimentEditor.EditArea;


@SuppressWarnings("serial")
public class MacroBox extends JComboBox implements ActionListener, KeyListener {

	private ArrayList<String> macros;

	EditArea textPane;

	public MacroBox(EditArea textPane) {
		super();

		macros = new ArrayList<String>();
		this.textPane = textPane;
		this.addItem("Makroauswahl");
		this.addActionListener(this);
		textPane.addKeyListener(this);
		loadMakros();
	}

	private void loadMakros() {
		try {
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new File("macro.xml"));
			Node xmlRoot = doc.getFirstChild();
			NodeList xmlMakros = xmlRoot.getChildNodes();
			for (int i = 0; i < xmlMakros.getLength(); i++) {
				String makroName = xmlMakros.item(i).getAttributes()
						.getNamedItem("name").getNodeValue();
				String makroContent = xmlMakros.item(i).getTextContent();
				if (i <= 9) {
					makroName += "\t [Strg + " + (i + 1) % 10 + "]";
				}
				this.addItem(makroName);
				macros.add(makroContent);
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.getSelectedIndex() != 0) {
			String s = macros.get(this.getSelectedIndex() - 1);
			int index = s.indexOf("%s");
			if(index != -1) {
				textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
			} else {
				textPane.replaceSelection(macros.get(this.getSelectedIndex() - 1));
			}
			this.setSelectedIndex(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.isControlDown()) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_1:
				if (macros.size() >= 1) {
					String s = macros.get(0);
					int index = s.indexOf("%s");
					if(index != -1) {
						textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
					} else {
						textPane.replaceSelection(s);
					}
				}
				break;
			case KeyEvent.VK_2:
				if (macros.size() >= 2) {
					String s = macros.get(1);
					int index = s.indexOf("%s");
					if(index != -1) {
						textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
					} else {
						textPane.replaceSelection(s);
					}
				}
				break;
			case KeyEvent.VK_3:
				if (macros.size() >= 3) {
					String s = macros.get(2);
					int index = s.indexOf("%s");
					if(index != -1) {
						textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
					} else {
						textPane.replaceSelection(s);
					}
				}
				break;
			case KeyEvent.VK_4:
				if (macros.size() >= 4) {
					String s = macros.get(3);
					int index = s.indexOf("%s");
					if(index != -1) {
						textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
					} else {
						textPane.replaceSelection(s);
					}
				}
				break;
			case KeyEvent.VK_5:
				if (macros.size() >= 5) {
					String s = macros.get(5);
					int index = s.indexOf("%s");
					if(index != -1) {
						textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
					} else {
						textPane.replaceSelection(s);
					}
				}
				break;
			case KeyEvent.VK_6:
				if (macros.size() >= 6) {
					String s = macros.get(5);
					int index = s.indexOf("%s");
					if(index != -1) {
						textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
					} else {
						textPane.replaceSelection(s);
					}
				}
				break;
			case KeyEvent.VK_7:
				if (macros.size() >= 7) {
					String s = macros.get(6);
					int index = s.indexOf("%s");
					if(index != -1) {
						textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
					} else {
						textPane.replaceSelection(s);
					}
				}
				break;
			case KeyEvent.VK_8:
				if (macros.size() >= 8) {
					String s = macros.get(7);
					int index = s.indexOf("%s");
					if(index != -1) {
						textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
					} else {
						textPane.replaceSelection(s);
					}
				}
				break;
			case KeyEvent.VK_9:
				if (macros.size() >= 9) {
					String s = macros.get(8);
					int index = s.indexOf("%s");
					if(index != -1) {
						textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
					} else {
						textPane.replaceSelection(s);
					}
				}
				break;
			case KeyEvent.VK_0:
				if (macros.size() >= 10) {
					String s = macros.get(9);
					int index = s.indexOf("%s");
					if(index != -1) {
						textPane.setEmbedding(s.substring(0, index), s.substring(index+2));
					} else {
						textPane.replaceSelection(s);
					}
				}
				break;
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}
}
