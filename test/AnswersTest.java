package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

public class AnswersTest extends JFrame implements ActionListener {

	private JPanel contentPane;
	static JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AnswersTest frame = new AnswersTest();
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
	public AnswersTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		textPane = new JTextPane();
		contentPane.add(textPane, BorderLayout.CENTER);

		JButton button = new JButton("New button");
		button.addActionListener(this);
		contentPane.add(button, BorderLayout.NORTH);
	}

	public void actionPerformed(ActionEvent ae){
		URL url = null;
		try {
			url = new URL("http://www.java2s.com");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		URLConnection connection;
		InputStream is;
		HTMLDocument htmlDoc = null;
		try {
			connection = url.openConnection();
			is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		HTMLEditorKit htmlKit = new HTMLEditorKit();
		htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
		HTMLEditorKit.Parser parser = new ParserDelegator();
		HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
		parser.parse(br, callback, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (HTMLDocument.Iterator iterator = htmlDoc.getIterator(HTML.Tag.A); iterator.isValid(); iterator
				.next()) {

			AttributeSet attributes = iterator.getAttributes();
			String srcString = (String) attributes.getAttribute(HTML.Attribute.HREF);
			System.out.print(srcString);
			int startOffset = iterator.getStartOffset();
			int endOffset = iterator.getEndOffset();
			int length = endOffset - startOffset;
			String text = null;
			try {
				text = htmlDoc.getText(startOffset, length);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			System.out.println(" - " + text);
		}
	}
}

/*
 * Hallo.<br> Gib deinen Namen ein: <input name="name"><br> Gib dein Alter ein:
 * <input name="alter"><br> <table width="100%">
 * 
 * <tr align=center>
 * 
 * <td>Programmiererfahrung</td>
 * 
 * <td><input type="radio" name="Programmiererfahrung" value="0"></td>
 * 
 * <td><input type="radio" name="Programmiererfahrung" value="1"></td>
 * 
 * <td><input type="radio" name="Programmiererfahrung" value="2"></td>
 * 
 * <td><input type="radio" name="Programmiererfahrung" value="3"></td>
 * 
 * <td><input type="radio" name="Programmiererfahrung" value="4"></td>
 * 
 * </tr> <tr align=center style="background-color:silver">
 * 
 * <td>Leer Zeichen</td>
 * 
 * <td><input type="radio" name="Leer Zeichen" value="0"></td>
 * 
 * <td><input type="radio" name="Leer Zeichen" value="1"></td>
 * 
 * <td><input type="radio" name="Leer Zeichen" value="2"></td>
 * 
 * <td><input type="radio" name="Leer Zeichen" value="3"></td>
 * 
 * <td><input type="radio" name="Leer Zeichen" value="4"></td>
 * 
 * </tr> </table>
 */

