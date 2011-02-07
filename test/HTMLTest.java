package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class HTMLTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private RSyntaxTextArea textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HTMLTest frame = new HTMLTest();
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
	public HTMLTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JButton button = new JButton("New button");
		contentPane.add(button, BorderLayout.SOUTH);

		textPane = new RSyntaxTextArea();
		textPane.setText("<html><head></head><body><form id=\"formname\">"
				+ "frage 1 test<br><input name=\"vorname\" type=\"text\" size=\"5\">"
				+ "<br><br><input name ='nextQuestion' type='submit' value='Weiter'>" + "<form></body></head>");
		contentPane.add(textPane, BorderLayout.CENTER);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				HTMLEditorKit htmlKit = new HTMLEditorKit();
				HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
				HTMLEditorKit.Parser parser = new ParserDelegator();
				HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
				StringReader reader = new StringReader(textPane.getText());
				System.out.println(textPane.getText());
				try {
					parser.parse(reader, callback, true);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					reader.close();
				}
				try {
					System.out.println("test" + ":" + htmlDoc.getText(0, htmlDoc.getLength()));
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (HTMLDocument.Iterator iterator = htmlDoc.getIterator(HTML.Tag.INPUT); iterator.isValid(); iterator
						.next()) {
					AttributeSet attributes = iterator.getAttributes();
					String srcString = (String) attributes.getAttribute(HTML.Attribute.NAME);
					System.out.println(srcString);
				}
				for (HTMLDocument.Iterator iterator = htmlDoc.getIterator(HTML.Tag.SELECT); iterator
						.isValid(); iterator.next()) {
					AttributeSet attributes = iterator.getAttributes();
					String srcString = (String) attributes.getAttribute(HTML.Attribute.NAME);
					System.out.println(srcString);
				}
				for (HTMLDocument.Iterator iterator = htmlDoc.getIterator(HTML.Tag.TEXTAREA); iterator
						.isValid(); iterator.next()) {
					AttributeSet attributes = iterator.getAttributes();
					String srcString = (String) attributes.getAttribute(HTML.Attribute.NAME);
					System.out.println(srcString);
				}
			}
		});
	}

}
