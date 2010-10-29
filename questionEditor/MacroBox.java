package questionEditor;

/**
 * Todo: makros aus xml datei laden
 */

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

public class MacroBox extends JComboBox implements ActionListener, KeyListener {

	public static final String TABLEROWDARK5RADIO = "Tabellenzeile (8 Radio - dunkel)";
	public static final String TABLEROWLIGHT5RADIO = "Tabellenzeile (8 Radio - hell)";

	private ArrayList<String> makros;

	JTextPane textPane;

	public MacroBox(JTextPane textPane) {
		super();

		makros = new ArrayList<String>();
		this.textPane = textPane;
		this.addItem("Makroauswahl");
		this.addActionListener(this);
		loadMakros();
	}

	private void loadMakros() {
		try {
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new File("makro.xml"));
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
				makros.add(makroContent);
				textPane.addKeyListener(this);
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
			textPane.replaceSelection(makros.get(this.getSelectedIndex() - 1));
			this.setSelectedIndex(0);
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.isControlDown()) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_1:
				if (makros.size() >= 1 && ke.isControlDown()) {
					System.out.println("test");
					textPane.replaceSelection(makros.get(0));
				}
				break;
			case KeyEvent.VK_2:
				if (makros.size() >= 1 && ke.isControlDown()) {
					textPane.replaceSelection(makros.get(1));
				}
				break;
			case KeyEvent.VK_3:
				if (makros.size() >= 1 && ke.isControlDown()) {
					textPane.replaceSelection(makros.get(2));
				}
				break;
			case KeyEvent.VK_4:
				if (makros.size() >= 1 && ke.isControlDown()) {
					textPane.replaceSelection(makros.get(3));
				}
				break;
			case KeyEvent.VK_5:
				if (makros.size() >= 1 && ke.isControlDown()) {
					textPane.replaceSelection(makros.get(4));
				}
				break;
			case KeyEvent.VK_6:
				if (makros.size() >= 1 && ke.isControlDown()) {
					textPane.replaceSelection(makros.get(5));
				}
				break;
			case KeyEvent.VK_7:
				if (makros.size() >= 1 && ke.isControlDown()) {
					textPane.replaceSelection(makros.get(6));
				}
				break;
			case KeyEvent.VK_8:
				if (makros.size() >= 1 && ke.isControlDown()) {
					textPane.replaceSelection(makros.get(7));
				}
				break;
			case KeyEvent.VK_9:
				if (makros.size() >= 1 && ke.isControlDown()) {
					textPane.replaceSelection(makros.get(8));
				}
				break;
			case KeyEvent.VK_0:
				if (makros.size() >= 1 && ke.isControlDown()) {
					textPane.replaceSelection(makros.get(9));
				}
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}
}
