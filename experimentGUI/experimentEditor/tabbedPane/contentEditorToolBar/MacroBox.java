package experimentGUI.experimentEditor.tabbedPane.contentEditorToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


@SuppressWarnings("serial")
public class MacroBox extends JComboBox {

	private ArrayList<String> macros;

	private RSyntaxTextArea textPane;

	public MacroBox(RSyntaxTextArea textP) {
		macros = new ArrayList<String>();
		textPane = textP;
		addItem("Makroauswahl");
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getSelectedIndex() != 0) {
					useMacro(macros.get(getSelectedIndex() - 1));
					setSelectedIndex(0);
				}
			}
		});
		textPane.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				if (ke.isControlDown()) {
					int key = 0;
					switch (ke.getKeyCode()) {
					case KeyEvent.VK_1:
						key=1;
						break;
					case KeyEvent.VK_2:
						key=2;
						break;
					case KeyEvent.VK_3:
						key=3;
						break;
					case KeyEvent.VK_4:
						key=4;
						break;
					case KeyEvent.VK_5:
						key=5;
						break;
					case KeyEvent.VK_6:
						key=6;
						break;
					case KeyEvent.VK_7:
						key=7;
						break;
					case KeyEvent.VK_8:
						key=8;
						break;
					case KeyEvent.VK_9:
						key=9;
						break;
					case KeyEvent.VK_0:
						key=10;
						break;
					}
					if (key>0 && macros.size() >= key) {
						useMacro(macros.get(key-1));
					}
				}
			}
		});
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
	
	private void useMacro(String macroText) {
		String selectedText = textPane.getSelectedText() == null ? "" : textPane.getSelectedText();
		String replacementText = macroText.replaceAll("%s", selectedText);
		textPane.replaceSelection(replacementText);
	}
}
