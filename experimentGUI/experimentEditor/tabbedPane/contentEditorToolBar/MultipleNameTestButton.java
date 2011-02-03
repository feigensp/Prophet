package experimentGUI.experimentEditor.tabbedPane.contentEditorToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;

import javax.swing.JButton;
import javax.xml.parsers.DocumentBuilder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

public class MultipleNameTestButton extends JButton implements ActionListener {

	private RSyntaxTextArea textArea;

	public MultipleNameTestButton(RSyntaxTextArea textArea, String text) {
		super(text);
		this.textArea = textArea;
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
//		Document doc = new DocumentBuilder().parse(new InputSource(new StringReader(
//				textArea.getText())));
//		NamedNodeMap nodeMap = doc.getAttributes();
//		for (int i = 0; i < nodeMap.getLength(); i++) {
//			System.out.println(nodeMap.item(i));
//		}
	}
}
