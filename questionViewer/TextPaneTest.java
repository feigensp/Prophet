package questionViewer;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class TextPaneTest extends JFrame {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		frame.setContentPane(contentPane);

		JEditorPane textPane = new JEditorPane();

		textPane.setEditable(false);

		textPane.setEditorKit(new HTMLEditorKit() {
			public ViewFactory getViewFactory() {
				return new HTMLEditorKit.HTMLFactory() {
					public View create(Element elem) {
						Object o = elem.getAttributes().getAttribute(
								StyleConstants.NameAttribute);
						if (o instanceof HTML.Tag) {
							HTML.Tag kind = (HTML.Tag) o;
							if (kind == HTML.Tag.INPUT)
								return new FormView(elem) {

									protected void submitData(String data) {
										System.out.println("Daten: " + data);
									}

//									public void actionPerformed(ActionEvent ae) {
//										if (ae.getActionCommand().equals(
//												"Weiter")) {
//											System.out.println("weiter");
//										} else if (ae.getActionCommand()
//												.equals("Zurück")) {
//											System.out.println("zurück");
//										}
//									}
								};
						}
						return super.create(elem);
					}
				};
			}
		});

		textPane.setText("test 1<br><input name='testInput' type='text' value='Dies ist ein Test'><br><br><input name ='zurück' type='submit' value='Zurück'><input name ='vor' type='submit' value='Weiter'>");

		contentPane.add(textPane);

		frame.setVisible(true);
	}
}
