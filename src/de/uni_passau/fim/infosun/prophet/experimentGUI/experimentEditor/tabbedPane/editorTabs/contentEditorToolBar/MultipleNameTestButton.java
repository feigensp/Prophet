package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class MultipleNameTestButton extends JButton implements ActionListener {

    private static final long serialVersionUID = 1L;
//	private RSyntaxTextArea textArea;

    public MultipleNameTestButton(RSyntaxTextArea textArea, String text) {
        super(text);
//		this.textArea = textArea;
        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
//		Document doc = new DocumentBuilder().parse(new InputSource(new StringReader(
//				textArea.getText())));
//		NamedNodeMap nodeMap = doc.getAttributes();
//		for (int i = 0; i < nodeMap.getLength(); i++) {
//			System.out.println(nodeMap.item(i));
//		}
    }
}
